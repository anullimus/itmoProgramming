package command;


import serverLogic.CollectionManager;
import utility.Response;

public class ClearCommand extends AbstractCommand {
    public ClearCommand(CollectionManager manager) {
        super(manager);
        setDescription("Очистить коллекцию.");
    }

    @Override
    public Response execute() {
        getCollectionManager().getLabWorks().clear();
        return new Response("Коллекция очищена.");
    }
}
