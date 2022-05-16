package serverLogic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {
    private static CollectionManager collectionManager;

    public static void main(String[] args) {
        try {
            collectionManager = new CollectionManager(System.getenv("VARRY"));
        } catch (NullPointerException nullPointerException) {
            System.err.println("You didn't set any path as argument to environment. So, goodbye.");
            System.exit(0);
        }catch (FileNotFoundException fileNotFoundException) {
            System.err.println("File-collection doesn't exists by inputted path.\n" +
                    "The elements wasn't added to the program's collection");
            System.exit(0);
        }
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
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
            pointer.setDaemon(true);
            pointer.start();
            while (true) {
                System.out.println("Sever started listen to clients. " + "\nPort " + serverSocket.getLocalPort() +
                        " / Adress " + InetAddress.getLocalHost() + ".\nWaiting for client connection.");
                socket = serverSocket.accept();
                pointer.interrupt();

                System.out.println(socket + " has connected to server.");
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                ServerConnection serverConnection = new ServerConnection(collectionManager, socket, inputStream, outputStream);
                serverConnection.work();
//                Runnable r = new ServerConnection(collectionManager, socket, inputStream, outputStream);
//                Thread t = new Thread(r);
//                t.start();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ServerSide.exit();
        }
    }

    private static void exit() {
        System.out.println(collectionManager.save());
        System.out.println("Finishing the server's work...");
        System.exit(0);
    }
}
