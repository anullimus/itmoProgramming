package command;


import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Response;

import java.util.stream.Collectors;


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
        return new Response(getCollectionManager().getLabWorks().stream().map(LabWork::toString)
                .collect(Collectors.joining("\n")));
    }
}
