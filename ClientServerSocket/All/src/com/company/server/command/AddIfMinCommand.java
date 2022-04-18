package com.company.server.command;

import com.company.common.data.initial.LabWork;
import com.company.server.logic.CommandInformer;
import com.company.server.logic.NewElementReader;

import java.util.LinkedHashSet;

public class AddIfMinCommand implements Command<LinkedHashSet<LabWork>> {
    private final LinkedHashSet<LabWork> collection;
    private final boolean isScript;
    private final String argumentLine;
    private final NewElementReader newElementReader;

    public AddIfMinCommand(LinkedHashSet<LabWork> collection, boolean isScript, String argumentLine) {
        this.collection = collection;
        this.isScript = isScript;
        this.argumentLine = argumentLine;
        this.newElementReader = new NewElementReader();
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        LabWork labWork;
        if (isScript) {
            labWork = newElementReader.readNewElementFromScript(argumentLine);
        } else {
            labWork = newElementReader.readNewElementFromConsole();
        }
        if (collection.size() == 0) {
            System.out.println("Коллекция пуста.");
        } else {
            for (LabWork lw : collection) {
                if (labWork.getMinimalPoint().compareTo(lw.getMinimalPoint()) < 0) {
                    System.out.println("Наименьший балл введенной лабораторной работы не является меньше " +
                            "минимального балла лабораторных из коллекции.");
                    return collection;
                }
                collection.add(labWork);
                System.out.println(CommandInformer.PS1
                        + "Наименьший балл введенной лабораторной работы является меньше "
                        + "минимального балла лабораторных из коллекции, "
                        + "поэтому лабораторная работа добавлена в коллекцию. Текущее количество элементов в коллекции: "
                        + collection.size());
            }
        }
        return collection;
    }
}
