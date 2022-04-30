package command;

import data.initial.LabWork;

import clientLogic.MyValidator;

import java.util.LinkedHashSet;

/**
 * Prints all elements of the collection.
 */
public class ShowCommand implements AbstractCommand<String> {
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
