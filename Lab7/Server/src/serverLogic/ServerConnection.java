package serverLogic;

import command.AbstractCommand;
import db.DatabaseHandler;
import exception.DeserializeException;
import exception.ExitException;
import exception.SerializeException;
import utility.Deserializer;
import utility.Request;
import utility.Response;
import utility.Serializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ServerConnection implements Runnable{
//    private HashMap<String, AbstractCommand> availableCommandsWithDescription;
//    private ArrayList<String> availableCommands;
//    private Map<String, Class<?>> commandsNeedArgument;
    private final CollectionManager collectionManager;
    private final DatabaseHandler databaseHandler;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream sendToClient;
    private final Socket socket;
    private final CommandManager commandManager;
//    private final Scanner scanner;

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public ServerConnection(DatabaseHandler databaseHandler, CollectionManager collectionManager, Socket socket, BufferedInputStream inputStream,
                            BufferedOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.sendToClient = outputStream;
        this.collectionManager = collectionManager;
        this.databaseHandler = databaseHandler;
        this.commandManager = new CommandManager(databaseHandler, collectionManager);
    }


    private void init() throws IOException {
        Response greetingResponse = new Response("Соединение установлено! Вы можете вводить команды!",
                CollectionManager.MAX_ID, commandManager.getAvailableCommands(), commandManager.getCommandsNeedArgument());
        sendToClient.write(Serializer.serializeResponse(greetingResponse));
        sendToClient.flush();

        receiveRequest(); // for init connect with client
    }
    @Override
    public void run() {
        try {
            init();

            AbstractCommand errorCommand = new AbstractCommand(null,null) {
                @Override
                public Response execute() {
                    return new Response("Неизвестная команда. Введите 'help' для получения списка команд.");
                }
            };
            Request requestFromClient;
            Response response;
            while (true) {

//                checkCommands();
                requestFromClient = receiveRequest();
                ServerLogger.logInfoMessage("Client: " + socket + " sent request: "
                        + requestFromClient.getCommandName());

                if (requestFromClient.isCommandHaveArgument()) {
                    response = commandManager.getAvailableCommandsWithDescription().
                            getOrDefault(requestFromClient.getCommandName(), errorCommand).
                            execute(requestFromClient);
                } else {
                    response = commandManager.getAvailableCommandsWithDescription().
                            getOrDefault(requestFromClient.getCommandName(), errorCommand).execute();
                }
//                ServerLogger.logInfoMessage("Result of executing the program: {}", response);

                byte[] serializedResponse = Serializer.serializeResponse(response);
                sendToClient.write(serializedResponse);
                sendToClient.flush();
                ServerLogger.logInfoMessage("Answer was successfully sent." + "\n");
            }
        } catch (IOException | SerializeException | DeserializeException e) {
            throw new ExitException(socket.toString());
        }
    }

    private Request receiveRequest() throws IOException {
        final int size = 1024;
        byte[] serializedRequest = new byte[size];
        int bytesRead = inputStream.read(serializedRequest);

        if (bytesRead == -1) {
            throw new IOException();
        }
        ServerLogger.logDebugMessage("Need to read {} bytes", bytesRead);

        try {
            return Deserializer.deserializeRequest(serializedRequest);
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
            return Deserializer.deserializeRequest(byteBuffer.array());
        }
    }
//    public void checkCommands() throws IOException {
//        if (System.in.available() > 0) {
//            String line = "";
//            try {
//                line = scanner.nextLine();
//            } catch (NoSuchElementException noSuchElementException) {
//                line = "exit";
//            }
//            if ("save".equals(line)) {
//                collectionManager.save();
//                ServerLogger.logInfoMessage("Collection successfully has been saved!");
//            }
//            if ("exit".equals(line)) {
//                ServerLogger.logInfoMessage("Server finished working");
//            }
//        }
//    }
}

