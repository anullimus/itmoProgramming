package command;

import data.initial.LabWork;
import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;

public class RemoveByIDCommand extends AbstractCommand {
    public RemoveByIDCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        super(databaseHandler, manager);
        setDescription("Удаляет элемент из коллекции по его ID.");
    }

@Override
public Response execute(Request request) {
    LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
    Response responseToReturn;
    long id = request.getLongArgument();
    try {
        synchronized (getDatabaseHandler()) {
            LabWork labWork = collection.stream().filter(lw -> lw.getId().equals(id)).findAny()
                    .orElseThrow(NoSuchElementException::new);
            if (!labWork.getName().equals(request.getClientName())) {
                responseToReturn = new Response("Вы не владелец данного элемента, поэтому вы не можете удалить его из коллекции");
            } else if (getDatabaseHandler().deleteLabworkByID(id)) {
                collection.remove(labWork);
                responseToReturn = new Response("Элемент с id = " + id + " удален из коллекции");
            } else {
                responseToReturn = new Response("Ошибка при удалении из базы данных");
            }
        }
    } catch (NoSuchElementException e) {
        responseToReturn = new Response("Элемента с id = " + id + " не существует");
    }
    return responseToReturn;
}
}