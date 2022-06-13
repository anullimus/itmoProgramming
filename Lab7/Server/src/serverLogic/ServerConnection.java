package serverLogic;

import command.*;
import data.initial.Difficulty;
import data.initial.LabWork;
import exception.DeserializeException;
import exception.SerializeException;
import utility.Deserializer;
import utility.Request;
import utility.Response;
import utility.Serializer;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;

public class ServerConnection implements Runnable {
    private final CollectionManager collectionManager;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream sendToClient;
    private final CommandManager commandManager;
    private final Socket socket;

//    private Receiver receiver;
//    private final Scanner scanner;

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public ServerConnection(CollectionManager collectionManager, Socket socket, BufferedInputStream inputStream,
                            BufferedOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.sendToClient = outputStream;
        this.collectionManager = collectionManager;
        this.commandManager = new CommandManager(collectionManager);

    }

    private void init() throws IOException {
        Response greetingResponse = new Response("Соединение установлено! Вы можете вводить команды!",
                CollectionManager.MAX_ID, commandManager.getAvailableCommands(), commandManager.getCommandsNeedArgument());
        sendToClient.write(Serializer.serializeResponse(greetingResponse));
        sendToClient.flush();
//
        Receiver receiver = new Receiver(inputStream, socket);
        receiver.run(); // for init connect with client
//        receiveRequest(); // for init connect with client
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException | SerializeException | DeserializeException e) {
//            ServerLogger.logInfoMessage("Client {" + socket + "} has disconnected.\n");
            ServerLogger.logErrorMessage("EEEERRRRROOOOOOOR IN INIT WHYYYYYYY");
        }
        AbstractCommand errorCommand = new InfoCommand(null) {
            @Override
            public Response execute() {
                return new Response("Неизвестная команда. Введите 'help' для получения списка команд.");
            }
        };
        Request requestFromClient;
        ResponseSender responseSender;
        Receiver receiver;
        while (true) {
//                checkCommands();
//            requestFromClient = receiveRequest();
            receiver = new Receiver(inputStream, socket);
            receiver.run();
            while (!receiver.isFinished()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.print("\n");
                    Thread.currentThread().interrupt();
                }
            }
            requestFromClient = receiver.getRequestFromClient();

            ServerLogger.logInfoMessage("Client: " + socket + " sent request: " + requestFromClient.getCommandName());
            responseSender = new ResponseSender(requestFromClient, commandManager, errorCommand, sendToClient);
            responseSender.run();
        }

    }

//    private Request receiveRequest() throws IOException {
//        final int size = 1024;
//        byte[] serializedRequest = new byte[size];
//        int bytesRead = inputStream.read(serializedRequest);
//
//        if (bytesRead == -1) {
//            throw new IOException();
//        }
//        ServerLogger.logDebugMessage("Need to read {} bytes", bytesRead);
//
//        try {
//            return Deserializer.deserializeRequest(serializedRequest);
//        } catch (DeserializeException e) {
//            int last = inputStream.available();
//            int len = bytesRead;
//            List<ByteBuffer> list = new ArrayList<>();
//            while (last > 0) {
//                ServerLogger.logDebugMessage("Stayed to read {} bytes", last);
//
//                byte[] arr = new byte[last];
//                int bytes = inputStream.read(arr);
//                len += bytes;
//                if (bytes == -1) {
//                    throw new IOException();
//                }
//                list.add(ByteBuffer.wrap(arr));
//                ServerLogger.logDebugMessage("Read {} bytes", bytes);
//                last = inputStream.available();
//            }
//            ByteBuffer byteBuffer = ByteBuffer.allocate(len);
//            byteBuffer.put(serializedRequest);
//            list.forEach(byteBuffer::put);
//            return Deserializer.deserializeRequest(byteBuffer.array());
//        }
//    private Request receiveRequest() throws IOException {
//        Runnable receiver = new Receiver();
//        Thread thread = new Thread(receiver);
//        thread.start();
//        return Deserializer.deserializeRequest(byteBuffer.array());
//    }
//    }
}

class Receiver implements Runnable {
    private final BufferedInputStream inputStream;
    private boolean finished;
    private Request requestFromClient;
    private final Socket socket;

    public Receiver(BufferedInputStream inputStream, Socket socket) {
        this.inputStream = inputStream;
        this.socket = socket;
        finished =  false;
    }

    public Request getRequestFromClient() {
        return requestFromClient;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        try {
            final int size = 1024;
            byte[] serializedRequest = new byte[size];
            int bytesRead = inputStream.read(serializedRequest);

            if (bytesRead == -1) {
                throw new IOException();
            }
            ServerLogger.logDebugMessage("Need to read {} bytes", bytesRead);

            try {
                requestFromClient = Deserializer.deserializeRequest(serializedRequest);
                finished = true;
            } catch (DeserializeException e) {
                int last = inputStream.available();
                int len = bytesRead;
                List<ByteBuffer> list = new ArrayList<>();
                while (last > 0) {
                    ServerLogger.logDebugMessage("Stayed to read {} bytes", last);

                    byte[] arr = new byte[last];
                    int bytes = inputStream.read(arr);
                    len += bytes;
                    if (bytes == -1) {
                        throw new IOException();
                    }
                    list.add(ByteBuffer.wrap(arr));
                    ServerLogger.logDebugMessage("Read {} bytes", bytes);
                    last = inputStream.available();
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(len);
                byteBuffer.put(serializedRequest);
                list.forEach(byteBuffer::put);
                requestFromClient = Deserializer.deserializeRequest(byteBuffer.array());
                finished = true;
            }
        } catch (IOException ioException) {
            ServerLogger.logInfoMessage("Client {" + socket + "} has disconnected.\n");
        }
    }
}

class ResponseSender implements Runnable {
    Response response;
    Request requestFromClient;
    CommandManager commandManager;
    AbstractCommand errorCommand;
    BufferedOutputStream sendToClient;

    public ResponseSender(Request requestFromClient, CommandManager commandManager,
                          AbstractCommand errorCommand, BufferedOutputStream sendToClient) {
        this.requestFromClient = requestFromClient;
        this.commandManager = commandManager;
        this.errorCommand = errorCommand;
        this.sendToClient = sendToClient;
    }

    @Override
    public void run() {
        if (requestFromClient.isCommandHaveArgument()) {
            response = (Response) commandManager.getAvailableCommandsWithDescription().
                    getOrDefault(requestFromClient.getCommandName(), errorCommand).
                    execute(requestFromClient);
        } else {
            response = commandManager.getAvailableCommandsWithDescription().
                    getOrDefault(requestFromClient.getCommandName(), errorCommand).execute();
        }
//                ServerLogger.logInfoMessage("Result of executing the program: {}", response);
        byte[] serializedResponse = Serializer.serializeResponse(response);
        try {
            sendToClient.write(serializedResponse);
            sendToClient.flush();
            ServerLogger.logInfoMessage("Answer was successfully sent." + "\n");
        } catch (IOException ioException) {
            ServerLogger.logErrorMessage("Answer wasn't sent." + "\n");
        }
    }

}
