package command;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;

public class RemoveByIDCommand extends Command {
    public RemoveByIDCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет элемент из коллекции по его ID.");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        long id = request.getLongArgument();
        if (collection.size() != 0) {
            for (LabWork lw : collection) {
                if (lw.getId() == id) {
                    collection.remove(lw);
                    getCollectionManager().save();
                    return new Response("Элемент успешно удален.");
                }
            }
            return new Response("Такого элемента нет в коллекции.");
        } else {
            return new Response("Элемент не с чем сравнивать. Коллекция пуста.");
        }
    }
}