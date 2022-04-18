package com.company.server.command;

import com.company.server.CommandInformer;

public class ExitCommand implements Command<String> {

    @Override
    public String execute() {
        System.out.println(CommandInformer.PS1 + "До скорых встреч!");
        System.exit(0);
        return "ня, пока.";
    }
}
