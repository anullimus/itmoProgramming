package command;

import serverLogic.CollectionManager;

import java.util.HashMap;

public class ManCommand extends AbstractCommand{
    private final HashMap<String, AbstractCommand> commands;

    public ManCommand(CollectionManager manager, HashMap<String, AbstractCommand> commands) {
        super(manager);
        setDescription("Показать руководство к конкретной команде.");
        this.commands = commands;
    }

    @Override
    public String execute(String arg) {
        if (commands.containsKey(arg)){
            return arg + ": " + commands.get(arg).getDescription();
        }
        else {
            return "Неправильный аргумент.";
        }
    }
}
