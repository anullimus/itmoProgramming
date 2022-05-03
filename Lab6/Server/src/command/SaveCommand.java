package command;

import serverLogic.CollectionManager;
import serverLogic.FileManager;

public class SaveCommand extends AbstractCommand {
    private final FileManager fileManager;

    public SaveCommand(CollectionManager collectionManager, FileManager fileManager) {
        super(collectionManager);
        this.fileManager = fileManager;
    }

    @Override
    public String execute() {
//        fileManager.save(getCollectionManager().getLabWorks());
        getCollectionManager().save();
        return "Коллекция сохранена успешно.";
    }
}
