package command;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.Tool;
import utility.Request;
import utility.Response;


public class AddCommand extends Command {

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
