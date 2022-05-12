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
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;
    private boolean isScriptExecuting;
    private String[] addDataFromScript;

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
//        ServerConnection serverConnection = new ServerConnection();
//        this.availableCommands = serverConnection.getAvailableCommands();
//        this.commandsNeedArgument = serverConnection.getCommandsNeedArgument();
        this.isScriptExecuting = false;
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

    public boolean isScriptExecuting() {
        return isScriptExecuting;
    }

    public Class<?> getArgumentClass(){
        return commandsNeedArgument.get(commandName);
    }

    public boolean analyzeCommand(String[] inputLineDivided, boolean isScriptExecuting) {
        this.isScriptExecuting = isScriptExecuting;
        commandName = inputLineDivided[0].toLowerCase();
        if (commandName.equals("technical")){
            return true;
        }
        if(commandName.equals("execute_script")){
            if(inputLineDivided.length == 1){
                System.err.println("Укажите путь к скрипту.");
            }else {
                commandHaveArgument = true;
                commandArgument = inputLineDivided[1];
                return true;
            }
        }
        if (!availableCommands.contains(commandName)) {
            return false;
        }
        if (commandsNeedArgument.containsKey(commandName) && inputLineDivided.length == 1 &&
                commandsNeedArgument.get(commandName) != LabWork.class) {
//            System.err.println("Аргумент не указан"); //defined in Abstract Command
            return false;
        }
        if (!commandsNeedArgument.containsKey(commandName) && inputLineDivided.length > 1) {
            System.err.println("Аргумент не должен быть указан");
            return false;
        }
        commandHaveArgument = false; // для execute_script, если не войдет в тело условки ниже, то значит false
        if (commandsNeedArgument.containsKey(commandName) && (inputLineDivided.length > 1
                || commandsNeedArgument.get(commandName) == LabWork.class)) {
            commandHaveArgument = true;
            if(commandsNeedArgument.get(commandName) != LabWork.class){
                commandArgument = inputLineDivided[1];
            }
        }
        return true;
    }
}
