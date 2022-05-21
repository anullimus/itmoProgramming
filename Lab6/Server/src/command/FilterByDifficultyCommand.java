package command;

import data.initial.Difficulty;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class FilterByDifficultyCommand extends AbstractCommand {

    public FilterByDifficultyCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести все элементы коллекции, значение поля difficulty которых " +
                "равно заданному. Доступные типы: " + Arrays.toString(Difficulty.values()));
    }

    @Override
    public Response execute(Request request) {
        Difficulty difficulty = request.getDifficultyArgument();
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        return new Response(collection.stream().
                filter(labwork -> labwork.getDifficulty().equals(difficulty)).
                collect(Collectors.toCollection(LinkedHashSet::new)).toString());
    }
}
