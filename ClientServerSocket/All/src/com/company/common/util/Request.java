package com.company.common.util;

import com.company.common.data.initial.LabWork;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private Number commandArgument;
    private LabWork labWorkToSend;

    public Request(String commandName) {
        this.commandName = commandName;
    }

    public void setCommandArgument(Number commandArgument) {
        this.commandArgument = commandArgument;
    }

    public void setRouteToSend(LabWork labWorkToSend) {
        this.labWorkToSend = labWorkToSend;
    }

    public String getCommandName() {
        return commandName;
    }

    public Number getCommandArgument() {
        return commandArgument;
    }

    public LabWork getLabWorkToSend() {
        return labWorkToSend;
    }

    public String toString() {
        return "[Имя команды = " + commandName
                + (commandArgument == null ? "" : "; Аргумент команды = " + commandArgument)
                + (labWorkToSend == null ? "" : "; Путь = " + labWorkToSend) + "]";
    }
}
