package command;

import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;
import utility.Tool;


public class AddCommand extends AbstractCommand {

    public AddCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription(Tool.RELAX);
    }

    @Override
    public Response execute(Request request) {
        getCollectionManager().getLabWorks().add(request.getLabWorkArgument());
        return new Response("Элемент успешно добавлен.");
    }
}
