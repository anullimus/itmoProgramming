package utility;


import clientLogic.NewElementReader;
import data.initial.Difficulty;
import data.initial.LabWork;

import java.io.Serializable;
import java.util.Locale;

public class Request implements Serializable {

    // heer should be sent a labwork and other not big info from client to server
    private final String commandName;
    private final boolean commandHaveArgument;
    private final CommandAnalyzer commandAnalyzer;

    private LabWork labWorkArgument;
    private Long longArgument;
    private String stringArgument;
    private Difficulty difficultyArgument;

    public Request(CommandAnalyzer commandAnalyzer) {
        this.commandAnalyzer = commandAnalyzer;
        this.commandName = commandAnalyzer.getCommandName();
        this.commandHaveArgument = commandAnalyzer.isCommandHaveArgument();
        classArgumentDefiner();
    }


    public String getCommandName() {
        return commandName;
    }

    public LabWork getLabWorkArgument() {
        return labWorkArgument;
    }

    public Long getLongArgument() {
        return longArgument;
    }

    public String getStringArgument() {
        return stringArgument;
    }

    public Difficulty getDifficultyArgument() {
        return difficultyArgument;
    }

    private void classArgumentDefiner() {

        if (commandAnalyzer.getArgumentClass() == LabWork.class) {
            labWorkArgument = new NewElementReader(commandAnalyzer.getAddDataFromScript()).readNewLabwork(commandAnalyzer.isScriptExecuting());
        } else if (commandAnalyzer.getArgumentClass() == Long.class) {
            longArgument = Long.parseLong(commandAnalyzer.getCommandArgumentString());
        } else if (commandAnalyzer.getArgumentClass() == Difficulty.class) {
            difficultyArgument = Difficulty.
                    valueOf(commandAnalyzer.getCommandArgumentString().toUpperCase(Locale.ROOT));
        } else {
            stringArgument = commandAnalyzer.getCommandArgumentString();
        }
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }
}
