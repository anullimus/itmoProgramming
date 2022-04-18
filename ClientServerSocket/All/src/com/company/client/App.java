package com.company.client;

import com.company.server.logic.CollectionManager;
import com.company.server.logic.CommandInformer;

import java.io.*;
import java.util.HashSet;
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
     *
     * @param args command line values
     */
    private final CommandInformer commandInformer;
    private String[] userCommand;
    private final Scanner commandReader;
    private boolean isScript;
    private final CollectionManager collectionManager;

    public App(CollectionManager collectionManager) {
        System.out.println("Добро пожаловать в обитель моей лабы 5. Здесь вы можете ознакомиться с коллекцией LabWorks.");
//        CollectionManager collectionManager = new CollectionManager(System.getenv("VARRY"));
        this.collectionManager = collectionManager;

    }

    private final HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    {
        this.nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        this.userCommand = new String[]{""};
        this.commandInformer = new CommandInformer();
        this.isScript = false;
        this.commandReader = new Scanner(System.in);
    }

    public void interactiveMode() {
        try {
            while (!userCommand[0].equals("exit")) {
                if (!isScript) {
                    System.out.print(CommandInformer.PS2);
                    userCommand = commandReader.nextLine().trim().split(" ", 2);
                }
                switch (userCommand[0]) {
                    case "":
                        break;
                    case "help":
                        commandInformer.help(userCommand);
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
                        collectionManager.updateId(userCommand[1]);
                        break;
                    case "remove_by_id":
                        collectionManager.removeById(userCommand[1]);
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
                        break;
                    case "add_if_min":
                        collectionManager.addIfMin();
                        break;
                    case "remove_greater":
                        collectionManager.removeGreater(userCommand[1]);
                        break;
                    case "remove_lower":
                        collectionManager.removeLower(userCommand[1]);
                        break;
                    case "max_by_author":
                        collectionManager.maxByAuthor();
                        break;
                    case "count_less_than_author":
                        collectionManager.countLessThanAuthor(userCommand[1]);
                        break;
                    case "filter_by_difficulty ":
                        collectionManager.filterByDifficulty(userCommand[1]);
                        break;
                    default:
                        System.out.println(CommandInformer.PS1 + "Такой команды не существует.");
                        break;
                }
                if (isScript) {
                    break;
                }
            }
            if (!isScript) {
                collectionManager.exit();
            }
        } catch (NoSuchElementException exception) {
            System.out.println(CommandInformer.PS1
                    + "Возникла непредвиденная ошибка. Программа остановлена, обратитесь в поддержку.");
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (!isScript) {
                System.out.println("Возможно вы забыли добавить аргумент рядом с командой.");
                interactiveMode();
            }
        }
    }

    /**
     * Executes the script.
     */
    public void executeScript() {
        String scriptPath = userCommand[1];
        if (nameOfFilesThatWasBroughtToExecuteMethod.contains(scriptPath)) {
            System.out.println("В файле присутствует конструкция, которая приводит к рекурсии!\n" +
                    "Выполнение скрипта приостановлено");
        } else {
            nameOfFilesThatWasBroughtToExecuteMethod.add(scriptPath);
            this.isScript = true;
            File file = new File(scriptPath);
            if (file.canRead()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(scriptPath));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        userCommand = line.trim().split(" ", 2);
                        if (userCommand[0].toLowerCase(Locale.ROOT).equals("add")) {
                            StringBuilder elementLine = new StringBuilder();
                            for (int i = 0; i < 11; i++) {
                                elementLine.append(bufferedReader.readLine().trim()).append(";");
                            }
                            userCommand = new String[]{"add", elementLine.toString()};
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
            isScript = false;
            interactiveMode();
            nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
        }
    }
}

