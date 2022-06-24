package serverLogic;

import db.DatabaseHandler;
import db.encription.IEncryptor;
import db.encription.MD2Encryptor;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerSide {
    private static CollectionManager collectionManager;

    public static void main(String[] args) {
        DatabaseHandler databaseHandler = null;
        Scanner credentials;
        String jdbcURL;
        String username;
        String password;
        jdbcURL = "jdbc:postgresql://localhost:9999/studs";
        try {
            credentials = new Scanner(new FileReader(System.getenv("credentials")));

            username = credentials.nextLine().trim();
            password = credentials.nextLine().trim();
            databaseHandler = new DatabaseHandler(jdbcURL, username, password, new MD2Encryptor());
            collectionManager = new CollectionManager(databaseHandler.readElementsFromDB());
            databaseHandler.connectToDatabase();
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Не найден credentials.txt с данными для входа в базу данных.");
            System.exit(-1);
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("В файле не найдены данные для входа. Завершение работы.");
            System.exit(-1);
        } catch (NullPointerException nullPointerException) {
            System.err.println("Почему-то что-то не найдено");
            nullPointerException.printStackTrace();
            System.exit(-1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(7878)) {

            while (true) {
                ServerLogger.logInfoMessage("Server started listen to clients. Port " + serverSocket.getLocalPort() +
                        " / Address " + InetAddress.getLocalHost());
                ServerLogger.logInfoMessage("Waiting for client connection.");

                Pointer pointer = new Pointer();
                pointer.start();
                socket = serverSocket.accept();
                pointer.finish();

                ServerLogger.logInfoMessage(socket + " has connected to server.");
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

                Runnable r = new ServerConnection(databaseHandler, collectionManager, socket, inputStream, outputStream);
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
        ServerLogger.logInfoMessage("Finishing the server's work...");
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
//class Saver {
//    void save(Scanner scanner, ServerConnection serverConnection) {
//        Thread thread = new Thread(() -> {
//            try {
//                while (true) {
//                    if (scanner.nextLine().trim().equals("save")) {
//                        serverConnection.getCollectionManager().save();
//                        ServerLogger.logInfoMessage("Collection successfully has been saved!");
//                    }
//                }
//            } catch (BufferOverflowException | IndexOutOfBoundsException exception) {
//                ServerLogger.logErrorMessage("Find some not serious problems. Please try again.");
//            } catch (NoSuchElementException noSuchElementException) {         //  ctrl+D
//                ServerSide.exit();
//            } catch (Exception exception) {
//                ServerLogger.logErrorMessage("Find some strange problems. Please try again.");
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
//    }
//}