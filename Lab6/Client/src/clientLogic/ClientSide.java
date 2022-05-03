package clientLogic;

public class ClientSide {
    /**
     * Отправная точка клиента. Создает объект {@link ClientConnection}.
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main(String[] args) {
        System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");
        ClientConnection connection = new ClientConnection();
        connection.work();
    }
}
