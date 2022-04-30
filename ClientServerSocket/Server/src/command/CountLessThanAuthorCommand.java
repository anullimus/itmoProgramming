package command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class CountLessThanAuthorCommand implements AbstractCommand<String> {
    private final LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public CountLessThanAuthorCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();
    }

    @Override
    public String execute() {
        String stringResultOfValidation = "Ответ не пришел :(";
        boolean success = myValidator.checkLessThanAuthorCommand(collection);
        if (success) {
            stringResultOfValidation = myValidator.getStringResultOfValidation();
        } else {
            System.out.println("Ошибка при обработке.");
        }
        return stringResultOfValidation;
    }
}
