package com.company.server.command;

import com.company.client.MyValidator;
import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class AddCommand implements Command<LinkedHashSet<LabWork>> {
    private LinkedHashSet<LabWork> collection;
    private final MyValidator myValidator;

    public AddCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        myValidator = new MyValidator();
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        boolean success = myValidator.checkAddCommand(collection);
        if (success) {
            collection = myValidator.getCollection();
            System.out.println("Элемент успешно добавлен");
        } else {
            System.out.println("Ошибка при добавлении элемента, возможно, такой элемент уже существует");
        }
        return collection;
    }
}
