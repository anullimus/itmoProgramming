package serverLogic;

import org.postgresql.util.PSQLException;
import serverLogic.executing.Console;
import serverLogic.executing.MainApp;
import serverLogic.db.DataManagerImpl;
import serverLogic.db.Database;
import serverLogic.executing.ServerLogger;
import util.DataManager;
import util.State;

import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public final class Server {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static int serverPort;
    private static String serverIp;

    private static String username;
    private static String password;
    private static final int numOfThreads = 50;
    private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(numOfThreads);
    private static final ExecutorService CACHED_THREAD_POOL = Executors.newCachedThreadPool();

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        String URL = "jdbc:postgresql://pg:5432/studs";
        try {
            initMainInfoForConnection();
            Connection connection;
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(URL, username, password);

            ServerLogger.logInfoMessage("Successfully made a connection with the database");
            State<Boolean> serverIsWorkingState = new State<>(true);
            Database database = new Database(connection);
            DataManager dataManager = new DataManagerImpl(database);
            dataManager.initialiseData();
            Console console = new Console(serverIsWorkingState);
            MainApp serverApp = new MainApp(serverPort, serverIp, CACHED_THREAD_POOL, FIXED_THREAD_POOL, dataManager);
            CACHED_THREAD_POOL.submit(console::start);
            serverApp.start(serverIsWorkingState);
        }catch (PSQLException psqlException){
            ServerLogger.logErrorMessage("You may not exist. Please check it.");
    //            psqlException.printStackTrace();
            System.exit(-1);
        }
        catch (SQLException sqlException) {
            ServerLogger.logErrorMessage("Couldn't connect to the server - check it's work. Please check if your login and password were correct.");
            sqlException.printStackTrace();
        } catch (IOException e) {
            ServerLogger.logErrorMessage("An unexpected IO error occurred. The message is: " + e.getMessage());
        } catch (UnresolvedAddressException e) {
            ServerLogger.logErrorMessage("Could not resolve the address you entered. Please re-start the server with another one");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            CACHED_THREAD_POOL.shutdown();
            FIXED_THREAD_POOL.shutdown();
        }
    }

    private static void initMainInfoForConnection() throws IOException {
        serverPort = 7878;
        serverIp = "localhost";

        System.out.print("Enter username: ");
        username = SCANNER.nextLine();
        try {
            char[] passwd = System.console().readPassword("Enter password: ");
            password = String.valueOf(passwd);
        }catch (NullPointerException nullPointerException){
            password = SCANNER.nextLine();
        }
    }
}



