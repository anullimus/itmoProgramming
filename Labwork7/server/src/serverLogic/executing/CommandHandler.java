package serverLogic.executing;


import commands.PrivateAccessedStudyGroupCommand;
import commands.RegisterCommand;
import dto.CommandFromClientDto;
import dto.CommandResultDto;
import util.DataManager;
import util.HistoryManager;
import util.Pair;
import util.State;

import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.Logger;

public class CommandHandler {
    private final Queue<Pair<CommandFromClientDto, SocketAddress>> queueToBeExecuted;
    private final Queue<Pair<CommandResultDto, SocketAddress>> queueToBeSent;
    private final DataManager dataManager;
    private final HistoryManager historyManager;

    public CommandHandler(
            Queue<Pair<CommandFromClientDto, SocketAddress>> queueToBeExecuted,
            Queue<Pair<CommandResultDto, SocketAddress>> queueToBeSent,
            DataManager dataManager,
            HistoryManager historyManager) {
        this.queueToBeExecuted = queueToBeExecuted;
        this.queueToBeSent = queueToBeSent;
        this.dataManager = dataManager;
        this.historyManager = historyManager;
    }

    public void startToHandleCommands(
            State<Boolean> isWorkingState,
            ExecutorService threadPool
    ) {
        Runnable startCheckingForAvailableCommandsToRun = new Runnable() {
            @Override
            public void run() {
                while (isWorkingState.getValue()) {
                    if (!queueToBeExecuted.isEmpty()) {
                        Pair<CommandFromClientDto, SocketAddress> pairOfCommandAndClientAddress = queueToBeExecuted.poll();
                        Runnable executeFirstCommandTack = new Runnable() {
                            @Override
                            public void run() {
                                ServerLogger.logInfoMessage("Starting to execute a new command");
                                assert pairOfCommandAndClientAddress != null;
                                CommandFromClientDto commandFromClientDto = pairOfCommandAndClientAddress.getFirst();
                                SocketAddress clientAddress = pairOfCommandAndClientAddress.getSecond();
                                try {

                                    executeWithValidation(commandFromClientDto, clientAddress);
                                } catch (Exception e) {
                                    ServerLogger.logErrorMessage(e.getMessage());
                                }

                                ServerLogger.logInfoMessage("Successfully executed the command command");


                            }
                        };
                        threadPool.submit(executeFirstCommandTack);
                    }
                }
            }
        };
        threadPool.submit(startCheckingForAvailableCommandsToRun);
    }

    private void executeWithValidation(CommandFromClientDto commandFromClientDto, SocketAddress clientAddress) {
        if (dataManager.validateUser(commandFromClientDto.getLogin(), commandFromClientDto.getPassword()) || commandFromClientDto.getCommand() instanceof RegisterCommand) {
            if (commandFromClientDto.getCommand() instanceof PrivateAccessedStudyGroupCommand) {
                final int id = ((PrivateAccessedStudyGroupCommand) commandFromClientDto.getCommand()).getStudyGroupId();
                if (dataManager.validateOwner(commandFromClientDto.getLogin(), id)) {
                    queueToBeSent.add(new Pair<>(commandFromClientDto.getCommand().execute(dataManager, historyManager, commandFromClientDto.getLogin()), clientAddress));
                } else {
                    queueToBeSent.add(new Pair<>(new CommandResultDto("You are not the owner of the object so you can't do anything with it", true), clientAddress));
                }
            } else {
                queueToBeSent.add(new Pair<>(commandFromClientDto.getCommand().execute(dataManager, historyManager, commandFromClientDto.getLogin()), clientAddress));
            }
        } else {
            queueToBeSent.add(new Pair<>(new CommandResultDto("Invalid login or password. Command was not executed", false), clientAddress));
        }
    }

}
