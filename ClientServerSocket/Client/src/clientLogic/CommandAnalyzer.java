package clientLogic;

import serverLogic.CommandManager;

public class CommandAnalyzer {
    private String commandName;
    private String commandArgument;
    private boolean commandHaveArgument;

    public CommandAnalyzer(String command) {
        commandHaveArgument = false;

        analyzeCommand(command);
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }

    public String getCommandName(){
        return commandName;
    }

    public String getCommandArgument() {
        return commandArgument;
    }

    public Class<?> getArgumentClass(){
        return CommandManager.getCommandsNeedArgument().get(commandName);
    }


    public boolean analyzeCommand(String command) {
        String[] inputLineDivided = command.trim().split(" ", 2);
        commandName = inputLineDivided[0].toLowerCase();

        if (!CommandManager.getAvailableCommands().contains(commandName)) {
            throw new IllegalStateException("Такой команды не существует");
        }
        if (CommandManager.getCommandsNeedArgument().containsKey(commandName) && inputLineDivided.length == 1) {
            throw new IllegalStateException("Аргумент не указан");
        }
        if (!CommandManager.getCommandsNeedArgument().containsKey(commandName) && inputLineDivided.length > 1) {
            throw new IllegalStateException("Аргумент не должен быть указан");
        }
        if (CommandManager.getCommandsNeedArgument().containsKey(commandName)) {
            commandHaveArgument = true;
            commandArgument = inputLineDivided[1];
        }
        return true;
    }
}
