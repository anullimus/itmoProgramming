package serverLogic.executing;


import util.Request;
import util.Response;
import serverLogic.connection.ClientDataReceiver;
import serverLogic.connection.ClientDataSender;
import serverLogic.util.HistoryManagerImpl;
import util.DataManager;
import util.Pair;
import util.State;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class MainApp {
    private final Queue<Pair<Request, SocketAddress>> queueToBeExecuted;
    private final Queue<Pair<Response, SocketAddress>> queueToBeSent;
    private final int port;
    private final String ip;
    private final CommandHandler commandHandler;
    private final ClientDataReceiver clientDataReceiver;
    private final ExecutorService threadPool;
    private final ForkJoinPool forkJoinPool;
    private final DataManager dataManager;

    public MainApp(
            int port,
            String ip,
            ExecutorService threadPool,
            ForkJoinPool forkJoinPool,
            DataManager dataManager
    ) {
        this.ip = ip;
        this.port = port;
        queueToBeExecuted = new LinkedBlockingQueue<>();
        queueToBeSent = new LinkedBlockingQueue<>();
        this.commandHandler = new CommandHandler(queueToBeExecuted, queueToBeSent, dataManager, new HistoryManagerImpl());
        this.clientDataReceiver = new ClientDataReceiver(queueToBeExecuted);
        this.threadPool = threadPool;
        this.forkJoinPool = forkJoinPool;
        this.dataManager = dataManager;
    }

    public void start(
            State<Boolean> isWorking
    ) throws IOException {
        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            datagramChannel.bind(new InetSocketAddress(ip, port));
            datagramChannel.configureBlocking(false);

            threadPool.submit(() -> {
                try {
                    clientDataReceiver.startReceivingData(datagramChannel, isWorking, threadPool);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });


            commandHandler.startToHandleCommands(
                    isWorking,
                    threadPool
            );


            while (isWorking.getValue()) {
                if (!queueToBeSent.isEmpty()) {
                    Pair<Response, SocketAddress> commandResultDtoAndSocketAddress = queueToBeSent.poll();
                    forkJoinPool.invoke(new ClientDataSender(commandResultDtoAndSocketAddress.getFirst(), datagramChannel,
                            commandResultDtoAndSocketAddress.getSecond()));
                }
            }

        } catch (BindException e) {
            ServerLogger.logErrorMessage("Could not start the server, bind exception. Please, use another port.");
            isWorking.setValue(false);
        }
    }
}
