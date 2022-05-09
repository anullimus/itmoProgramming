package command;

import serverLogic.CollectionManager;
import serverLogic.FileManager;

public class SaveCommand extends Command {

    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        super(collectionManager);
    }

    @Override
    public String execute() {
        getCollectionManager().save();
        return "Коллекция сохранена успешно.";
    }
}
