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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerConnection {
    private HashMap<String, AbstractCommand> availableCommandsWithDescription;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
    private final CollectionManager collectionManager;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream sendToClient;
    private final Socket socket;
    private final BufferedReader reader;

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public ServerConnection(CollectionManager collectionManager, Socket socket, BufferedInputStream inputStream,
                            BufferedOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.sendToClient = outputStream;
        this.collectionManager = collectionManager;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        fillingSpecialCommandArrays();
    }

    private void fillingSpecialCommandArrays() {
        availableCommandsWithDescription = new HashMap<>();
        commandsNeedArgument = new HashMap<>();
        availableCommandsWithDescription.put("info", new InfoCommand(collectionManager));
        availableCommandsWithDescription.put("show", new ShowCommand(collectionManager));
        availableCommandsWithDescription.put("add", new AddCommand(collectionManager));
        availableCommandsWithDescription.put("update_id", new UpdateIDCommand(collectionManager));
        availableCommandsWithDescription.put("remove_by_id", new RemoveByIDCommand(collectionManager));
        availableCommandsWithDescription.put("add_if_min", new AddIfMinCommand(collectionManager));
        availableCommandsWithDescription.put("remove_greater", new RemoveGreaterCommand(collectionManager));
        availableCommandsWithDescription.put("remove_lower", new RemoveLowerCommand(collectionManager));
        availableCommandsWithDescription.put("max_by_author", new MaxByAuthorCommand(collectionManager));
        availableCommandsWithDescription.put("count_less_than_author", new CountLessThanAuthorCommand(collectionManager));
        availableCommandsWithDescription.put("filter_by_difficulty", new FilterByDifficultyCommand(collectionManager));
        availableCommandsWithDescription.put("help", new HelpCommand(collectionManager, availableCommandsWithDescription));

        commandsNeedArgument.put("add", LabWork.class);
        commandsNeedArgument.put("add_if_min", LabWork.class);
        commandsNeedArgument.put("update_id", Long.class);
        commandsNeedArgument.put("remove_by_id", Long.class);
        commandsNeedArgument.put("remove_greater", LabWork.class);
        commandsNeedArgument.put("remove_lower", LabWork.class);
        commandsNeedArgument.put("count_less_than_author", String.class);
        commandsNeedArgument.put("filter_by_difficulty", Difficulty.class);
        commandsNeedArgument.put("execute_script", File.class);
        availableCommands = new ArrayList<>(availableCommandsWithDescription.keySet());
    }

    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }

    public void work() {
        try {
            Response greetingResponse = new Response("Соединение установлено! Вы можете вводить команды!" +
                    "\nP.s. если не знаете, что вводить, просто введите команду 'help'",
                    CollectionManager.MAX_ID, getAvailableCommands(), getCommandsNeedArgument());
            sendToClient.write(Serializer.serializeResponse(greetingResponse));
            sendToClient.flush();
            AbstractCommand errorCommand = new AbstractCommand(null) {
                @Override
                public Response execute() {
                    return new Response("Неизвестная команда. Введите 'help' для получения списка команд.");
                }
            };
            receiveRequest();   // оставляем, чтобы инициализировать успешное соединение
            Request requestFromClient;
            Response response;
            while (true) {

                requestFromClient = receiveRequest();
                ServerLogger.logInfoMessage("Client: " + socket + " sent request: "
                        + requestFromClient.getCommandName());

                if (requestFromClient.isCommandHaveArgument()) {
                    response = availableCommandsWithDescription.
                            getOrDefault(requestFromClient.getCommandName(), errorCommand).
                            execute(requestFromClient);
                } else {
                    response = availableCommandsWithDescription.
                            getOrDefault(requestFromClient.getCommandName(), errorCommand).execute();
                }
//                ServerLogger.logInfoMessage("Result of executing the program: {}", response);

                byte[] serializedResponse = Serializer.serializeResponse(response);
                sendToClient.write(serializedResponse);
                sendToClient.flush();
                ServerLogger.logInfoMessage("Answer was successfully sent." + "\n");
            }
        } catch (IOException | SerializeException | DeserializeException e) {
            ServerLogger.logInfoMessage("Client has disconnected");
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
}
