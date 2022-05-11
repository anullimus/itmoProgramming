package clientLogic;


import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import exception.DeserializeException;
import exception.ScriptElementReaderException;
import serverLogic.Tool;
import utility.*;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.nio.channels.Selector;
import java.util.Set;


public class ClientConnection {
    private Selector selector;
    private static Scanner fromKeyboard;
    private SocketChannel socketChannel;
    private final InetSocketAddress socketAddress;
    private static CommandAnalyzer commandAnalyzer;
    private HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    public ClientConnection(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;

    }

    public void work() {
        try (Scanner scanner = new Scanner(System.in)) {
            nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
            commandAnalyzer = new CommandAnalyzer();
            fromKeyboard = scanner;

            selector = Selector.open();
            while (true)
                try {
                    socketChannel = SocketChannel.open(socketAddress);
                    break;
                } catch (ConnectException connectException) {
                    System.err.println("Нет связи с сервером. Подключться ещё раз (введите {да} или {нет})?");
                    String answer;
                    while (!(answer = fromKeyboard.nextLine()).equals("да")) {
                        switch (answer) {
                            case "":
                                break;
                            case "нет":
                                exit();
                                break;
                            default:
                                System.out.println("Введите корректный ответ.");
                        }
                    }
                    System.out.print("Подключение ...\n");
                }

            socketChannel.finishConnect();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);


            // doubled code below, but I don't care. It's for init with server. Doesn't need response
            commandAnalyzer.analyzeCommand(new String[]{"help"}, false);
            Request request = new Request(commandAnalyzer);
            byte[] serializedRequest = Serializer.serializeRequest(request);
            socketChannel.write(ByteBuffer.wrap(serializedRequest));
            Response response = receiveResponse();
            System.out.println(response);
            LabWork.MAX_ID = response.getMaxIdInCollection();

            interactiveMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void interactiveMode() {
        String command;
        while (!(command = fromKeyboard.nextLine()).equals("exit")) {
            try {
                String[] parsedCommand = command.trim().split(" ", 2);
                if ("".equals(parsedCommand[0])) {
                    System.out.print(Tool.PS2);
                    continue;
                }
                kitchen(parsedCommand, false);
                System.out.println(Tool.PS1 + "Введите команду: ");
                System.out.print(Tool.PS2);
            } catch (Exception exception) {
                System.err.println("Видимо пока...");
                exit();
            }
        }
        exit();
    }

    private void executeScript(String scriptPath) {
        if (nameOfFilesThatWasBroughtToExecuteMethod.contains(scriptPath)) {
            System.out.println("В файле присутствует конструкция, которая приводит к рекурсии!\n" +
                    "Выполнение скрипта приостановлено");
        } else {
            nameOfFilesThatWasBroughtToExecuteMethod.add(scriptPath);
            try {
                File scriptFile = new File(scriptPath);
                if (scriptFile.canRead()) {
                    String[] elementLine = {};
                    Scanner fromScript = new Scanner(new FileReader(scriptPath));
                    while (fromScript.hasNextLine()) {
                        String[] parsedCommand = fromScript.nextLine().trim().split(" ", 2);
                        if (parsedCommand[0].equals("add") || parsedCommand[0].equals("add_if_min")) {
                            int elementsInLabwork = 11;
                            elementLine = new String[elementsInLabwork];
                            for (int i = 0; i < elementsInLabwork; i++) {
                                elementLine[i] = (fromScript.nextLine().trim());
                            }
                            commandAnalyzer.setAddDataFromScript(elementLine);
                        }
                        kitchen(parsedCommand, true);
                    }
                    nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
                    System.out.println("Исполнение скрипта завершено.");
                } else {
                    throw new SecurityException();
                }
            } catch (SecurityException securityException) {
                nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
                System.err.println("У вас нет доступа к файл-скрипту.");
            } catch (FileNotFoundException fileNotFoundException) {
                System.err.println("Файл-скрипт не найден.");
            } catch (JsonSyntaxException jsonSyntaxException) {
                System.err.println("Ошибка в синтаксисе JSON. Не удалось добавить элемент.");
            } catch (IllegalArgumentException illegalArgumentException) {
                System.err.println("+1 некорректная команда.");
            }
        }
    }

    private void kitchen(String[] command, boolean isScriptExecuting) {
        try {
            if (commandAnalyzer.analyzeCommand(command, isScriptExecuting)) {
                if (commandAnalyzer.getCommandName().equals("execute_script")) {
                    executeScript(commandAnalyzer.getCommandArgumentString());
                } else {
                    Request request = new Request(commandAnalyzer);
                    byte[] serializedRequest = Serializer.serializeRequest(request);
                    socketChannel.write(ByteBuffer.wrap(serializedRequest));    // свернули в буффер и записали в канал
                    System.out.println(receiveResponse());
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (ScriptElementReaderException scriptElementReaderException) {
            System.err.println("В введенных данных недостаточно аргументов для создания нового элемента.");
        } catch (JsonSyntaxException ex) {
            System.err.println("Ошибка в синтаксисе JSON. Не удалось добавить элемент.");
        } catch (NumberFormatException e) {
            System.err.println("Введеныные данные содержат неверный формат.");
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println("Введена некорректная команда. Воспользуйтесь 'help'-инструкцией.\n");
        } catch (IOException ioException) {
            System.err.println("Похоже на то, что сервер приостановил свою работу.");
            exit();
        }
    }

    private Response receiveResponse() throws IOException {
        final int bufferSize = 1024;
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            SelectionKey key = iter.next();
            try {
                if (key.isReadable()) {
                    int bytesRead = socketChannel.read(byteBuffer);
                    if (bytesRead <= 0) {
                        continue;
                    }
                    ((Buffer) byteBuffer).flip();
                    byte[] serializedResponse = new byte[byteBuffer.remaining()];
                    byteBuffer.get(serializedResponse);
                    try {
                        return Deserializer.deserializeResponse(serializedResponse);
                    } catch (DeserializeException deserializeException) {
                        byteBuffer = ByteBuffer.allocate(byteBuffer.capacity() + byteBuffer.capacity() * 2);
                        byteBuffer.put(serializedResponse);
                    }
                }
            } catch (IOException ioException) {
                new Response("An existing connection was forcibly closed by the remote host");
            }
        }
    }

    private void exit() {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}
