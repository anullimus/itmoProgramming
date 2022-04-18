package com.company.server.command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

/**
 * Prints all elements of the collection.
 */
public class ShowCommand implements Command<String> {
    private final LinkedHashSet<LabWork> collection;

    public ShowCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
    }

    @Override
    public String execute() {
        boolean success = MyValidator.checkCommandFormat();
        if (success) {
            System.out.println("---------------------------------------------");
            for (LabWork labWork : collection) {
                System.out.println(labWork);
                System.out.println();
            }
            return "---------------------------------------------";
        } else {
            return "Коллекция пуста.";
        }
    }
}
