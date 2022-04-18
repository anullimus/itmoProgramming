package com.company.client;

import com.company.server.CommandInformer;

public class CommandAnalyzer {
    private String commandArgument;
    private boolean commandHaveArgument;

    public CommandAnalyzer() {
        commandHaveArgument = false;
    }


    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }

    public String getCommandArgument() {
        return commandArgument;
    }

    public boolean analyzeCommand(String[] command) {
        String commandName = command[0].toLowerCase();

        if (!CommandInformer.getAvailableCommands().contains(commandName)) {
            throw new IllegalStateException("Такой команды не существует");
        }
        if (CommandInformer.getCommandsNeedArgument().containsKey(commandName) && command.length == 1) {
            throw new IllegalStateException("Аргумент не указан");
        }
        if (!CommandInformer.getCommandsNeedArgument().containsKey(commandName) && command.length > 1) {
            throw new IllegalStateException("Аргумент не должен быть указан");
        }

        if ("execute_script".equals(commandName)) {
            App.IS_SCRIPT = true;
        }
        if (CommandInformer.getCommandsNeedArgument().containsKey(commandName)) {
            commandHaveArgument = true;
            commandArgument = command[1];
        }
        return true;
    }
}
