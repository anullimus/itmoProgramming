package command;

import data.initial.Difficulty;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.Tool;
import utility.Request;
import utility.Response;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;

public class FilterByDifficultyCommand extends Command {

    public FilterByDifficultyCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести все элементы коллекции, значение поля difficulty которых " +
                "равно заданному. Доступные типы: " + Arrays.toString(Difficulty.values()));
    }

    @Override
    public Response execute(Request request) {
        boolean wasAtLeastOneThisTypeOfElementInCollection = false;
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        StringBuilder result = new StringBuilder();
        for (LabWork lw : collection) {
            if (lw.getDifficulty().equals(request.getDifficultyArgument())) {
                result.append(lw).append("\n\n");
                wasAtLeastOneThisTypeOfElementInCollection = true;
            }
        }
        if (wasAtLeastOneThisTypeOfElementInCollection) {
            result.append(Tool.PS1).append("Выведены все лаб. работы типа ");
            System.out.println(result);
            return new Response(result.toString());
        } else {
            return new Response("В коллекции не нашлось ни одного элемента с заданным типом");
        }
    }
}
