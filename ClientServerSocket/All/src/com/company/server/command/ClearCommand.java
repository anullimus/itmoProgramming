package com.company.server.command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class ClearCommand implements Command<LinkedHashSet<LabWork>> {
    private final LinkedHashSet<LabWork> collection;

    public ClearCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        boolean success = MyValidator.checkCommandFormat();
        if (success) {
            collection.clear();
            System.out.println("Коллекция очищена.");
        } else {
            System.out.println("Ошибка при очистке коллекции, проверьте введенные данные.");
        }
        return collection;
    }
}
