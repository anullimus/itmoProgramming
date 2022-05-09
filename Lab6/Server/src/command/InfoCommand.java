package command;

import serverLogic.CollectionManager;
import serverLogic.Tool;

/**
 * Prints full information about the command that manages the collection.
 */
public class InfoCommand extends Command {

    public InfoCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести в стандартный поток вывода информацию " +
                "о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }
    @Override
    public String execute(String arg) {
        return execute();
    }

    @Override
    public String execute() {
        StringBuilder collectionInfo = new StringBuilder(Tool.PS1);
        collectionInfo.append("Тип коллекции: ").append(getCollectionManager().getLabWorks().getClass()).
                append("\n Дата инициализации: ").append(getCollectionManager().getCreationDate()).
                append("\n Количество элементов: ").append(getCollectionManager().getSize());
        return collectionInfo.toString();
    }
}

