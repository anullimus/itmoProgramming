package com.company.server.command;

import com.company.common.data.initial.LabWork;
import com.company.common.data.initial.Person;
import com.company.server.logic.CommandInformer;

import java.util.LinkedHashSet;

public class CountLessThanAuthorCommand implements Command<String> {
    private final LinkedHashSet<LabWork> collection;
    private final String authorName;

    public CountLessThanAuthorCommand(LinkedHashSet<LabWork> collection, String authorName) {
        this.collection = collection;
        this.authorName = authorName;
    }

    @Override
    public String execute() {
        if (collection.size() == 0) {
            return "Коллекция пуста.";
        } else {
            Person author = null;
            for (LabWork lw : collection) {
                if (lw.getName().equals(authorName)) {
                    author = lw.getAuthor();
                }
            }
            if (author != null) {
                long countOfAuthors = 0;
                for (LabWork lw : collection) {
                    if (lw.getAuthor().getBirthday().compareTo(author.getBirthday()) < 0) {
                        countOfAuthors++;
                    }
                }
                return CommandInformer.PS1
                        + "Количество лабораторных работ, авторы которых родились раньше введенного автора: "
                        + countOfAuthors;
            } else {
                return "Автор не найден.";
            }
        }
    }
}
