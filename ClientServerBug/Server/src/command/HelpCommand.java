package command;

import serverLogic.CollectionManager;
import utility.Response;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command {
    private final HashMap<String, Command> commands;

    public HelpCommand(CollectionManager manager, HashMap<String, Command> commands) {
        super(manager);
        setDescription("Вывести справку по доступным командам.");
        this.commands = commands;
    }
    @Override
    public Response execute() {
        StringBuilder result = new StringBuilder("------------------------------------------\n");
        for(Map.Entry<String, Command> entry: commands.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue().getDescription()).append("\n");
        }
        result.append("execute_script: Считать и исполнить скрипт из указанного файла\n");
        result.append("------------------------------------------");
        return new Response(result.toString());
    }
}
