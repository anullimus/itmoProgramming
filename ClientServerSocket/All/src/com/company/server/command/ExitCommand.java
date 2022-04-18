package com.company.server.command;

import com.company.common.data.secretDontOpen.SecretExit;
import com.company.server.logic.CommandInformer;

import java.util.Locale;
import java.util.Scanner;

public class ExitCommand implements Command<String> {
    private final String[] userCommand;
    private final boolean isScript;

    public ExitCommand(String[] userCommand, Boolean isScript) {
        this.userCommand = userCommand;
        this.isScript = isScript;
    }

    @Override
    public String execute() {
        try {
            String key = userCommand[1];
            if (key.equals("-f")) {
                System.out.println(CommandInformer.PS1 + "До скорых встреч!");
                System.exit(0);
            } else if (key.equals("-secret")) {
                SecretExit secretExit = new SecretExit();
                while (true) {
                    secretExit.passwdAsker();
                    if (secretExit.passwdChecker()) {
                        try {
                            secretExit.play();
                        } catch (Exception exception) {
                            secretExit.someProblemMessagePrint();
                        }
                        break;
                    } else {
                        secretExit.rejected();
                    }
                }
                System.out.println(CommandInformer.PS1 + "До скорых встреч!");
                System.exit(0);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException exception) {
            return "Такого ключа не существует.";
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (isScript) {
                return "В режиме скрипта очистка коллекции возможна только с помощью ключа -f.";
            }
            System.out.println(CommandInformer.PS1
                    + "Выход из программы будет выполнен без сохранения в файл. Вы уверены, "
                    + "что НЕ хотите сохранить коллекцию? [y/n]");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String approve = scanner.next().trim().toLowerCase(Locale.ROOT);
                try {
                    if (approve.equals("y") || approve.equals("yes")) {
                        System.out.println(CommandInformer.PS1 + "До скорых встреч!");
                        System.exit(0);
                    } else if (approve.equals("n") || approve.equals("no")) {
                        return "Выход без сохранения отменен.";
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException exception1) {
                    System.out.println("Вы должны подтвердить действие одним из предложенных вариантов: [y/n]");
                }
            }
        }
        return "Произошел тот случай, который не ожидал программист...";
    }
}
