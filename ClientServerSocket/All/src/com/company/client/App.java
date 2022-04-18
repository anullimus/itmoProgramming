package com.company.client;

import com.company.server.CollectionManager;
import com.company.server.CommandInformer;

import java.io.*;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Amir Khanov
 * @version 1.0
 * Main class for <Strong>run</Strong> the application.
 * <img src="doc-files" alt-":^"/>
 */
public class App {
    /**
     * Start point of the program
     */
    public static String[] USER_COMMAND = new String[]{""};
    private final Scanner commandReader;
    public static boolean IS_SCRIPT = false;
    private final CollectionManager collectionManager;

    public App(CollectionManager collectionManager) {
        System.out.println("Добро пожаловать в обитель моей лабы . Здесь вы можете ознакомиться с коллекцией LabWorks.");
//        CollectionManager collectionManager = new CollectionManager(System.getenv("VARRY"));
        this.collectionManager = collectionManager;

    }

    {
        commandReader = new Scanner(System.in);
    }

    public void interactiveMode() {
        try {
            while (!USER_COMMAND[0].equals("exit")) {
                if (!IS_SCRIPT) {
                    System.out.print(CommandInformer.PS2);
                    USER_COMMAND = commandReader.nextLine().trim().split(" ", 2);
                }
                switch (USER_COMMAND[0]) {
                    case "":
                        break;
                    case "help":
                        CommandInformer.help();
                        break;
                    case "info":
                        collectionManager.info();
                        break;
                    case "show":
                        collectionManager.show();
                        break;
                    case "add":
                        collectionManager.add();
                        break;
                    case "update_id":
                        collectionManager.updateId();
                        break;
                    case "remove_by_id":
                        collectionManager.removeById();
                        break;
                    case "clear":
                        collectionManager.clear();
                        break;
                    case "save":
                        collectionManager.save();
                        break;
                    case "execute_script":
                        executeScript();
                        break;
                    case "exit":
                        collectionManager.exit();
                        break;
                    case "add_if_min":
                        collectionManager.addIfMin();
                        break;
                    case "remove_greater":
                        collectionManager.removeGreater();
                        break;
                    case "remove_lower":
                        collectionManager.removeLower();
                        break;
                    case "max_by_author":
                        collectionManager.maxByAuthor();
                        break;
                    case "count_less_than_author":
                        collectionManager.countLessThanAuthor();
                        break;
                    case "filter_by_difficulty":
                        collectionManager.filterByDifficulty();
                        break;
                    default:
                        System.out.println(CommandInformer.PS1 + "Такой команды не существует.");
                        break;
                }
                if (IS_SCRIPT) {
                    break;
                }
            }
            if (!IS_SCRIPT) {
                collectionManager.exit();
            }
        } catch (NoSuchElementException exception) {
            System.out.println(CommandInformer.PS1
                    + "Возникла непредвиденная ошибка. Программа остановлена, обратитесь в поддержку.");
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (!IS_SCRIPT) {
                System.out.println("Возможно вы забыли добавить аргумент рядом с командой.");
                interactiveMode();
            }
        }
    }

    /**
     * Executes the script.
     */
    public void executeScript() {
        String scriptPath = USER_COMMAND[1];
        if (CollectionManager.nameOfFilesThatWasBroughtToExecuteMethod.contains(scriptPath)) {
            System.out.println("В файле присутствует конструкция, которая приводит к рекурсии!\n" +
                    "Выполнение скрипта приостановлено");
        } else {
            CollectionManager.nameOfFilesThatWasBroughtToExecuteMethod.add(scriptPath);
            IS_SCRIPT = true;
            File file = new File(scriptPath);
            if (file.canRead()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(scriptPath));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        USER_COMMAND = line.trim().split(" ", 2);
                        if (USER_COMMAND[0].toLowerCase(Locale.ROOT).equals("add")) {
                            StringBuilder elementLine = new StringBuilder();
                            for (int i = 0; i < 11; i++) {
                                elementLine.append(bufferedReader.readLine().trim()).append(";");
                            }
                            USER_COMMAND = new String[]{"add", elementLine.toString()};
                        }
                        interactiveMode();
                    }
                    System.out.println(CommandInformer.PS1 + "Исполнение скрипта завершено.");
                } catch (FileNotFoundException exception) {
                    System.out.println(CommandInformer.PS1 + "Файл-скрипт не найден.");
                } catch (IOException exception) {
                    System.out.println(CommandInformer.PS1
                            + "Возникла непредвиденная ошибка. Программа остановлена, обратитесь в поддержку.");
                    exception.printStackTrace();
                    System.exit(0);
                }
            } else {
                System.out.println(CommandInformer.PS1 + "У вас нет доступа к файл-скрипту.");
            }
            IS_SCRIPT = false;
            interactiveMode();
            CollectionManager.nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
        }
    }
}

