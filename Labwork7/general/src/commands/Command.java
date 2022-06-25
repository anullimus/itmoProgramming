package commands;

import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;

import java.io.Serializable;


public abstract class Command implements Serializable {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public abstract CommandResultDto execute(
            DataManager dataManager,
            HistoryManager historyManager,
            String username
    );

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Command{"
                + "name='" + name + '\''
                + '}';
    }
}
