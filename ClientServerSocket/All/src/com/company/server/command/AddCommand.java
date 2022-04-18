package com.company.server.command;

import com.company.common.data.initial.LabWork;
import com.company.server.logic.NewElementReader;

public class AddCommand implements Command<LabWork> {
    private final String argumentLine;
    private final boolean isScript;

    public AddCommand(String argumentLine, boolean isScript) {
        this.argumentLine = argumentLine;
        this.isScript = isScript;
    }

    @Override
    public LabWork execute() {
        LabWork newLabWork;
        NewElementReader newElementReader = new NewElementReader();
        if (isScript) {
            newLabWork = newElementReader.readNewElementFromScript(argumentLine);
        } else {
            newLabWork = newElementReader.readNewElementFromConsole();
        }
        newLabWork.changeId();
        System.out.println("Элемент успешно добавлен.");
        return newLabWork;
    }
}
