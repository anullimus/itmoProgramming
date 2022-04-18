package com.company.server.command;

import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class RemoveByIDCommand implements Command<LinkedHashSet<LabWork>>{
    private final String valueOfId;
    private final LinkedHashSet<LabWork> collection;

    public RemoveByIDCommand(String valueOfId, LinkedHashSet<LabWork> collection) {
        this.valueOfId = valueOfId;
        this.collection = collection;
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        long id;
        try {
            id = Long.parseLong(valueOfId);
        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("Введеныные данные некорректны.");
            return collection;
        }
        for (LabWork labWork : collection) {
            if (labWork.getId() == id) {
                collection.remove(labWork);
                System.out.println("Элемент с id = " + id + " успешно удален из коллекции.");
                return collection;
            }
        }
        System.out.println("Here is no lab work find with id=" + id);
        return collection;
    }
}