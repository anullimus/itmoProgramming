package com.company.server.command;

import com.company.common.data.initial.LabWork;
import com.company.server.logic.CommandInformer;

import java.util.LinkedHashSet;

public class RemoveLowerCommand implements Command<LinkedHashSet<LabWork>> {
    private final LinkedHashSet<LabWork> collection;
    private final String valueOfId;

    public RemoveLowerCommand(LinkedHashSet<LabWork> collection, String valueOfId) {
        this.collection = collection;
        this.valueOfId = valueOfId;
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        if (collection.size() == 0) {
            System.out.println("Коллекция пуста.");
        } else {
            long id;
            float minimalPoint = -1;
            try {
                id = Long.parseLong(valueOfId);
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Введеныные данные некорректны.");
                return collection;
            }
            for (LabWork labWork : collection) {
                if (labWork.getId() == id) {
                    minimalPoint = labWork.getMinimalPoint();
                    break;
                }
            }
            if (minimalPoint == -1) {
                System.out.println("Лабораторная работа с введенным id не найдена.");
            } else {
                for (LabWork ticket : collection) {
                    if (ticket.getMinimalPoint().compareTo(minimalPoint) < 0) {
                        collection.remove(ticket);
                    }
                }
                System.out.println(CommandInformer.PS1 + "Лаб. работы, сделанные на баллы меньше, чем " + minimalPoint +
                        " удалены из коллекции. Оставшееся количество элементов в коллекции: " + collection.size());
            }
        }
        return collection;
    }
}