package command;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;

import java.util.LinkedHashSet;

public class RemoveGreaterCommand extends AbstractCommand {
    public RemoveGreaterCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет все элементы из коллекции, которые превышают заданный(по показателю минимального балла)");
    }

    @Override
    public String execute(String arg) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        if (collection.size() != 0) {
            int beginSize = collection.size();
            try {
                collection.removeIf(p -> (p != null && p.compareTo(getCollectionManager().getSerializer()
                        .fromJson(arg, LabWork.class)) > 0));
                getCollectionManager().save();
                return "Удалено из коллекции " + (beginSize - collection.size()) + " элементов.";
            } catch (JsonSyntaxException ex) {
                return "Синтаксическая ошибка JSON. Не удалось удалить элемент.";
            }
        } else return "Элементу не с чем сравнивать. Коллекция пуста.";
    }
}
