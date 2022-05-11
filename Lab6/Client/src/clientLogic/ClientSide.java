package clientLogic;

import java.net.InetSocketAddress;

public class ClientSide {

    public static void main(String[] args) {
        System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8000);
        ClientConnection clientConnection = new ClientConnection(socketAddress);
        clientConnection.work();
    }
}
