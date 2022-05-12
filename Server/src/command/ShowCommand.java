package command;


import serverLogic.CollectionManager;
import utility.Response;


/**
 * Prints all elements of the collection.
 */
public class ShowCommand extends AbstractCommand {

    public ShowCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести все элементы коллекции.");
    }

    @Override
    public Response execute() {
        return new Response(getCollectionManager().getLabWorks());
    }
}
