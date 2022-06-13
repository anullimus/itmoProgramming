package serverLogic;

import command.*;
import data.initial.Difficulty;
import data.initial.LabWork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private HashMap<String, AbstractCommand> availableCommandsWithDescription;
    private final CollectionManager collectionManager;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
    public CommandManager(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
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

    public HashMap<String, AbstractCommand> getAvailableCommandsWithDescription() {
        return availableCommandsWithDescription;
    }

    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }
    public AbstractCommand getCommandByName(String name) {
        return availableCommandsWithDescription.get(name);
    }
}
