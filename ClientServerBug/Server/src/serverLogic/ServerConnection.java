package serverLogic;

import data.initial.Difficulty;
import data.initial.LabWork;
import exception.DeserializeException;
import exception.SerializeException;
import utility.Deserializer;
import utility.Request;
import command.*;
import utility.Response;
import utility.Serializer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Provides the full information of commands that manage the collection{@link CollectionManager} and some else technical
 * elements.
 */
public class ServerConnection {
    private HashMap<String, Command> availableCommandsWithDescription;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
    private CollectionManager serverCollection;
    private BufferedInputStream inputStream;
    private BufferedOutputStream sendToClient;
    private Socket socket;

    public ServerConnection() {
        fillingSpecialCommandArrays();
    }

    /**
     * @param serverCollection обеспечивает доступ к коллекции.
     * @param socket           активное соединение с клиентской программой.
     *                         Команды, доступные клиенту, являются объектами {@link Command}, хранящимися в
     *                         {@code HashMap <String, AbstractCommand> availableCommands}.
     */
    public ServerConnection(CollectionManager serverCollection, Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new BufferedInputStream(socket.getInputStream());
        this.sendToClient = new BufferedOutputStream(socket.getOutputStream());
        this.serverCollection = serverCollection;
        fillingSpecialCommandArrays();
    }

    private void fillingSpecialCommandArrays() {
        availableCommandsWithDescription = new HashMap<>();
        commandsNeedArgument = new HashMap<>();
//        String relaxDesctiprtionMessage = "Чтобы использовать данную команду, вам достаточно ввести только ее " +
//                "сигнатуру, а заполнение элемента вам будет предложено в последующих строчках в интерактивном режиме.";
        availableCommandsWithDescription.put("info", new InfoCommand(serverCollection));
        availableCommandsWithDescription.put("show", new ShowCommand(serverCollection));
        availableCommandsWithDescription.put("add", new AddCommand(serverCollection));
        availableCommandsWithDescription.put("update_id", new UpdateIDCommand(serverCollection));
        availableCommandsWithDescription.put("remove_by_id", new RemoveByIDCommand(serverCollection));
        availableCommandsWithDescription.put("add_if_min", new AddIfMinCommand(serverCollection));
        availableCommandsWithDescription.put("remove_greater", new RemoveGreaterCommand(serverCollection));
        availableCommandsWithDescription.put("remove_lower", new RemoveLowerCommand(serverCollection));
        availableCommandsWithDescription.put("max_by_author", new MaxByAuthorCommand(serverCollection));
        availableCommandsWithDescription.put("count_less_than_author", new CountLessThanAuthorCommand(serverCollection));
        availableCommandsWithDescription.put("filter_by_difficulty", new FilterByDifficultyCommand(serverCollection));
        availableCommandsWithDescription.put("help", new HelpCommand(serverCollection, availableCommandsWithDescription));

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
                    "\nP.s. если не знаете, что вводить, просто введите команду 'help'");
            sendToClient.write(Serializer.serializeResponse(greetingResponse));
            sendToClient.flush();
            Command errorCommand = new Command(null) {
                @Override
                public Response execute() {
                    return new Response("Неизвестная команда. Введите 'help' для получения списка команд.");
                }
            };
            Request requestFromClient = receiveRequest();
            while (true) {
                System.out.print("Клиент: " + socket + " отправил запрос: " + requestFromClient);
//               todo: логирование

                Response response;
                if (requestFromClient.isCommandHaveArgument())
                    response = availableCommandsWithDescription.
                            getOrDefault(requestFromClient.getCommandName(), errorCommand).
                            execute(requestFromClient);
                else {
                    response = availableCommandsWithDescription.
                            getOrDefault(requestFromClient.getCommandName(), errorCommand).execute();
                }
                //               todo: логирование
                byte[] serializedResponse = Serializer.serializeResponse(response);
                sendToClient.write(serializedResponse);
                sendToClient.flush();
                //               todo: логирование

                requestFromClient = receiveRequest();
                System.out.println("Ответ успешно отправлен.");
            }
        } catch (IOException | SerializeException | DeserializeException e) {
            //e.printStackTrace();
            System.out.println("Клиент отключился");
//            ServerLogger.logInfoMessage("Клиент отключился");
            //               todo: логирование

        }
    }

    private Request receiveRequest() throws IOException {
        final int size = 1024;
        byte[] serializedRequest = new byte[size];
        int bytesRead = inputStream.read(serializedRequest);
        if (bytesRead == -1) {
            throw new IOException();
        }
//        ServerLogger.logDebugMessage("Прочитано {} байт", bytesRead);
//               todo: логирование

        try {
            return Deserializer.deserializeRequest(serializedRequest);
        } catch (DeserializeException e) {
            int last = inputStream.available();
            int len = bytesRead;
            List<ByteBuffer> list = new ArrayList<>();
            while (last > 0) {
//                ServerLogger.logDebugMessage("Осталось прочитать {} байт", last);
//                todo: логирование

                byte[] arr = new byte[last];
                int bytes = inputStream.read(arr);
                len += bytes;
                if (bytes == -1) {
                    throw new IOException();
                }
                list.add(ByteBuffer.wrap(arr));
//                ServerLogger.logDebugMessage("Прочитано {} байт", bytes);
//                todo: логирование
                last = inputStream.available();
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(len);
            byteBuffer.put(serializedRequest);
            list.forEach(byteBuffer::put);
            return Deserializer.deserializeRequest(byteBuffer.array());
        }
    }
}
