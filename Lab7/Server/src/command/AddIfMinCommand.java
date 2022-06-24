package command;


//import serverLogic.MyValidator;

import data.initial.LabWork;
import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;
import utility.Tool;

import java.util.LinkedHashSet;

public class AddIfMinCommand extends AbstractCommand {

    public AddIfMinCommand(DatabaseHandler databaseHandler, CollectionManager collectionManager) {
        super(databaseHandler, collectionManager);
        setDescription("Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего " +
                "элемента этой коллекции. " + Tool.RELAX);
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        LabWork labWork = request.getLabWork();
        if (collection.stream().allMatch((collectionRoute) -> Double.compare(labWork.getMinimalPoint(),
                collectionRoute.getMinimalPoint()) < 0)) {
            synchronized (getDatabaseHandler()) {
                long nextId = getDatabaseHandler().addLabworkToDB(labWork);
                if (nextId != -1) {
                    labWork.setId(nextId);
                    collection.add(labWork);
                    return new Response("Элемент успешно добавлен");
                } else {
                    return new Response("Ошибка при добавлении в базу данных");
                }
            }
        }
        return new Response("Новый путь не меньше минимального");
    }
}
