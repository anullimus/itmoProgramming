package command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class RemoveLowerCommand implements AbstractCommand<LinkedHashSet<LabWork>> {
    private LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public RemoveLowerCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();

    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        boolean success = myValidator.checkRemoveLowerCommand(collection);
        if (success) {
            collection = myValidator.getCollection();
        } else {
            System.out.println("Ошибка при удалении элемента, возможно, такой элемента не существует.");
        }
        return collection;
    }
}