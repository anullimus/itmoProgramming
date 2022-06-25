package serverLogic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serverLogic.executing.Console;
import serverLogic.executing.MainApp;
import serverLogic.db.DataManagerImpl;
import serverLogic.db.Database;
import util.DataManager;
import util.State;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;


public final class Server {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final Scanner BUFFERED_READER = new Scanner(System.in);
    private static int serverPort;
    private static String serverIp;
    private static final int MAX_PORT = 65535;
    private static final int MIN_PORT = 1024;
    private static String dbHost;
    private static String dbName;
    private static String username;
    private static String password;
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();
    private static final ExecutorService CACHED_THREAD_POOL = Executors.newCachedThreadPool();

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        String URL = "jdbc:postgresql://localhost:9999/studs";
        try {
            initMainInfoForConnection();
            Connection connection;
            connection = DriverManager.getConnection(URL, username, password);

//            LOGGER.info("Successfully made a connection with the database");
            System.out.println("Successfully made a connection with the database");
            State<Boolean> serverIsWorkingState = new State<>(true);
            Database database = new Database(connection, LOGGER);
            DataManager dataManager = new DataManagerImpl(database, LOGGER);
            dataManager.initialiseData();
            Console console = new Console(serverIsWorkingState, LOGGER);
            MainApp serverApp = new MainApp(LOGGER,
                    serverPort,
                    serverIp,
                    CACHED_THREAD_POOL,
                    FORK_JOIN_POOL,
                    dataManager);
            CACHED_THREAD_POOL.submit(console::start);
            serverApp.start(serverIsWorkingState);
        } catch (SQLException sqlException) {
            LOGGER.error("Couldn't connect to the server. Please check if your login and password were correct.");
            sqlException.printStackTrace();

        } catch (IOException e) {
            LOGGER.error("An unexpected IO error occurred. The message is: " + e.getMessage());
        } catch (UnresolvedAddressException e) {
            LOGGER.error("Could not resolve the address you entered. Please re-start the server with another one");
        } finally {
            CACHED_THREAD_POOL.shutdown();
            FORK_JOIN_POOL.shutdown();
        }
    }

    private static void initMainInfoForConnection() throws IOException {
        serverPort = 7878;

        serverIp = "localhost";
        dbHost = "localhost";

        dbName = "s335100db_all";
        System.out.println("Enter username");
        username = BUFFERED_READER.nextLine();
        System.out.println("Enter password");
        password = BUFFERED_READER.nextLine();
    }

//    private static <T> T ask(
//            Predicate<T> predicate,
//            String askMessage,
//            String errorMessage,
//            String wrongValueMessage,
//            Function<String, T> converter
//    ) throws IOException {
//        LOGGER.info(askMessage);
//        String input;
//        T value;
//        do {
//            try {
//                input = BUFFERED_READER.readLine();
//                value = converter.apply(input);
//            } catch (IllegalArgumentException e) {
//                LOGGER.error(errorMessage);
//                continue;
//            }
//            if (predicate.test(value)) {
//                return value;
//            } else {
//                LOGGER.error(wrongValueMessage);
//            }
//        } while (true);
//    }
//
//    private static String ask(
//            String askMessage
//    ) throws IOException {
//        LOGGER.info(askMessage);
//        String input;
//        input = BUFFERED_READER.readLine();
//        return input;
//    }
}



