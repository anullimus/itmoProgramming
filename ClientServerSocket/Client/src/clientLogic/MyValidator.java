package clientLogic;


import data.initial.LabWork;
//import serverLogic.App;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Scanner;

public class MyValidator {
    private static final Scanner scanner;
    private static final String standardErrorMessage;
    private LinkedHashSet<LabWork> collection;
    private String stringResultOfValidation;

    static {
        scanner = new Scanner(System.in);
        standardErrorMessage = "Ошибка при вводе, повторите попытку: ";
    }

    {
        stringResultOfValidation = "Ответ не приходил";
    }

    public static int readInt() {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.out.print(standardErrorMessage);
            }
        }
        return value;
    }

    public static long readLong() {
        long value;
        while (true) {
            try {
                value = Long.parseLong(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.out.print(standardErrorMessage);
            }
        }
        return value;
    }

    public static double readDouble() {
        double value;
        while (true) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.out.print(standardErrorMessage);
            }
        }
        return value;
    }

    public static float readFloat() {
        float value;
        while (true) {
            try {
                value = Float.parseFloat(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.out.print(standardErrorMessage);
            }
        }
        return value;
    }

    public static String getStandardErrorMessage() {
        return standardErrorMessage;
    }

    public LinkedHashSet<LabWork> getCollection() {
        return collection;
    }

    public String getStringResultOfValidation() {
        return stringResultOfValidation;
    }

    public static boolean checkCommandFormat() {
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer();
        if (!commandAnalyzer.analyzeCommand(App.USER_COMMAND)) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    private static String checkAndGetCommandArgument(){
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer();
        if (!commandAnalyzer.analyzeCommand(App.USER_COMMAND)) {
            throw new IllegalArgumentException();
        }
        return commandAnalyzer.getCommandArgument();
    }


    public boolean checkAddCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            checkCommandFormat();

            collection.add(new NewElementReader().readNewLabwork());
            return true;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkAddIfMinCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            checkCommandFormat();

            LabWork labWork = new NewElementReader().readNewLabwork();
            for (LabWork lw : collection) {
                if (labWork.getMinimalPoint().compareTo(lw.getMinimalPoint()) < 0) {
                    System.out.println("Наименьший балл введенной лабораторной работы не является меньше " +
                            "минимального балла лабораторных из коллекции.");
                    return false;
                }
                collection.add(labWork);
                System.out.println(CommandManager.PS1
                        + "Наименьший балл введенной лабораторной работы является меньше "
                        + "минимального балла лабораторных из коллекции, "
                        + "поэтому лабораторная работа добавлена в коллекцию. Текущее количество элементов в коллекции: "
                        + collection.size());
                return true;
            }
            return false;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkLessThanAuthorCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            CommandAnalyzer commandAnalyzer = new CommandAnalyzer();
            if (!commandAnalyzer.analyzeCommand(App.USER_COMMAND)) {
                throw new IllegalArgumentException();
            }

            Person author = null;
            for (LabWork lw : collection) {
                if (lw.getAuthor().getName().equals(commandAnalyzer.getCommandArgument())) {
                    author = lw.getAuthor();
                }
            }
            if (author != null) {
                long countOfAuthors = 0;
                for (LabWork lw : collection) {
                    if (lw.getAuthor().getBirthday().compareTo(author.getBirthday()) < 0) {
                        countOfAuthors++;
                    }
                }
                stringResultOfValidation = CommandManager.PS1
                        + "Количество лабораторных работ, авторы которых родились раньше введенного автора: "
                        + countOfAuthors;
            } else {
                stringResultOfValidation = "Автор не найден.";

            }
            return true;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkFilterByDifficultyCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            boolean wasAtLeastOneThisTypeOfElementInCollection = false;
            for (LabWork lw : collection) {
                if (lw.getDifficulty().equals(Difficulty.valueOf(checkAndGetCommandArgument()
                        .toUpperCase(Locale.ROOT)))) {
                    System.out.println(lw);
                    wasAtLeastOneThisTypeOfElementInCollection = true;
                }
            }
            if (wasAtLeastOneThisTypeOfElementInCollection) {
                stringResultOfValidation = CommandManager.PS1 + "Выведены все лаб. работы типа " + App.USER_COMMAND[1] + ".";
            } else {
                stringResultOfValidation = CommandManager.PS1 + "В коллекции не нашлось ни одного элемента с заданным типом";
            }
            return true;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checMaxByAuthorCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            checkCommandFormat();

            LabWork[] arr = new LabWork[collection.size()];
            arr = collection.toArray(arr);
            LabWork lwWithOldestAuthor = arr[0];
            for (LabWork lw : collection) {
                if (lwWithOldestAuthor.getAuthor().getBirthday().compareTo(lw.getAuthor().getBirthday()) > 0) {
                    lwWithOldestAuthor = lw;
                }
            }
            stringResultOfValidation = "Лабораторная работа, написанная самым старым автором: " + lwWithOldestAuthor;
            return true;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkRemoveByIDCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            long id = Long.parseLong(checkAndGetCommandArgument());

            for (LabWork labWork : collection) {
                if (labWork.getId() == id) {
                    collection.remove(labWork);
                    System.out.println("Элемент с id = " + id + " успешно удален из коллекции.");
                }
            }
            System.out.println("Here is no lab work find with id=" + id);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Введеныные данные содержат неверный формат.");
            return false;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkUpdateIDCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        try {
            long id = Long.parseLong(checkAndGetCommandArgument());

            for (LabWork labWork : collection) {
                if (labWork.getId() == id) {
                    labWork.changeId();
                    System.out.println("Элемент с id = " + id + " успешно обновлен");                    return true;
                }
            }
            System.out.println("Here is no lab work find with id=" + id);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Введеныные данные содержат неверный формат.");
            return false;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkRemoveGreaterCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        float minimalPoint = -1;
        try {
            long id = Long.parseLong(checkAndGetCommandArgument());

            for (LabWork labWork : collection) {
                if (labWork.getId() == id) {
                    minimalPoint = labWork.getMinimalPoint();
                    break;
                }
            }if (minimalPoint == -1) {
                System.out.println("Лабораторная работа с введенным id не найдена.");
            } else {
                for (LabWork ticket : collection) {
                    if (ticket.getMinimalPoint().compareTo(minimalPoint) > 0) {
                        collection.remove(ticket);
                    }
                }
                System.out.println(CommandManager.PS1 + "Лаб. работы, сделанные на баллы больше, чем " + minimalPoint +
                        " удалены из коллекции. Оставшееся количество элементов в коллекции: " + collection.size());
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Введеныные данные содержат неверный формат.");
            return false;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean checkRemoveLowerCommand(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
        float minimalPoint = -1;
        try {
            long id = Long.parseLong(checkAndGetCommandArgument());

            for (LabWork labWork : collection) {
                if (labWork.getId() == id) {
                    minimalPoint = labWork.getMinimalPoint();
                    break;
                }
            }if (minimalPoint == -1) {
                System.out.println("Лабораторная работа с введенным id не найдена.");
            } else {
                for (LabWork ticket : collection) {
                    if (ticket.getMinimalPoint().compareTo(minimalPoint) < 0) {
                        collection.remove(ticket);
                    }
                }
                System.out.println(CommandManager.PS1 + "Лаб. работы, сделанные на баллы меньше, чем " + minimalPoint +
                        " удалены из коллекции. Оставшееся количество элементов в коллекции: " + collection.size());
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Введеныные данные содержат неверный формат.");
            return false;
        } catch (NullPointerException | IllegalArgumentException exception) {
            return false;
        }
    }



}
