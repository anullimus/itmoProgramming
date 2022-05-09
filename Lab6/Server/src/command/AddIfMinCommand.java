package command;


//import serverLogic.MyValidator;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.Tool;

import java.util.Collections;
import java.util.LinkedHashSet;

public class AddIfMinCommand extends Command {

    public AddIfMinCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription("Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего " +
                "элемента этой коллекции. " + Tool.RELAX);
    }

    @Override
    public String execute(String arg) {
        LabWork inputLabwork;
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        if (collection.size() != 0) {
            try {
                inputLabwork = getCollectionManager().getSerializer().fromJson(arg, LabWork.class);
                if (Collections.min(collection).compareTo(inputLabwork) > 0) {
                    collection.add(inputLabwork);
                    getCollectionManager().save();
                    return "Элемент успешно добавлен.";
                } else return "Не удалось добавить элемент.";
            } catch (JsonSyntaxException e) {
                return "Ошибка в синтаксисе JSON. Не удалось добавить элемент.";
            }
        } else return "Элемент не с чем сравнивать. Коллекция пуста.";
    }
}
