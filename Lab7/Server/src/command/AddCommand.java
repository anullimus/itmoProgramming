package command;

import data.initial.LabWork;
import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;
import utility.Tool;


public class AddCommand extends AbstractCommand {

    public AddCommand(DatabaseHandler databaseHandler, CollectionManager collectionManager) {
        super(databaseHandler, collectionManager);
        setDescription(Tool.RELAX);
    }

    @Override
    public Response execute(Request request) {
        LabWork labWork = request.getLabWork();
        synchronized (getDatabaseHandler()) {
            long nextId = getDatabaseHandler().addLabworkToDB(labWork);
            if (nextId != -1) {
                labWork.setId(nextId);
                getCollectionManager().getLabWorks().add(labWork);
                return new Response("Элемент успешно добавлен");
            }
            return new Response("Ошибка при добавлении элемента, возможно, такой элемент уже существует");
        }
    }
}
