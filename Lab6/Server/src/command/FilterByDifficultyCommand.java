package command;

import data.initial.Difficulty;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.Tool;

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
    public String execute(String arg) {
        try {
            boolean wasAtLeastOneThisTypeOfElementInCollection = false;
            LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
            StringBuilder result = new StringBuilder();
            for (LabWork lw : collection) {
                if (lw.getDifficulty().equals(Difficulty.valueOf(arg.toUpperCase(Locale.ROOT)))) {
                    result.append(lw).append("\n\n");
                    wasAtLeastOneThisTypeOfElementInCollection = true;
                }
            }
            if (wasAtLeastOneThisTypeOfElementInCollection) {
                result.append(Tool.PS1).append("Выведены все лаб. работы типа ");
                System.out.println(result);
                return result.toString();
            } else {
                return "В коллекции не нашлось ни одного элемента с заданным типом";
            }
        } catch (NullPointerException | IllegalArgumentException exception) {
            return "Передан некорректный аргумент.";
        }
    }
}
