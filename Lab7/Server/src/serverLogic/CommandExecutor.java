package serverLogic;

import command.AbstractCommand;
import utility.CommandResponse;
import utility.IResponse;
import utility.Request;

import java.util.concurrent.RecursiveTask;

public class CommandExecutor extends RecursiveTask<IResponse> {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final Request request;

    public CommandExecutor(CollectionManager collectionManager, CommandManager commandManager, Request request) {
        this.commandManager = commandManager;
        this.request = request;
        this.collectionManager = collectionManager;
    }

    @Override
    protected IResponse compute() {
        AbstractCommand command = commandManager.getCommandByName(request.getCommandName());
        if (command.needCheckAuthentication()) {
            if (!collectionManager.getDbManager().checkIfUserConnect(request)) {
                return new CommandResponse("Вы не авторизованы");
            }
        }
        return command.execute(request);
    }
}
