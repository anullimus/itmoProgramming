package serverLogic;

import exception.ExitException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerSide {
    private static CollectionManager collectionManager;

    public static void main(String[] args) {
        try {
            collectionManager = new CollectionManager(System.getenv("VARRY"));
        } catch (NullPointerException nullPointerException) {
            ServerLogger.logErrorMessage("You didn't set any path as argument to environment. So, goodbye.");
            System.exit(1);
        } catch (FileNotFoundException fileNotFoundException) {
            ServerLogger.logErrorMessage("File-collection doesn't exists by inputted path.\n" +
                    "The elements wasn't added to the program's collection");
            System.exit(1);
        }
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(7878)) {

            Scanner scanner = new Scanner(System.in);
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
                ServerConnection serverConnection = new ServerConnection(collectionManager, socket, inputStream, outputStream);

                Saver saver = new Saver();
                saver.save(scanner, serverConnection);

                try {
                    serverConnection.work();
                } catch (ExitException exitException) {
                    ServerLogger.logErrorMessage(exitException.getMessage());
                }
//                Runnable r = new ServerConnection(collectionManager, socket, inputStream, outputStream);
//                Thread t = new Thread(r);
//                t.start();
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
        ServerLogger.logInfoMessage(collectionManager.save());
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
class Saver {
    void save(Scanner scanner, ServerConnection serverConnection) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    if (scanner.nextLine().trim().equals("save")) {
                        serverConnection.getCollectionManager().save();
                        ServerLogger.logInfoMessage("Collection successfully has been saved!");
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