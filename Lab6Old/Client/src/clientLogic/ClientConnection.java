package clientLogic;


import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import exception.DeserializeException;
import exception.ScriptElementReaderException;
import utility.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;


public class ClientConnection {
    private final Selector selector;
    private final Scanner fromKeyboard;
    private final SocketChannel socketChannel;
    private final CommandAnalyzer commandAnalyzer;
    private final HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    public ClientConnection(SocketChannel socketChannel, Selector selector, Scanner fromKeyboard) {
        this.socketChannel = socketChannel;
        this.selector = selector;
        this.fromKeyboard = fromKeyboard;
        nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        commandAnalyzer = new CommandAnalyzer();
    }

    public void work() throws IOException {
        try {
            // doubled code below, but I don't care. It's for init with server. Doesn't need response
            commandAnalyzer.analyzeCommand(new String[]{"technical"}, false);
            Request request = new Request(commandAnalyzer);
            byte[] serializedRequest = Serializer.serializeRequest(request);
            socketChannel.write(ByteBuffer.wrap(serializedRequest));
            Response response = receiveResponse();
            System.out.println(response);
            LabWork.MAX_ID = response.getMaxIdInCollection();
            commandAnalyzer.setAvailableCommands(response.getAvailableCommands());
            commandAnalyzer.setCommandsNeedArgument(response.getCommandsNeedArgument());


            interactiveMode();
        } catch (IOException ioException) {
            System.err.print("Кажется сервер решил отдохнуть. ");
            throw new IOException();
        }
    }


    private void interactiveMode() throws IOException {
        String command;
        try {
            while (!(command = fromKeyboard.nextLine()).equals("exit")) {
                String[] parsedCommand = command.trim().split(" ", 2);
                if ("".equals(parsedCommand[0])) {
                    System.out.print(Tool.PS2);
                    continue;
                }
                kitchen(parsedCommand, false);
                System.out.println(Tool.PS1 + "Введите команду: ");
                System.out.print(Tool.PS2);
            }

        } catch (IOException ioException) {
            throw new IOException();
        } catch (Exception exception) {
            System.err.println("Видимо пока...");
        }
        exit();
    }

    private void executeScript(String scriptPath) throws IOException {
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
            } catch (IOException ioException) {
                throw new IOException();
            }
        }
    }

    private void kitchen(String[] command, boolean isScriptExecuting) throws IOException {
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
        } catch (JsonSyntaxException jsonSyntaxException) {
            System.err.println("Ошибка в синтаксисе JSON. Не удалось добавить элемент.");
        } catch (NumberFormatException numberFormatException) {
            System.err.println("Введеныные данные содержат неверный формат.");
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println("Введена некорректная команда. Воспользуйтесь 'help'-инструкцией.\n");
        } catch (IOException ioException) {
            throw new IOException();
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
