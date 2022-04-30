package command;

import serverLogic.CollectionManager;
import serverLogic.CommandManager;

/**
 * Prints full information about the command that manages the collection.
 */
public class InfoCommand implements AbstractCommand<String> {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        StringBuilder collectionInfo = new StringBuilder(CommandManager.PS1);
        collectionInfo.append("Тип коллекции: ").append(collectionManager.getLabWorks().getClass()).
                append("\n Дата инициализации: ").append(collectionManager.getCreationDate()).
                append("\n Количество элементов: ").append(collectionManager.getSize());
        return collectionInfo.toString();
    }
}

