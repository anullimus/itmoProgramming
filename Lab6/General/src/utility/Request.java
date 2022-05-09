package utility;


import data.initial.Country;
import data.initial.Difficulty;
import data.initial.LabWork;

import java.io.Serializable;

public class Request implements Serializable {
    // heer should be sent a labwork and other not big info from client to server

    private final String commandName;
    private Class<?> commandArgument;
    private boolean commandHaveArgument;

    private LabWork labWork;
    private Enum<Country> countryEnum;
    private Enum<Difficulty> difficultyEnum;

    public Request(String commandName) {
        this.commandName = commandName;
    }

    public Request(String commandName, String commandArgumentString) {
        this.commandName = commandName;
        this.commandHaveArgument = true;
    }

    public String getCommandName() {
        return commandName;
    }

    public LabWork getLabWork() {
        return labWork;
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }

}
