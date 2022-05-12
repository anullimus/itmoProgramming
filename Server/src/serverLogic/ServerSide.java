package serverLogic;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {

    private static final CollectionManager serverCollection = new CollectionManager(System.getenv("VARRY"));

    public static void main(String[] args) {
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(8000)){
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
                System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + serverSocket.getLocalPort() +
                        " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиента");
                socket = serverSocket.accept();
                pointer.interrupt();
                System.out.println(socket + " подключился к серверу.");
//            todo: логирование
                ServerConnection serverConnection = new ServerConnection(serverCollection, socket);
                serverConnection.work();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
