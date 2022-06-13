package serverLogic;

import database.DBManager;
import encryption.IEncryptor;
import encryption.MD2Encryptor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerSide {
    private static CollectionManager collectionManager;

    private ServerSide() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            IEncryptor encryptor = new MD2Encryptor();
            DBManager dbManager = new DBManager(System.getenv("DB_URL"), System.getenv("DB_USERNAME"),
                    System.getenv("DB_PASSWORD"), encryptor);
            collectionManager = new CollectionManager(dbManager);

            ServerLogger.logInfoMessage("Подключение к базе данных установлено, ожидание подключений клиентов");
        } catch (NullPointerException nullPointerException) {
            ServerLogger.logErrorMessage("You didn't set any path as argument to environment / or file is empty. So, goodbye.");
            System.exit(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ServerLogger.logErrorMessage(e.getMessage());
        }
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(7878)) {

            Scanner scanner = new Scanner(System.in);
            while (true) {
                ServerLogger.logInfoMessage("Server started listen to clients. Port " + serverSocket.getLocalPort() +
                        " / Address " + InetAddress.getLocalHost());
                ServerLogger.logInfoMessage("Waiting for client connection.");

//                Pointer pointer = new Pointer();
//                pointer.start();
                socket = serverSocket.accept();
//                pointer.finish();

                ServerLogger.logInfoMessage(socket + " has connected to server.");
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                ServerConnection serverConnection = new ServerConnection(collectionManager, socket, inputStream, outputStream);

                ServerCommands saver = new ServerCommands();
                saver.save(scanner, serverConnection);

//              serverConnection.work();
                Runnable r = new ServerConnection(collectionManager, socket, inputStream, outputStream);
                Thread t = new Thread(r);
                t.start();

            }
        } catch (IOException ioException) {
            ServerLogger.logErrorMessage(ioException.getMessage());
            ServerSide.exit();
        } catch (NoSuchElementException noSuchElementException) {         //  ctrl+D
            exit();
        } catch (Exception exception) {
            ServerLogger.logErrorMessage("Handled some unexpected exception");
            exit();
        }
    }

    public static void exit() {
//        ServerLogger.logInfoMessage(collectionManager.save());
        ServerLogger.logInfoMessage("Server work has been stopped.");
        System.exit(1);
    }
}

/**
 * class for visual - outputs points when the Server is waiting the Client connection
 */
class Pointer {
    Thread pointer = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.print("\n");
                Thread.currentThread().interrupt();
            }
        }
    });

    void start() {
        pointer.setDaemon(true);
        pointer.start();
    }

    void finish() {
        pointer.interrupt();
    }
}

/**
 * class for realisation of server's single command save()
 */
class ServerCommands {
    void save(Scanner scanner, ServerConnection serverConnection) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    if (scanner.nextLine().trim().equals("save")) {
//                        serverConnection.getCollectionManager().save();
                        ServerLogger.logInfoMessage("The collection saves automatically to DB.");
                    } else if (scanner.nextLine().trim().equals("exit")) {
                        ServerSide.exit();
                    }
                }
            } catch (BufferOverflowException | IndexOutOfBoundsException exception) {
                ServerLogger.logErrorMessage("Find some not serious problems. Please try again.");
            } catch (NoSuchElementException noSuchElementException) {         //  ctrl+D
                ServerSide.exit();
            } catch (Exception exception) {
                ServerLogger.logErrorMessage("Find some strange problems. Please try again.");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}