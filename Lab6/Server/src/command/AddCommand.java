package command;

import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.Tool;


public class AddCommand extends Command {

    public AddCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription(Tool.RELAX);
    }

    @Override
    public String execute(String arg) {
        LabWork inputLabwork;
        try {
            inputLabwork = getCollectionManager().getSerializer().fromJson(arg, LabWork.class);
            getCollectionManager().getLabWorks().add(inputLabwork);
            getCollectionManager().save();
            return "Элемент успешно добавлен.";
        } catch (JsonSyntaxException ex) {
            return "Ошибка в синтаксисе JSON. Не удалось добавить элемент.";
        }
    }
}
