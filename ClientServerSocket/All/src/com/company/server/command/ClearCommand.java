package com.company.server.command;

import com.company.common.data.initial.LabWork;
import com.company.server.logic.CommandInformer;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Scanner;

public class ClearCommand implements Command<LinkedHashSet<LabWork>>{
    private final LinkedHashSet<LabWork> collection;
    private final String[] userCommand;
    private final boolean isScript;

    public ClearCommand(LinkedHashSet<LabWork> collection, String[] userCommand, Boolean isScript) {
        this.collection = collection;
        this.userCommand = userCommand;
        this.isScript = isScript;
    }

    @Override
    public LinkedHashSet<LabWork> execute() {
        try {
            String key = userCommand[1];
            if (key.equals("-f")) {
                collection.clear();
                System.out.println(CommandInformer.PS1 + "Коллекция очищена.");
                return collection;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Такого ключа не существует.");
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isScript) {
                System.out.println("В режиме скрипта очистка коллекции возможна только с помощью ключа -f.");
                return collection;
            }
            System.out.println(CommandInformer.PS1
                    + "Подтвердите, что хотите удалить все лабораторные работы из коллекции. [y/n]");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String approve = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                try {
                    if (approve.equals("y") || approve.equals("yes")) {
                        collection.clear();
                        System.out.println(CommandInformer.PS1 + "Коллекция очищена");
                        break;
                    } else if (approve.equals("n") || approve.equals("no")) {
                        System.out.println(CommandInformer.PS1 + "Очистка коллекции отменена.");
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException exception1) {
                    System.out.println("Вы должны подтвердить действие одним из предложенных вариантов: [y/n]");
                }
            }
        }
        return collection;
    }
}
