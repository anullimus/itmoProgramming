package command;

import data.initial.LabWork;
import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;


/**
 * Updates id of element from collection.
 */
public class UpdateIDCommand extends AbstractCommand {

    public UpdateIDCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        super(databaseHandler, manager);
        setDescription("Обновить значение элемента коллекции, id которого равен заданному.");
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
                responseToReturn = new Response("Вы не владелец данного элемента, поэтому вы не можете обновить его в коллекции");
            } else if (getDatabaseHandler().updateLabworkByID(labWork)) {
                labWork.changeId();
                responseToReturn = new Response("Элемент с id = " + id + " успешно обновлен.");

            } else {
                responseToReturn = new Response("Ошибка при обновлении в базе данных");
            }
        }
    } catch (NoSuchElementException e) {
        responseToReturn = new Response("Элемента с id = " + id + " не существует");
    }
    return responseToReturn;
}
}
