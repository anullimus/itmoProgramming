package command;

import data.initial.LabWork;
import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        super(databaseHandler, manager);
        setDescription("Удаляет все элементы из коллекции, которые меньше заданного(по показателю минимального балла)");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        int beginSize = collection.size();
        String clientName = request.getClientName();
        float minPoint = request.getFloatArgument();
        synchronized (getDatabaseHandler()) {
            if (getDatabaseHandler().deleteLabworksLowerThanMinPoint(clientName, minPoint)) {
                collection.removeIf(p -> (p != null && p.compareTo(request.getLabWork()) < 0));
                return new Response("Удалено из коллекции " + (beginSize - collection.size()) + " элементов.");
            }
            return new Response("Ошибка при удалении элементов из базы данных");
        }
    }
}