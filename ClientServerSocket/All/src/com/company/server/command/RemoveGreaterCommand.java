package com.company.server.command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class RemoveGreaterCommand implements Command<LinkedHashSet<LabWork>> {
    private  LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public RemoveGreaterCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        boolean success = myValidator.checkRemoveGreaterCommand(collection);
        if (success) {
            collection = myValidator.getCollection();
        } else {
            System.out.println("Ошибка при удалении элемента, возможно, такой элемента не существует.");
        }
        return collection;
    }
}
