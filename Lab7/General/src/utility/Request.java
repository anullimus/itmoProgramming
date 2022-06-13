package utility;


import data.initial.Difficulty;
import data.initial.LabWork;

import java.io.Serializable;
import java.util.Locale;

public class Request implements Serializable {

    // heer should be sent a labwork and other not big info from client to server
    private static final long serialVersionUID = 1325084203608236803L;
    private final String commandName;
    private final boolean commandHaveArgument;
    private final CommandAnalyzer commandAnalyzer;
    private final String clientName;
    private final String clientPassword;

    private LabWork labWorkArgument;
    private Long longArgument;
    private String stringArgument;
    private Difficulty difficultyArgument;

    public Request(CommandAnalyzer commandAnalyzer, String clientName, String clientPassword) {
        this.commandAnalyzer = commandAnalyzer;
        this.commandName = commandAnalyzer.getCommandName();
        this.commandHaveArgument = commandAnalyzer.isCommandHaveArgument();
        this.clientName = clientName;
        this.clientPassword = clientPassword;
        classArgumentDefiner();
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setLabWorkArgument(LabWork labWorkArgument) {
        this.labWorkArgument = labWorkArgument;
    }

    public void setStringArgument(String stringArgument) {
        this.stringArgument = stringArgument;
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
        if (!commandAnalyzer.getCommandName().equals("technical")) {
            if (commandAnalyzer.getArgumentClass() == LabWork.class) {
                NewElementReader newElementReader = new NewElementReader();
                newElementReader.injectAddInfoFromScript(commandAnalyzer.getAddDataFromScript());
                labWorkArgument = newElementReader.readNewLabwork(commandAnalyzer.isScriptExecuting());
            } else if (commandAnalyzer.getArgumentClass() == Long.class) {
                longArgument = Long.parseLong(commandAnalyzer.getCommandArgumentString());
            } else if (commandAnalyzer.getArgumentClass() == Difficulty.class) {
                difficultyArgument = Difficulty.
                        valueOf(commandAnalyzer.getCommandArgumentString().toUpperCase(Locale.ROOT));
            } else {
                stringArgument = commandAnalyzer.getCommandArgumentString();
            }
        }
    }

    public boolean isCommandHaveArgument() {
        return commandHaveArgument;
    }
}
