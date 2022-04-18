package com.company.server.command;

import com.company.client.App;
import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;
import com.company.server.CommandInformer;

import java.util.LinkedHashSet;

public class RemoveLowerCommand implements Command<LinkedHashSet<LabWork>> {
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