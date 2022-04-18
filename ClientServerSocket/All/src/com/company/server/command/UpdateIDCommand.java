package com.company.server.command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;


/**
 * Updates id of element from collection.
 */
public class UpdateIDCommand implements Command<LinkedHashSet<LabWork>> {
    private LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public UpdateIDCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        boolean success = myValidator.checkUpdateIDCommand(collection);
        if (success) {
            collection = myValidator.getCollection();
        } else {
            System.out.println("Ошибка при обновлении ID. Перепроверьте корректность введенных данных.");
        }
        return collection;
    }
}
