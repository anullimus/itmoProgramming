package command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;


public class FilterByDifficultyCommand implements AbstractCommand<String> {
    private final LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public FilterByDifficultyCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();
    }

    @Override
    public String execute() {
        String stringResultOfValidation = "Ответ не пришел :(";
        boolean success = myValidator.checkFilterByDifficultyCommand(collection);
        if (success) {
            stringResultOfValidation = myValidator.getStringResultOfValidation();
        } else {
            System.out.println("Ошибка при обработке.");
        }
        return stringResultOfValidation;
    }
}