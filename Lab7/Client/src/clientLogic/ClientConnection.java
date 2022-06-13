package clientLogic;


import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import exception.ScriptElementReaderException;
import utility.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;


public class ClientConnection {
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final Scanner fromKeyboard;
    private final CommandAnalyzer commandAnalyzer;
    private final HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    public ClientConnection(Scanner fromKeyboard, InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.fromKeyboard = fromKeyboard;
        nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        commandAnalyzer = new CommandAnalyzer();
    }

    private void init() throws IOException {
        try {
            Serializable response = executeCommand(new String[]{"technical"}, false, new NewElementReader());
            System.out.println(response);
            RudimentClass rudimentClass = new RudimentClass();
            LabWork.MAX_ID = rudimentClass.getMaxId();
            commandAnalyzer.setAvailableCommands(rudimentClass.getAvailableCommands());
            commandAnalyzer.setCommandsNeedArgument(rudimentClass.getCommandsNeedArgument());
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
        NewElementReader newElementReader = new NewElementReader();
        String command;
        try {
            while (true) {
                System.out.print("Вы хотите зарегистрироваться (1) или войти под существующим пользователем (2) ?: ");
                String answer = fromKeyboard.nextLine();
                if ("1".equals(answer)) {
                    dbRequest("register_user");
                    break;
                }
                if ("2".equals(answer)) {
                    dbRequest("connect_user");
                    break;
                }
            }

            while (!(command = fromKeyboard.nextLine()).equals("exit")) {
                String[] parsedCommand = command.trim().split(" ", 2);
                if ("".equals(parsedCommand[0])) {
                    System.out.print(Tool.PS2);
                    continue;
                }
                try {
                    System.out.println(executeCommand(parsedCommand, false, newElementReader));
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
                        executeCommand(parsedCommand, true, new NewElementReader());
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

    private Serializable executeCommand(String[] command, boolean isScriptExecuting, NewElementReader newElementReader) throws IOException {
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer();
        try {
            if (commandAnalyzer.analyzeCommand(command, isScriptExecuting)) {
                if (commandAnalyzer.isDBCommand()) {
                    dbRequest(commandAnalyzer.getCommandName());
                } else if (commandAnalyzer.getCommandName().equals("execute_script")) {
                    executeScript(commandAnalyzer.getCommandArgumentString());
                } else {

                    sendRequest(RequestCreator.createRequest(command, newElementReader, isScriptExecuting));
                    return receiveResponse();
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
        }
        throw new IllegalArgumentException();
    }

    private void dbRequest(String dbRequestName) throws IOException {
        commandAnalyzer.setCommandName(dbRequestName);
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
            sendRequest(request);
            ConnectResponse response = (ConnectResponse) receiveResponse();
            if (response.isClientConnected()) {
                clientConnected = true;
                RequestCreator.setClientName(clientName);
                RequestCreator.setClientPassword(clientPassword);
            }
            System.out.println(response.getConnectMessage());
        } while (!clientConnected);
    }

    private void sendRequest(Request request) throws IOException{
        Serializer serializer = new Serializer(request);
        if (serializer.possibleToSerialize()) {
            outputStream.write(serializer.serialize());
        } else {
            System.out.println("Невозможно сериализовать запрос");
        }
    }

    private Serializable receiveResponse() throws IOException {
        final int startBufferSize = 1024;
        ByteBuffer mainBuffer = ByteBuffer.allocate(0);
        while (true) {
            byte[] bytesToDeserialize = new byte[startBufferSize];
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int bytesCount = bis.read(bytesToDeserialize);
            ByteBuffer newBuffer = ByteBuffer.allocate(mainBuffer.capacity() + bytesCount);
            newBuffer.put(mainBuffer);
            newBuffer.put(ByteBuffer.wrap(bytesToDeserialize, 0, bytesCount));
            mainBuffer = ByteBuffer.wrap(newBuffer.array());

            Deserializer deserializer = new Deserializer(mainBuffer.array());
            if (deserializer.possibleToDeserialize()) {
                return deserializer.deserialize();
            } else {
                List<ByteBuffer> buffers = new ArrayList<>();
                int bytesLeft = bis.available();
                int len = bytesLeft;
                while (bytesLeft > 0) {
                    byte[] leftBytesToSerialize = new byte[bytesLeft];
                    if (bis.read(leftBytesToSerialize) == -1) {
                        throw new IOException("Сервер не работает");
                    }
                    buffers.add(ByteBuffer.wrap(leftBytesToSerialize));
                    bytesLeft = bis.available();
                    len += bytesLeft;
                }
                newBuffer = ByteBuffer.allocate(len + mainBuffer.capacity());
                newBuffer.put(mainBuffer);
                buffers.forEach(newBuffer::put);
                mainBuffer = ByteBuffer.wrap(newBuffer.array());

                deserializer = new Deserializer(mainBuffer.array());
                if (deserializer.possibleToDeserialize()) {
                    return deserializer.deserialize();
                }
            }
        }
    }

    private void exit() {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}
