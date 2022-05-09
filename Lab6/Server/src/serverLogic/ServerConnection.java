package serverLogic;

import data.initial.Difficulty;
import data.initial.LabWork;
import data.initial.Person;
import utility.Request;
import command.*;

import java.io.File;
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
public class ServerConnection {
    private HashMap<String, Command> availableCommandsWithDescription;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
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
     *                         Команды, доступные клиенту, являются объектами {@link Command}, хранящимися в
     *                         {@code HashMap <String, AbstractCommand> availableCommands}.
     */
    public ServerConnection(CollectionManager serverCollection, Socket incoming) {
        this.incoming = incoming;
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
        availableCommandsWithDescription.put("execute_script", new ExecuteScriptCommand(serverCollection, availableCommandsWithDescription));
        availableCommandsWithDescription.put("help", new HelpCommand(serverCollection, availableCommandsWithDescription));

        commandsNeedArgument.put("update_id", Long.class);
        commandsNeedArgument.put("remove_by_id", Long.class);
        commandsNeedArgument.put("remove_greater", LabWork.class);
        commandsNeedArgument.put("remove_lower", LabWork.class);
        commandsNeedArgument.put("count_less_than_author", Person.class);
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
        try (ObjectInputStream getFromClient = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream sendToClient = new ObjectOutputStream(incoming.getOutputStream())) {
            sendToClient.writeObject("Соединение установлено! Вы можете вводить команды!" +
                    "\nP.s. если не знаете, что вводить, просто введите команду 'help'");
            Command errorCommand = new Command(null) {
                @Override
                public String execute() {
                    return "Неизвестная команда. Введите 'help' для получения списка команд.";
                }
            };
            while (true) {
                try {
                    Request requestFromClient = (Request) getFromClient.readObject();
                    System.out.print("Получено [" + requestFromClient + "] от " + incoming + ". ");

                    if (requestFromClient.isCommandHaveArgument())
                        if (requestFromClient.getCommandName().equals("execute_script")) {
                            new ExecuteScriptCommand(serverCollection, availableCommandsWithDescription)
                                    .execute(requestFromClient.getCommandArgument());
                        } else {
                            sendToClient.writeObject(availableCommandsWithDescription.
                                    getOrDefault(requestFromClient.getCommandName(), errorCommand).
                                    execute(requestFromClient.getCommandArgument()));
                        }
                    else {
                        sendToClient.writeObject(availableCommandsWithDescription.
                                getOrDefault(requestFromClient.getCommandName(), errorCommand).execute());
                    }
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
