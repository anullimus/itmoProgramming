package command;


import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Response;

import java.util.LinkedHashSet;


public class MaxByAuthorCommand extends Command {

    public MaxByAuthorCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести любой объект из коллекции, значение " +
                "поля author которого является максимальным.");
    }

    @Override
    public Response execute() {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        LabWork[] arr = new LabWork[collection.size()];
        arr = collection.toArray(arr);
        LabWork lwWithOldestAuthor = arr[0];
        for (LabWork lw : collection) {
            if (lwWithOldestAuthor.getAuthor().getBirthday().compareTo(lw.getAuthor().getBirthday()) > 0) {
                lwWithOldestAuthor = lw;
            }
        }
        return new Response("Лабораторная работа, написанная самым старым автором: "
                + lwWithOldestAuthor);
    }
}
