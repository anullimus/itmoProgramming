package command;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;

import java.util.LinkedHashSet;

public class RemoveByIDCommand extends AbstractCommand {
    public RemoveByIDCommand(CollectionManager manager) {
        super(manager);
        setDescription("Удаляет элемент из коллекции по его ID.");
    }

    @Override
    public String execute(String arg) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        long id = Long.parseLong(arg);

        if (collection.size() != 0) {
            try {
                for (LabWork lw : collection) {
                    if (lw.getId() == id) {
                        collection.remove(lw);
                        getCollectionManager().save();
                        return "Элемент успешно удален.";
                    }
                }
                return "Такого элемента нет в коллекции.";
            } catch (JsonSyntaxException ex) {
                return "Синтаксическая ошибка JSON. Не удалось удалить элемент.";
            }
        } else return "Элемент не с чем сравнивать. Коллекция пуста.";
    }
}