package serverLogic;

import command.*;
import data.initial.Difficulty;
import data.initial.LabWork;
import db.DatabaseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private HashMap<String, AbstractCommand> availableCommandsWithDescription;
    private CollectionManager collectionManager;
    private DatabaseHandler databaseHandler;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
    public CommandManager(DatabaseHandler databaseHandler, CollectionManager collectionManager){
        this.collectionManager = collectionManager;
        this.databaseHandler = databaseHandler;
        availableCommands = new ArrayList<>();
        fillingSpecialCommandArrays();
    }
    private void fillingSpecialCommandArrays() {
        availableCommandsWithDescription = new HashMap<>();
        commandsNeedArgument = new HashMap<>();
        availableCommandsWithDescription.put("info", new InfoCommand(collectionManager));
        availableCommandsWithDescription.put("show", new ShowCommand(collectionManager));
        availableCommandsWithDescription.put("add", new AddCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("update_id", new UpdateIDCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("remove_by_id", new RemoveByIDCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("add_if_min", new AddIfMinCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("clear", new ClearCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("remove_greater", new RemoveGreaterCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("remove_lower", new RemoveLowerCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("max_by_author", new MaxByAuthorCommand(collectionManager));
        availableCommandsWithDescription.put("count_less_than_author", new CountLessThanAuthorCommand(collectionManager));
        availableCommandsWithDescription.put("filter_by_difficulty", new FilterByDifficultyCommand(collectionManager));
        availableCommandsWithDescription.put("help", new HelpCommand(collectionManager, availableCommandsWithDescription));
        availableCommandsWithDescription.put("connect_user", new ConnectUserCommand(databaseHandler, collectionManager));
        availableCommandsWithDescription.put("register_user", new RegisterUserCommand(databaseHandler, collectionManager));

        availableCommands.add("register_user");
        availableCommands.add("connect_user");
        availableCommands.addAll(availableCommandsWithDescription.keySet());

        commandsNeedArgument.put("add", LabWork.class);
        commandsNeedArgument.put("add_if_min", LabWork.class);
        commandsNeedArgument.put("update_id", Long.class);
        commandsNeedArgument.put("remove_by_id", Long.class);
        commandsNeedArgument.put("remove_greater", LabWork.class);
        commandsNeedArgument.put("remove_lower", LabWork.class);
        commandsNeedArgument.put("count_less_than_author", String.class);
        commandsNeedArgument.put("filter_by_difficulty", Difficulty.class);
        commandsNeedArgument.put("execute_script", File.class);
    }

    public HashMap<String, AbstractCommand> getAvailableCommandsWithDescription() {
        return availableCommandsWithDescription;
    }

    public AbstractCommand getCommandByName(String name){
        return availableCommandsWithDescription.get(name);
    }
    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }

}
