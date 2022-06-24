package utility;

import data.initial.LabWork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class CommandAnalyzer implements Serializable {
    private String commandArgument;
    private String commandName;
    private boolean commandHaveArgument;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
    private boolean ScriptExecuting;
    private String[] addDataFromScript;
    private boolean DBCommand;

    //    public CommandAnalyzer(String[] parsedCommand, boolean ScriptExecuting) {
//        commandHaveArgument = false;
//        this.ScriptExecuting = ScriptExecuting;
//        DBCommand = false;
//
//        analyzeCommand(parsedCommand, ScriptExecuting);
//    }
    public boolean isDBCommand() {
        return DBCommand;
    }

    public void setAvailableCommands(ArrayList<String> availableCommands) {
        this.availableCommands = availableCommands;
    }

    public void setCommandsNeedArgument(Map<String, Class<?>> commandsNeedArgument) {
        this.commandsNeedArgument = commandsNeedArgument;
    }

    public void setAddDataFromScript(String[] addDataFromScript) {
        this.addDataFromScript = addDataFromScript;
    }

    public String[] getAddDataFromScript() {
        return addDataFromScript;
    }

    public CommandAnalyzer() {
        this.ScriptExecuting = false;
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandArgumentString() {
        return commandArgument;
    }

    public boolean isScriptExecuting() {
        return ScriptExecuting;
    }

    public Class<?> getArgumentClass() {
        return commandsNeedArgument.get(commandName);
    }

    public boolean analyzeCommand(String[] parsedCommand, boolean ScriptExecuting) {
        this.ScriptExecuting = ScriptExecuting;
        commandName = parsedCommand[0].toLowerCase();
        if (commandName.equals("technical")) {
            return true;
        }
        DBCommand = "connect_user".equals(commandName) || "register_user".equals(commandName);
        if (commandName.equals("execute_script")) {
            if (parsedCommand.length == 1) {
                System.err.println("Укажите путь к скрипту.");
            } else {
                commandHaveArgument = true;
                commandArgument = parsedCommand[1];
                return true;
            }
        }
        if (!availableCommands.contains(commandName)) {
            return false;
        }
        if (commandsNeedArgument.containsKey(commandName) && parsedCommand.length == 1 &&
                commandsNeedArgument.get(commandName) != LabWork.class) {
//            System.err.println("Аргумент не указан"); //defined in Abstract Command
            return false;
        }
        if (!commandsNeedArgument.containsKey(commandName) && parsedCommand.length > 1) {
            System.err.println("Аргумент не должен быть указан");
            return false;
        }
        commandHaveArgument = false; // для execute_script, если не войдет в тело условки ниже, то значит false
        if (commandsNeedArgument.containsKey(commandName) && (parsedCommand.length > 1
                || commandsNeedArgument.get(commandName) == LabWork.class)) {
            commandHaveArgument = true;
            if (commandsNeedArgument.get(commandName) != LabWork.class) {
                commandArgument = parsedCommand[1];
            }
        }
        return true;
    }
}
