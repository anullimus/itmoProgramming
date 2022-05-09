package command;

import serverLogic.CollectionManager;

import java.util.HashMap;

public class HelpCommand extends Command {
    private final HashMap<String, Command> commands;

    public HelpCommand(CollectionManager manager, HashMap<String, Command> commands) {
        super(manager);
        setDescription("Вывести справку по доступным командам.");
        this.commands = commands;
    }
    @Override
    public String execute() {
        return commands.entrySet().toString();
    }
}
