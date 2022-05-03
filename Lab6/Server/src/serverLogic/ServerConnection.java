package serverLogic;

import command.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Provides the full information of commands that manage the collection{@link CollectionManager} and some else technical
 * elements.
 */
public class ServerConnection implements Runnable {
    private HashMap<String, AbstractCommand> availableCommandsWithDescription;
    private ArrayList<String> availableCommands;
    private ArrayList<String> commandsNeedArgument;
    private CollectionManager serverCollection;
    private Socket incoming;


    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    public ServerConnection() {
        fillingSpecialCommandArrays();
    }

    /**
     * @param serverCollection обеспечивает доступ к коллекции.
     * @param incoming         активное соединение с клиентской программой.
     *                         Команды, доступные клиенту, являются объектами {@link AbstractCommand}, хранящимися в
     *                         {@code HashMap <String, AbstractCommand> availableCommands}.
     */
    public ServerConnection(CollectionManager serverCollection, Socket incoming) {
        this.incoming = incoming;
        this.serverCollection = serverCollection;
        fillingSpecialCommandArrays();
    }

    private void fillingSpecialCommandArrays() {
        availableCommandsWithDescription = new HashMap<>();
        commandsNeedArgument = new ArrayList<>();
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
        availableCommandsWithDescription.put("man", new ManCommand(serverCollection, availableCommandsWithDescription));

        commandsNeedArgument.add("update_id");
        commandsNeedArgument.add("remove_by_id");
        commandsNeedArgument.add("remove_greater");
        commandsNeedArgument.add("remove_lower");
        commandsNeedArgument.add("max_by_author");
        commandsNeedArgument.add("count_less_than_author");
        commandsNeedArgument.add("filter_by_difficulty");
        commandsNeedArgument.add("man");
        commandsNeedArgument.add("add");
        availableCommands = new ArrayList<>(availableCommandsWithDescription.keySet());
    }

    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public ArrayList<String> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }

    @Override
    public void run() {
        try (ObjectInputStream getFromClient = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream sendToClient = new ObjectOutputStream(incoming.getOutputStream())) {
            sendToClient.writeObject("Соединение установлено! Вы можете вводить команды!" +
                    "\nP.s. если не знаете, что вводить, просто введите команду 'help'");
            AbstractCommand errorCommand = new AbstractCommand(null) {
                @Override
                public String execute() {
                    return "Неизвестная команда. Введите 'help' для получения списка команд.";
                }
            };
            while (true) {
                try {
                    String requestFromClient = (String) getFromClient.readObject();
                    System.out.print("Получено [" + requestFromClient + "] от " + incoming + ". ");
                    String[] parsedCommand = requestFromClient.trim().split(" ", 2);
                    if (parsedCommand.length == 1)
                        sendToClient.writeObject(availableCommandsWithDescription.getOrDefault(parsedCommand[0], errorCommand).execute());
                    else if (parsedCommand.length == 2)
                        sendToClient.writeObject(availableCommandsWithDescription.getOrDefault(parsedCommand[0], errorCommand).execute(parsedCommand[1]));
                    System.out.println("Ответ успешно отправлен.");
                } catch (SocketException e) {
                    System.out.println(incoming + " отключился от сервера."); //Windows
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(incoming + " отключился от сервера."); //Unix
        }
    }
}
