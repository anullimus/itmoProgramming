package command;

import serverLogic.CollectionManager;

import java.util.HashMap;

public class HelpCommand extends AbstractCommand{
    private final HashMap<String, AbstractCommand> commands;

    public HelpCommand(CollectionManager manager, HashMap<String, AbstractCommand> commands) {
        super(manager);
        setDescription("Вывести справку по доступным командам.");
        this.commands = commands;
    }
    @Override
    public String execute() {
        return commands.keySet() + "\nВведите 'man {command}' для получения справки к команде.";
    }

}
