package command;

import data.initial.LabWork;

import serverLogic.CollectionManager;

import java.util.LinkedHashSet;

/**
 * Prints all elements of the collection.
 */
public class ShowCommand extends Command {

    public ShowCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести все элементы коллекции.");
    }

    @Override
    public String execute() {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        StringBuilder result = new StringBuilder();
        if (collection.size() != 0) {
            result.append("---------------------------------------------\n");
            for (LabWork labWork : collection) {
                result.append(getCollectionManager().getSerializer().toJson(labWork)).append("\n\n");
            }result.append("---------------------------------------------");
            return result.toString();
        } else {
            return "Коллекция пуста.";
        }
    }
}
