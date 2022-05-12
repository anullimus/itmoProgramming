package command;


//import serverLogic.MyValidator;

import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Tool;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;

public class AddIfMinCommand extends AbstractCommand {

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
            if (collection.stream().allMatch((collectionLabwork) -> Float.compare(inputLabwork.getMinimalPoint(),
                    collectionLabwork.getMinimalPoint()) < 0)) {
                inputLabwork.changeId();
                collection.add(inputLabwork);
                getCollectionManager().save();
                return new Response("Элемент успешно добавлен.");
        } else {
            return new Response("Ваш элеменет не минимальный.");
        }
    } else

    {
        return new Response("Элемент не с чем сравнивать. Коллекция пуста.");
    }
}
}
