package com.company.server.command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class MaxByAuthorCommand implements Command<String> {
    private final LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public MaxByAuthorCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();
    }

    @Override
    public String execute() {
        String stringResultOfValidation = "Ответ не пришел :(";
        boolean success = myValidator.checMaxByAuthorCommand(collection);
        if (success) {
            stringResultOfValidation = myValidator.getStringResultOfValidation();
        } else {
            System.out.println("Ошибка при обработке.");
        }
        return stringResultOfValidation;
    }
}
