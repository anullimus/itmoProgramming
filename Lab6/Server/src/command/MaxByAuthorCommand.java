package command;


import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Response;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;


public class MaxByAuthorCommand extends Command {

    public MaxByAuthorCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести любой объект из коллекции, значение " +
                "поля author которого является максимальным.");
    }

    @Override
    public Response execute() {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        try {
            LabWork lwWithOldestAuthor = collection.stream().max(Comparator.comparing(LabWork::getAuthor)).
                    orElseThrow(NoSuchElementException::new);
            return new Response("Лабораторная работа, написанная самым старым автором:\n"
                    + lwWithOldestAuthor);
        } catch (NoSuchElementException noSuchElementException) {
            return new Response("Коллекция пуста.");
        }
    }
}
