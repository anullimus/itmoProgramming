package utility;

import data.initial.LabWork;
import serverLogic.ServerConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class CommandAnalyzer implements Serializable {
    private String commandArgument;
    private  String commandName;
    private boolean commandHaveArgument;
    private final ArrayList<String> availableCommands;
    private final Map<String, Class<?>> commandsNeedArgument;
    private boolean isSriptExecuting;

    public CommandAnalyzer() {
        ServerConnection serverConnection = new ServerConnection();
        this.availableCommands = serverConnection.getAvailableCommands();
        this.commandsNeedArgument = serverConnection.getCommandsNeedArgument();
        this.isSriptExecuting = false;
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }

    public String getCommandName(){
        return commandName;
    }

    public String getCommandArgumentString() {
        return commandArgument;
    }

    public boolean isSriptExecuting() {
        return isSriptExecuting;
    }

    public Class<?> getArgumentClass(){
        return commandsNeedArgument.get(commandName);
    }

    public boolean analyzeCommand(String[] inputLineDivided, boolean isScriptExecuting) {
        this.isSriptExecuting = isScriptExecuting;
        commandName = inputLineDivided[0].toLowerCase();

        if (!availableCommands.contains(commandName)) {
            return false;
        }
        if (commandsNeedArgument.containsKey(commandName) && inputLineDivided.length == 1 &&
                commandsNeedArgument.get(commandName) != LabWork.class) {
            System.err.println("Аргумент не указан");
            return false;
        }
        if (!commandsNeedArgument.containsKey(commandName) && inputLineDivided.length > 1) {
            System.err.println("Аргумент не должен быть указан");
            return false;
        }
        commandHaveArgument = false; // для execute_script, если не войдет в тело условки ниже, то значит false
        if (commandsNeedArgument.containsKey(commandName) && inputLineDivided.length > 1) {
            commandHaveArgument = true;
            commandArgument = inputLineDivided[1];
        }
        return true;
    }
}
