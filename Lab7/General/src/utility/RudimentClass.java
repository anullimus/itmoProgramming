package utility;

import data.initial.Difficulty;
import data.initial.LabWork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RudimentClass {

    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;

    public RudimentClass() {
        fillingSpecialCommandArrays();
    }

    private void fillingSpecialCommandArrays() {

        commandsNeedArgument = new HashMap<>();
        availableCommands.add("info");
        availableCommands.add("show");
        availableCommands.add("add");
        availableCommands.add("update_id");
        availableCommands.add("remove_by_id");
        availableCommands.add("add_if_min");
        availableCommands.add("remove_greater");
        availableCommands.add("remove_lower");
        availableCommands.add("max_by_author");
        availableCommands.add("count_less_than_author");
        availableCommands.add("filter_by_difficulty");
        availableCommands.add("help");

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

    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }
    public long getMaxId(){
        return 123123L;
    }
}
