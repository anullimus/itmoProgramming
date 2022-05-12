package command;

import serverLogic.CollectionManager;
import utility.Tool;
import utility.Request;
import utility.Response;


public class AddCommand extends AbstractCommand {

    public AddCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription(Tool.RELAX);
    }

    @Override
    public Response execute(Request request) {
        getCollectionManager().getLabWorks().add(request.getLabWorkArgument());
        getCollectionManager().save();
        return new Response("Элемент успешно добавлен.");
    }
}
