package com.company.server.command;

import com.company.common.data.initial.LabWork;

import java.util.LinkedHashSet;

public class MaxByAuthorCommand implements Command<String> {
    private final LinkedHashSet<LabWork> collection;

    public MaxByAuthorCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
    }

    @Override
    public String execute() {
        if (collection.isEmpty()) {
            return "Коллекция пуста.";
        } else {
            LabWork[] arr = new LabWork[collection.size()];
            arr = collection.toArray(arr);
            LabWork lwWithOldestAuthor = arr[0];
            for (LabWork lw : collection) {
                if (lwWithOldestAuthor.getAuthor().getBirthday().compareTo(lw.getAuthor().getBirthday()) < 0) {
                    lwWithOldestAuthor = lw;
                }
            }
            return "Лабораторная работа, написанная самым старым автором: " + lwWithOldestAuthor;
        }
    }
}
