package clientLogic;

import java.net.InetSocketAddress;

public class ClientSide {
    /**
     * Отправная точка клиента. Создает объект {@link ClientConnection}.
     *
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main(String[] args) {
        System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8000);
        ClientConnection clientConnection = new ClientConnection(socketAddress);
        clientConnection.work();
    }
}
