package command;

import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;


/**
 * Updates id of element from collection.
 */
public class UpdateIDCommand extends Command {

    public UpdateIDCommand(CollectionManager manager) {
        super(manager);
        setDescription("Обновить значение элемента коллекции, id которого равен заданному.");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        long id = request.getLongArgument();

        for (LabWork labWork : collection) {
            if (labWork.getId() == id) {
                labWork.changeId();
                getCollectionManager().save();
                return new Response("Элемент с id = " + id + " успешно обновлен.");
            }
        }
        return new Response("Here is no lab work find with id=" + id);
    }
}
