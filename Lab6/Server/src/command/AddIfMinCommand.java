package command;


//import serverLogic.MyValidator;
import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;

import java.util.Collections;
import java.util.LinkedHashSet;

public class AddIfMinCommand extends AbstractCommand {
//    private final MyValidator myValidator;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription("Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего " +
                "элемента этой коллекции. " + "!!!!!!!!!!!должен быть релакс месседж, тк элементы будут добавляться поочередно!!!");
//        myValidator = new MyValidator();
    }

    @Override
    public String execute(String arg) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        if (collection.size() != 0) {
            try {
                LabWork inputLabwork = getCollectionManager().getSerializer().fromJson(arg, LabWork.class);
                if (Collections.min(collection).compareTo(inputLabwork) > 0) {
                    collection.add(inputLabwork);
                    getCollectionManager().save();
                    return "Элемент успешно добавлен.";
                } else return "Не удалось добавить элемент.";
            } catch (JsonSyntaxException e) {
                return "Синтаксическая ошибка JSON. Не удалось добавить элемент.";
            }
        } else return "Элемент не с чем сравнивать. Коллекция пуста.";

    }
}
