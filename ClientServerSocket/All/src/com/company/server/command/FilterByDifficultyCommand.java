package com.company.server.command;

import com.company.common.data.initial.Difficulty;
import com.company.common.data.initial.LabWork;
import com.company.server.logic.CommandInformer;

import java.util.LinkedHashSet;

public class FilterByDifficultyCommand implements Command<String> {
    private final LinkedHashSet<LabWork> collection;
    private final String difficultValue;

    public FilterByDifficultyCommand(LinkedHashSet<LabWork> collection, String difficultValue) {
        this.collection = collection;
        this.difficultValue = difficultValue;
    }

    @Override
    public String execute() {
        try {
            if (difficultValue == null) {
                throw new IllegalArgumentException();
            } else if (collection.size() == 0) {
                throw new NullPointerException();
            } else {
                boolean wasAtLeastOneThisTypeOfElementInCollection = false;
                for (LabWork lw : collection) {
                    if (lw.getDifficulty().equals(Difficulty.valueOf(difficultValue))) {
                        System.out.println(lw);
                        wasAtLeastOneThisTypeOfElementInCollection = true;
                    }
                }
                if (wasAtLeastOneThisTypeOfElementInCollection) {
                    return CommandInformer.PS1 + "Выведены все лаб. работы типа " + difficultValue + ".";
                } else {
                    return CommandInformer.PS1 + "В коллекции не нашлось ни одного элемента с заданным типом";
                }
            }
        } catch (NullPointerException exception) {
            return CommandInformer.PS1 + "Коллекция пуста.";
        } catch (IllegalArgumentException exception) {
            return CommandInformer.PS1 + "Неправильный аргумент, попробуйте ввести команду еще раз.";
        }
    }
}
