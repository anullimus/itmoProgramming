package command;

import serverLogic.CollectionManager;
import serverLogic.FileManager;
import utility.Response;

public class SaveCommand extends AbstractCommand {

    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        super(collectionManager);
    }

    @Override
    public Response execute() {
        getCollectionManager().save();
        return new Response("Коллекция сохранена успешно.");
    }
}
