package clientLogic;

import data.initial.LabWork;
import exception.DeserializeException;
import exception.ScriptElementReaderException;
import utility.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private final String clientName;
    private final String clientPassword;

    public ClientConnection(SocketChannel socketChannel, Selector selector, Scanner fromKeyboard,
                            String clientName, String clientPassword) {
        this.clientName = clientName;
        this.clientPassword = clientPassword;
        this.socketChannel = socketChannel;
        this.selector = selector;
        this.fromKeyboard = fromKeyboard;
        nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        commandAnalyzer = new CommandAnalyzer();
    }

    private void init() throws IOException {
        try {
            Response response = executeCommand(new String[]{"technical"}, false);
            System.out.println(response);
            commandAnalyzer.setAvailableCommands(response.getAvailableCommands());
            commandAnalyzer.setCommandsNeedArgument(response.getCommandsNeedArgument());
        } catch (IOException ioException) {
            System.err.print("Кажется сервер решил отдохнуть. ");
            throw new IOException();
        }
    }

    public void work() throws IOException {
        try {
            init();

            interactiveMode();
        } catch (NullPointerException nullPointerException) {
            System.err.println("Solve the problem from work() method, buddy");
        }
    }


    private void interactiveMode() throws IOException {

        while (true) {
            System.out.print("Вы хотите зарегистрироваться (0) или войти под существующим пользователем (1) ?: ");
            String answer = fromKeyboard.nextLine();
            if ("0".equals(answer)) {
                dbRequest("register_user");
                break;
            }
            if ("1".equals(answer)) {
                dbRequest("connect_user");
                break;
            }
        }

        String command;
        try {
            while (!(command = fromKeyboard.nextLine()).equals("exit")) {
                String[] parsedCommand = command.trim().split(" ", 2);
                if ("".equals(parsedCommand[0])) {
                    System.out.print(Tool.PS2);
                    continue;
                }
                try {
                    System.out.println(executeCommand(parsedCommand, false));
                } catch (IllegalArgumentException illegalArgumentException) {
                    System.err.println("Введена некорректная команда. Воспользуйтесь 'help'-инструкцией.\n");
                }
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
            System.err.println("В файле присутствует конструкция, которая приводит к рекурсии!\n" +
                    "Выполнение скрипта приостановлено");
        } else {
            nameOfFilesThatWasBroughtToExecuteMethod.add(scriptPath);
            try {
                File scriptFile = new File(scriptPath);
                if (scriptFile.canRead()) {
                    String[] elementLine;
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
                        executeCommand(parsedCommand, true);
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
            } catch (IllegalArgumentException illegalArgumentException) {
                System.err.println("+1 некорректная команда.");
            } catch (IOException ioException) {
                throw new IOException();
            }
        }
    }

    private Response executeCommand(String[] command, boolean isScriptExecuting) throws IOException {
        try {
            if (commandAnalyzer.analyzeCommand(command, isScriptExecuting)) {
                if (commandAnalyzer.getCommandName().equals("execute_script")) {
                    executeScript(commandAnalyzer.getCommandArgumentString());
                } else if (commandAnalyzer.isDBCommand()) {
                    dbRequest(commandAnalyzer.getCommandName());
                } else {
                    Request request = new Request(commandAnalyzer, clientName, clientPassword);
                    byte[] serializedRequest = Serializer.serializeRequest(request);
                    socketChannel.write(ByteBuffer.wrap(serializedRequest));    // свернули в буффер и записали в канал
                    return receiveResponse();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (ScriptElementReaderException scriptElementReaderException) {
            System.err.println("В введенных данных недостаточно аргументов для создания нового элемента.");
        } catch (NumberFormatException numberFormatException) {
            System.err.println("Введеныные данные содержат неверный формат.");
        }
        throw new IllegalArgumentException();
    }

    private void dbRequest(String dbRequestName) throws IOException {
        boolean clientConnected = false;
        do {
            System.out.print("Введите имя пользователя: ");
            String clientName = fromKeyboard.nextLine();
            if (clientName.isEmpty()) {
                System.out.println("Имя клиента не может быть пустым");
                continue;
            }
            System.out.print("Введите пароль: ");
            String clientPassword = fromKeyboard.nextLine();
            if (clientPassword.isEmpty()) {
                System.out.println("Пароль не может быть пустым");
                continue;
            }
            Request request = new Request(commandAnalyzer, clientName, clientPassword);
            byte[] serializedRequest = Serializer.serializeRequest(request);
            socketChannel.write(ByteBuffer.wrap(serializedRequest));    // свернули в буффер и записали в канал

            Response response = receiveResponse();





            if (response.isClientConnected()) {
                clientConnected = true;
                RequestCreator.setClientName(clientName);
                RequestCreator.setClientPassword(clientPassword);
            }
            System.out.println(response.getConnectMessage());
        } while (!clientConnected);





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
                    byteBuffer.flip();
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
