package command;


//import serverLogic.MyValidator;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.Tool;
import utility.Request;
import utility.Response;

import java.util.Collections;
import java.util.LinkedHashSet;

public class AddIfMinCommand extends Command {

    public AddIfMinCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription("Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего " +
                "элемента этой коллекции. " + Tool.RELAX);
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        LabWork inputLabwork = request.getLabWorkArgument();
        if (collection.size() != 0) {
            if (Collections.min(collection).compareTo(inputLabwork) > 0) {
                collection.add(inputLabwork);
                getCollectionManager().save();
                return new Response("Элемент успешно добавлен.");
            } else {
                return new Response("Ваш элеменет не минимальный.");
            }
        } else {
            return new Response("Элемент не с чем сравнивать. Коллекция пуста.");
        }
    }
}
