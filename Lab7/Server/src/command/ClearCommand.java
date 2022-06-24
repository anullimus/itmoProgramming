package command;


import data.initial.LabWork;
import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class ClearCommand extends AbstractCommand {
    public ClearCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        super(databaseHandler, manager);
        setDescription("Очистить коллекцию.");
    }

    @Override
    public Response execute(Request request) {
        String clientName = request.getClientName();
        synchronized (getDatabaseHandler()) {
            if (getDatabaseHandler().deleteClientLabworks(clientName)) {

                LinkedHashSet<LabWork> labWorks = getCollectionManager().getLabWorks();
                Collection<LabWork> collection = labWorks.stream().filter(lw -> lw.getName().equals(clientName)).collect(Collectors.toList());
                collection.forEach(labWorks::remove);
                return new Response("Из коллекции удалены все ваши объекты");
            }
            return new Response("Ошибка при удалении из базы данных");
        }
    }
}
