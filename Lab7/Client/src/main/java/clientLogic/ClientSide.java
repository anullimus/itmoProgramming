package clientLogic;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.Scanner;

public class ClientSide {

    public static void main(String[] args) {
        Scanner fromKeyboard = new Scanner(System.in);
        System.out.println("Запуск клиентского модуля.\nПодключение к серверу ...");

//        System.out.println(InetAddress.getLocalHost());


        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8000);
        while (true) {
            try (Selector selector = Selector.open();
                 SocketChannel socketChannel = SocketChannel.open(socketAddress)) {

                socketChannel.finishConnect();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);

                ClientConnection clientConnection = new ClientConnection(socketChannel, selector, fromKeyboard);
                clientConnection.work();

            } catch (ConnectException connectException) {
                System.err.println("Нет связи с сервером. Подключться ещё раз (введите {да} или {нет})?");
                String answer;
                while (!(answer = fromKeyboard.nextLine()).equals("да")) {
                    switch (answer) {
                        case "":
                            break;
                        case "нет":
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Введите корректный ответ.");
                    }
                }
                System.out.print("Подключение ...\n");
            } catch (IOException e) {
                System.out.println("Сервер отдыхает");
            } catch (UnresolvedAddressException e) {
                System.out.println("Сервер с данным адресом недоступен");
            }
        }
    }
}
