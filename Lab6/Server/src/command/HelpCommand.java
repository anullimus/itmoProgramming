package command;

import serverLogic.CollectionManager;
import utility.Response;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends AbstractCommand {
    private final HashMap<String, AbstractCommand> commands;

    public HelpCommand(CollectionManager manager, HashMap<String, AbstractCommand> commands) {
        super(manager);
        setDescription("Вывести справку по доступным командам.");
        this.commands = commands;
    }
    @Override
    public Response execute() {
        StringBuilder result = new StringBuilder("------------------------------------------\n");
        for(Map.Entry<String, AbstractCommand> entry: commands.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue().getDescription()).append("\n");
        }
        result.append("execute_script: Считать и исполнить скрипт из указанного файла\n");
        result.append("exit: Завершить работу клиентского приложения\n");
        result.append("------------------------------------------");
        return new Response(result.toString());
    }
}
