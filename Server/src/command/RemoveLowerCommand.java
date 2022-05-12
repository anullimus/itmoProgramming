package command;

import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет все элементы из коллекции, которые меньше заданного(по показателю минимального балла)");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        if (collection.size() != 0) {
            int beginSize = collection.size();
            collection.removeIf(p -> (p != null && p.compareTo(request.getLabWorkArgument()) < 0));
            getCollectionManager().save();
            return new Response("Удалено из коллекции " + (beginSize - collection.size()) + " элементов.");
        } else {
            return new Response("Элементу не с чем сравнивать. Коллекция пуста.");
        }
    }
}