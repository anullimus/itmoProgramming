package clientLogic;

import com.company.server.App;
import com.company.server.CollectionManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.Scanner;

public class Client {
    private static final int PORT = 1658;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите host: ");
        String host = scanner.nextLine();
        InetSocketAddress socketAddress = new InetSocketAddress(host, PORT);

        try (Selector selector = Selector.open();
             SocketChannel socketChannel = SocketChannel.open(socketAddress) {

            socketChannel.finishConnect();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);

            App app = new App(selector, socketChannel, scanner);
            app.interactiveMode();
        } catch (IOException exception) {
            System.out.println("Сервер недоступен");
        } catch (UnresolvedAddressException unresolvedAddressException){
            System.out.println("Сервер с данными недоступен.");
        }

    }


    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
    }

    public void sendMessage() {
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                writer.write(username + ": " + messageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException exception) {
            closeEverything(socket, reader, writer);
        }
    }

    // ниже врубаем многопоточность, чтобы сервер не ждал возможности вызвать команду
    public void listeningforMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = reader.readLine();
                        System.out.println(messageFromGroupChat);
                        break;
                    } catch (IOException exception) {
                        closeEverything(socket, reader, writer);
                        break;
                    }
                }
                App app = new App(new CollectionManager(System.getenv("VARRY")));
                app.interactiveMode();
            }
        }).start();

    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
