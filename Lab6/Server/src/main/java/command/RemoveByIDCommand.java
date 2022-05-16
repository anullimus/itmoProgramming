package command;

import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;

public class RemoveByIDCommand extends AbstractCommand {
    public RemoveByIDCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет элемент из коллекции по его ID.");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        long id = request.getLongArgument();
        try {
            collection.remove(collection.stream().filter(labWork -> labWork.getId().equals(id)).findAny()
                    .orElseThrow(NoSuchElementException::new));
//            getCollectionManager().save();
            return new Response("Элемент с id = " + id + " удален из коллекции");
        } catch (NoSuchElementException e) {
            return new Response("Элемента с id = " + id + " не существует.");
        }
    }
}