package clientLogic;

import serverLogic.ServerConnection;

import java.util.ArrayList;

public class CommandAnalyzer {
//    private String commandArgument;
//    private boolean commandHaveArgument;
    private final ArrayList<String> availableCommands;
    private final ArrayList<String> commandsNeedArgument;

    public CommandAnalyzer() {
        ServerConnection serverConnection = new ServerConnection();
        this.availableCommands = serverConnection.getAvailableCommands();
        this.commandsNeedArgument = serverConnection.getCommandsNeedArgument();
    }
//
//    public boolean isCommandHaveArgument() {
//        return commandHaveArgument;
//    }
//
//    public String getCommandName(){
//        return commandName;
//    }

//    public String getCommandArgument() {
//        return commandArgument;
//    }


    public boolean analyzeCommand(String command) {
        String[] inputLineDivided = command.trim().split(" ", 2);
        String commandName = inputLineDivided[0].toLowerCase();

        if (!availableCommands.contains(commandName)) {
            System.out.println("Такой команды не существует");
        }
        if (commandsNeedArgument.contains(commandName) && inputLineDivided.length == 1) {
            System.out.println("Аргумент не указан");
        }
        if (!commandsNeedArgument.contains(commandName) && inputLineDivided.length > 1) {
            System.out.println("Аргумент не должен быть указан");
        }
        return true;
    }
}
