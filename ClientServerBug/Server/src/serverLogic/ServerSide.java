package serverLogic;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {

    private static final CollectionManager serverCollection = new CollectionManager(System.getenv("VARRY"));

    /**
     * Точка входа в программу. Управляет подключением к клиентам и созданием потоков для каждого из них.
     *
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main(String[] args) {
        Socket socket;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8000);
            System.out.print("Сервер начал слушать клиентов. " + "\nПорт " + serverSocket.getLocalPort() +
                    " / Адрес " + InetAddress.getLocalHost() + ".\nОжидаем подключения клиента");
            ServerSide.waitingPointer();
            socket = serverSocket.accept();
            System.out.println(socket + " подключился к серверу.");
//            todo: логирование
            ServerConnection serverConnection = new ServerConnection(serverCollection, socket);
            serverConnection.work();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Анимация ожидания соединения с клиентом путем ввода точек в консоль.
     */
    private static void waitingPointer() {
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
    }
}
