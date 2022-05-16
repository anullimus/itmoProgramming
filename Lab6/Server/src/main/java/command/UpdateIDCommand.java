package command;

import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;


/**
 * Updates id of element from collection.
 */
public class UpdateIDCommand extends AbstractCommand {

    public UpdateIDCommand(CollectionManager manager) {
        super(manager);
        setDescription("Обновить значение элемента коллекции, id которого равен заданному.");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        long id = request.getLongArgument();
        try {
            LabWork labWork = collection.stream().filter(collectionLabwork -> collectionLabwork.getId().equals(id))
                    .findAny().orElseThrow(NoSuchElementException::new);
            labWork.changeId();
//            getCollectionManager().save();
            return new Response("Элемент с id = " + id + " успешно обновлен.");
        }catch (NoSuchElementException noSuchElementException) {
            return new Response("Here is no lab work find with id=" + id);
        }
    }
}
