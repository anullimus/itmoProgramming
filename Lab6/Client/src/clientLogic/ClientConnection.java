package clientLogic;


import utility.CommandAnalyzer;
import utility.Request;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection {
    private static Scanner fromKeyboard;
    private static ObjectOutputStream toServer;
    private static ObjectInputStream fromServer;
    private static CommandAnalyzer commandAnalyzer;

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void work() {
        try (Scanner scanner = new Scanner(System.in)) {
            commandAnalyzer = new CommandAnalyzer();
            fromKeyboard = scanner;
            Socket outcoming;
            ObjectOutputStream outputStream;
            ObjectInputStream inputStream;
            while (true) {
                try {
                    outcoming = new Socket("localhost", 8000);
                    outputStream = new ObjectOutputStream(outcoming.getOutputStream());
                    toServer = outputStream;

                    inputStream = new ObjectInputStream(outcoming.getInputStream());
                    fromServer = inputStream;

                    interactiveMode();
                    break;
                } catch (IOException e) {
                    System.err.println("Нет связи с сервером. Попробовать подключиться к нему ещё раз?(да/нет)");
                    String answer;
                    while (!(answer = fromKeyboard.nextLine()).equals("да")) {
                        if ("нет".equals(answer)) {
                            exit();
                        } else {
                            System.out.println("Введите корректный ответ.");
                        }
                    }
                    System.out.print("Подключение ...");
                }
            }
        }
    }

    /**
     * Парсит пользовательские команды и осуществляет обмен данными с сервером.
     *
     * @throws IOException при отправке и получении данных с сервера.
     */
    private void interactiveMode() {
        try {
            System.out.println((String) fromServer.readObject());
            String command;
            while (!(command = fromKeyboard.nextLine()).equals("exit")) {
                try {
                    String[] parsedCommand = command.trim().split(" ", 2);
                    if ("".equals(parsedCommand[0])) {
                        continue;
                    }
                    if (commandAnalyzer.analyzeCommand(command)) {
                        String inputLabwork;
                        if (commandAnalyzer.getCommandName().equals("add")) {
                            inputLabwork = new NewElementReader().readNewLabwork();
                            toServer.writeObject(new Request("add", inputLabwork));
                        } else if (commandAnalyzer.getCommandName().equals("add_if_man")) {
                            inputLabwork = new NewElementReader().readNewLabwork();
                            toServer.writeObject(new Request("add_if_min", inputLabwork));
                        } else if (commandAnalyzer.isCommandHaveArgument()) {
                            toServer.writeObject(new Request(commandAnalyzer);
                        } else {
                            toServer.writeObject(new Request(commandAnalyzer.getCommandName()));
                        }
                        System.out.println((String) fromServer.readObject());
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException illegalArgumentException) {
                    System.err.print("Введена некорректная команда. Воспользуйтесь 'man'-инструкцией для более " +
                            "подробной информации о команде.\n");
                }
            }
            exit();
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.print("Введена некорректная команда. Воспользуйтесь 'man'-инструкцией для более " +
                    "подробной информации о команде.\n");
        } catch (ClassNotFoundException classNotFoundException) {
            System.err.println("Класс не найден: " + classNotFoundException.getMessage() + "\n");
        } catch (IOException ioException) {
            ioException.printStackTrace();  // idk how can it happen
        }
    }

    /**
     * Завершает работу клиентского приложения.
     */
    private void exit() {
        System.out.println("Завершение программы.");
        System.exit(0);
    }
}
