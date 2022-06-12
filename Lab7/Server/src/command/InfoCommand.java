package command;

import serverLogic.CollectionManager;
import utility.Response;
import utility.Tool;

/**
 * Prints full information about the command that manages the collection.
 */
public class InfoCommand extends AbstractCommand {

    public InfoCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести в стандартный поток вывода информацию " +
                "о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }
    @Override
    public Response execute() {
        StringBuilder collectionInfo = new StringBuilder(Tool.PS1);
        collectionInfo.append("Тип коллекции: ").append(getCollectionManager().getLabWorks().getClass()).
                append("\n Дата инициализации: ").append(getCollectionManager().getCreationDate()).
                append("\n Количество элементов: ").append(getCollectionManager().getSize());
        return new Response(collectionInfo.toString());
    }
}

