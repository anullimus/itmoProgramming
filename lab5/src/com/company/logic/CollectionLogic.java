package com.company.logic;


import com.company.data.initial.*;
import com.company.data.secretDontOpen.SecretExit;


import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

/**
 * Collection class manages the elements of collections with lab work{@link LabWork}.
 */
public class CollectionLogic {
    private final CommandInformer commandInformer;
    private FileLogic fileManager;
    private String[] userCommand;
    private final Scanner commandReader;
    private BufferedReader br;
    private boolean isScript;
    private LocalDate creationTimeOfCollection;
    private LinkedHashSet<LabWork> collection;
    private final HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    {
        this.nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        this.userCommand = new String[]{""};
        this.commandInformer = new CommandInformer();
        this.isScript = false;
        this.commandReader = new Scanner(System.in);
    }

    public CollectionLogic() {
        System.out.println(CommandInformer.PS1 + "Вы забыли ввести путь к файлу коллекции в переменной окружения\n" +
                CommandInformer.PS1 + "Но ничего страшного, вы можете ввести его здесь: ");
        userCommand = commandReader.next().trim().split(" ");
        init(userCommand[0]);
    }

    public CollectionLogic(String collectionPath) {
        init(collectionPath);
    }

    /**
     * Finally initializes the constructions.
     */
    private void init(String collectionPath) {
        fileManager = new FileLogic(collectionPath);
        collection = fileManager.getCollection();
        creationTimeOfCollection = LocalDate.now();
    }

    /**
     * Opens interactive mode for manage the collection.
     */
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
                        info();
                        break;
                    case "show":
                        show();
                        break;
                    case "add":
                        add(elementInputer());
                        break;
                    case "update_id":
                        updateId(userCommand[1]);
                        break;
                    case "remove_by_id":
                        removeById(userCommand[1]);
                        break;
                    case "clear":
                        clear();
                        break;
                    case "save":
                        fileManager.save(userCommand, collection);
                        break;
                    case "execute_script":
                        executeScript();
                        break;
                    case "exit":
                        break;
                    case "add_if_min":
                        addIfMin();
                        break;
                    case "remove_greater":
                        removeGreater(userCommand[1]);
                        break;
                    case "remove_lower":
                        removeLower(userCommand[1]);
                        break;
                    case "max_by_author":
                        maxByAuthor();
                        break;
                    case "count_less_than_author":
                        countLessThanAuthor(userCommand[1]);
                        break;
                    case "filter_by_difficulty ":
                        filterByDifficulty(userCommand[1]);
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
                exit();
            }
        } catch (NoSuchElementException exception) {
            System.out.println(CommandInformer.PS1
                    + "Возникла непредвиденная ошибка. Программа остановлена, обратитесь в поддержку.");
            fileManager.makeDump(userCommand, collection);
//            exception.printStackTrace();
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException exception) {
            if (!isScript) {
                System.out.println("Возможно вы забыли добавить аргумент рядом с командой.");
                interactiveMode();
            }
        }
    }

    /**
     * Prints full information about the command that manages the collection.
     */
    public void info() {
        try {
            String description = commandInformer.getCommands().get(userCommand[1]);
            if (description != null) {
                System.out.println(CommandInformer.PS1 + "Описание команды: " + description);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException exception) {
            System.out.println("Такой команды не существует.");
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println(this);
        }
    }

    /**
     * Prints all elements of the collection.
     */
    public void show() {
        if (userCommand.length != 1) {
            System.out.println("Такой команды не существует.");
        } else if (collection != null && collection.size() != 0) {
            System.out.println("---------------------------------------------");
            for (LabWork labWork : collection) {
                System.out.println(labWork);
                System.out.println();
            }
            System.out.println("---------------------------------------------");
        } else {
            System.out.println("Коллекция пуста.");
        }
    }

    /**
     * @return LabWork class object that was filled by user.
     */
    private LabWork elementInputer() {
        String nameOfLabWork = null, nameOfPerson = null, line = "";
        long xForCoordinates = 0;
        Coordinates coordinatesOfLab = null;
        Difficulty difficulty = null;
        LocalDate birthday = null;
        Country nationality = null;
        Location location = null;
        int yForCoordinates = 0, xForLocation = 0, zForLocation = 0;
        double yForLocation = 0;

        Float minimalPoint = null;

        try {
            if (isScript) {
                System.out.print(CommandInformer.PS1 + "Попытка добавить элемент в коллекцию...  ");

                try {
                    nameOfLabWork = br.readLine().trim();
//                nameOfLabWork += commandReader.nextLine();
                    xForCoordinates = Long.parseLong(br.readLine().trim());
                    yForCoordinates = Integer.parseInt(br.readLine().trim());
                    coordinatesOfLab = new Coordinates(xForCoordinates, yForCoordinates);
                    minimalPoint = Float.parseFloat(br.readLine().trim());
                    difficulty = Difficulty.valueOf(br.readLine().trim());

                    nameOfPerson = br.readLine().trim();
//                nameOfPerson += commandReader.nextLine();
                    birthday = LocalDate.parse(br.readLine().trim());
                    nationality = Country.valueOf(br.readLine().trim());
                    xForLocation = Integer.parseInt(br.readLine().trim());
                    yForLocation = Double.parseDouble(br.readLine().trim());
                    zForLocation = Integer.parseInt(br.readLine().trim());
                    location = new Location(xForLocation, yForLocation, zForLocation);
                    Person author = new Person(nameOfPerson, birthday, nationality, location);
                    return new LabWork(nameOfLabWork, coordinatesOfLab, minimalPoint, difficulty, author);
                } catch (Exception exception) {
                    System.out.println("Элемент не добавлен, так как в данных была ошибка.");
                }

            } else {
                System.out.println(CommandInformer.PS1 + "Введите название лабораторной работы: ");
                nameOfLabWork = commandReader.next().trim();
                nameOfLabWork += commandReader.nextLine();


                while (true) {
                    System.out.println(CommandInformer.PS1 + "Введите координату 'x' места написания лаб. работы: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лаб. работы.");
                            break;
                        }
                        Long.parseLong(line);
                        break;
                    } catch (NumberFormatException exception) {
                        System.out.println("Неправильный аргумент. Параметр 'x' - это целое число.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите координату 'y' места написания лаб. работы: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лаб. работы.");
                            break;
                        }
                        yForCoordinates = Integer.parseInt(line);
                        coordinatesOfLab = new Coordinates(xForCoordinates, yForCoordinates);
                        break;
                    } catch (NumberFormatException exception) {
                        System.out.println("Неправильный аргумент. Параметр 'y' - это положительное число.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите минимальный балл: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        minimalPoint = Float.parseFloat(line);
                        if (minimalPoint > 0) {
                            break;
                        } else {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Неправильный аргумент. " +
                                "Параметр 'минимальный балл' - это положительное число.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите уровень сложности: ("
                            + Arrays.toString(Difficulty.values()) + "): ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        difficulty = Difficulty.valueOf(line.toUpperCase());
                        break;
                    } catch (IllegalArgumentException exception) {
                        System.out.println("Введите строго один из следующих уровней сложности ("
                                + Arrays.toString(Difficulty.values()) + ").\n" +
                                "Или введите 'local_exit' для отмены режима добавления элемента. ");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                if (!line.equals("local_exit")) {
                    System.out.println("Далее вам необходимо заполнить информацию о человеке, написавшем эту лаб. работу.");

                    System.out.println(CommandInformer.PS1 + "Введите имя автора: ");
                    nameOfPerson = commandReader.next().trim();
                    nameOfPerson += commandReader.nextLine();
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите его дату рождения автора в формате: YYYY-MM-DD: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        birthday = LocalDate.parse(line);
                        break;
                    } catch (DateTimeException | NullPointerException exception) {
                        System.out.println("Неправильный аргумент. " +
                                "Параметр 'дату рождения' должен передоваться в формате: YYYY-MM-DD.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите национальность автора: ("
                            + Arrays.toString(Country.values()) + "): ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        nationality = Country.valueOf(line.toUpperCase());
                        break;
                    } catch (IllegalArgumentException exception) {
                        System.out.println("Введите строго одну из следующих стран("
                                + Arrays.toString(Country.values()) + ").\n" +
                                "Или введите 'local_exit' для отмены режима добавления элемента. ");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите координату 'x' текущей локации автора: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        xForLocation = Integer.parseInt(line);
                        break;
                    } catch (NumberFormatException exception) {
                        System.out.println("Неправильный аргумент. Параметр 'x' - это целое число.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите координату 'y' текущей локации автора: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        yForLocation = Double.parseDouble(line);
                        break;
                    } catch (NumberFormatException exception) {
                        System.out.println("Неправильный аргумент. Параметр 'y' - это целое число.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                while (!line.equals("local_exit")) {
                    System.out.println(CommandInformer.PS1 + "Введите координату 'z' текущей локации автора: ");
                    try {
                        line = commandReader.next().trim();
                        if (line.equals("local_exit")) {
                            System.out.println("Вы вышли из режима добавления новой лабы.");
                            break;
                        }
                        zForLocation = Integer.parseInt(line);
                        location = new Location(xForLocation, yForLocation, zForLocation);
                        break;
                    } catch (NumberFormatException exception) {
                        System.out.println("Неправильный аргумент. Параметр 'z' - это положительное число.\n" +
                                "Введите корректные данные или введите 'local_exit' отмены режима добавления элемента.");
                    } catch (NoSuchElementException exception) {
                        System.exit(1);
                    }
                }
                try {
                    Person author = new Person(nameOfPerson, birthday, nationality, location);
                    return new LabWork(nameOfLabWork, coordinatesOfLab, minimalPoint, difficulty, author);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
//        } catch (IOException exception) {
//            fileManager.makeDump(userCommand, collection);
//            System.out.println(CommandInformer.PS1 + "Связь с файл-скриптом потеряна. Элемент добавить не удалось. " +
//                    "Коллекция экстренно сохранена в файле dump.json");
//            System.exit(1);
        } catch (Exception exception) {
            System.out.println(CommandInformer.PS1 + "Возникла непредвиденная ошибка. Элемент добавить не удалось. " +
                    "Коллекция экстренно сохранена в файле dump.json");
            fileManager.makeDump(userCommand, collection);
            exception.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Adds new element to collection.
     *
     * @param newLabWork is an object of LabWork class that was filled by user.
     */
    public void add(LabWork newLabWork) {
        if (newLabWork != null) {
            collection.add(newLabWork);
            System.out.println(CommandInformer.PS1 + "Спасибо. Лабораторная работа добавлена в коллекцию.");
        } else {
            System.out.println("Аргумент newLabWork пришел в в неправильном виде!");
        }
    }

    /**
     * Updates id of element from collection.
     *
     * @param stringIdOfElement is a pre-int-converted id of one of the collection elements.
     */
    public void updateId(String stringIdOfElement) {
        try {
            int element = Integer.parseInt(stringIdOfElement);
            boolean wasChanged = false;
            for (LabWork labWork : collection) {
                if (labWork.getId() == element) {
                    labWork.changeId();
                    System.out.println(CommandInformer.PS1 + "Обновление id элемента прошло успешно.");
                    wasChanged = true;
                    break;
                }
            }
            if (!wasChanged) {
                System.out.println("Не нашлось ни одной лаб. работы с введенным id.");
            }
        } catch (NumberFormatException exception) {
            System.out.println("Неправильный аргумент. Необходимо ввести команду + id элемента в формате числа.");
        }
    }

    /**
     * Removes the element from collection by id.
     *
     * @param stringIdOfElement is a pre-int-converted id of one of the collection elements.
     */
    public void removeById(String stringIdOfElement) {
        try {
            int element = Integer.parseInt(stringIdOfElement);
            boolean wasRemoved = false;
            for (LabWork labWork : collection) {
                if (labWork.getId() == element) {
                    collection.remove(labWork);
                    System.out.println(CommandInformer.PS1 +
                            "Элемент удален. Оставшееся количество лаборатных работ в коллекции: " + collection.size());
                    wasRemoved = true;
                    break;
                }
            }
            if (!wasRemoved) {
                System.out.println("Не нашлось ни одного элемента с введенным id.");
            }
        } catch (NumberFormatException exception) {
            System.out.println("Неправильный аргумент. Необходимо ввести команду + id элемента в формате числа.");
        }
    }

    /**
     * Clears the collection.
     */
    public void clear() {
        try {
            String key = userCommand[1];
            if (key.equals("-f")) {
                collection.clear();
                System.out.println(CommandInformer.PS1
                        + "Коллекция очищена. Оставшееся количество элементов в коллекции: 0");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Такого ключа не существует.");
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println(CommandInformer.PS1
                    + "Подтвердите, что хотите удалить все лабораторные работы из коллекции. [y/n]");
            while (true) {
                String approve = null;
                if (!isScript) {
                    approve = commandReader.next().trim().toLowerCase(Locale.ROOT);
                } else {
                    try {
                        approve = br.readLine().trim();
                    } catch (IOException exception1) {
                        fileManager.makeDump(userCommand, collection);
                        exception1.printStackTrace();
                        System.exit(0);
                    }
                }
                try {
                    if (approve.equals("y") || approve.equals("yes")) {
                        collection.clear();
                        System.out.println(CommandInformer.PS1 + "Коллекция очищена. Оставшееся количество элементов " +
                                "в коллекции: 0");
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
                    br = new BufferedReader(new FileReader(scriptPath));
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        userCommand = line.trim().split(" ", 2);
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
        }nameOfFilesThatWasBroughtToExecuteMethod.clear();
    }

    /**
     * Ends the program.
     */
    public void exit() {
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
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Такого ключа не существует.");
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println(CommandInformer.PS1
                    + "Выход из программы будет выполнен без сохранения в файл. Вы уверены, "
                    + "что НЕ хотите сохранить коллекцию? [y/n]");
            while (true) {
                String approve = null;
                if (!isScript) {
                    approve = commandReader.next().trim().toLowerCase(Locale.ROOT);
                } else {
                    try {
                        approve = br.readLine().trim();
                    } catch (IOException exception1) {
                        fileManager.makeDump(userCommand, collection);
                        exception1.printStackTrace();
                        System.exit(0);
                    }
                }
                try {
                    if (approve.equals("y") || approve.equals("yes")) {
                        System.out.println(CommandInformer.PS1 + "До скорых встреч!");
                        System.exit(0);
                    } else if (approve.equals("n") || approve.equals("no")) {
                        fileManager.save(userCommand, collection);
                        System.out.println(CommandInformer.PS1 + "Коллекция экстренно сохранена в файл dump.csv \n" +
                                "До скорых встреч!");
                        System.exit(0);
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException exception1) {
                    System.out.println("Вы должны подтвердить действие одним из предложенных вариантов: [y/n]");
                }
            }
        }
    }

    /**
     * Adds new element to collection if it is minimal.
     */
    public void addIfMin() {
        LabWork labWork = elementInputer();
        if (collection.size() == 0) {
            System.out.println("Коллекция пуста.");
        } else {
            boolean isTheFewest = true;
            for (LabWork lw : collection) {
                if (labWork.getMinimalPoint().compareTo(lw.getMinimalPoint()) > 0) {
                    isTheFewest = false;
                    break;
                }
            }
            if (!isTheFewest) {
                System.out.println("Наименьший балл введенной лабораторной работы не является меньше " +
                        "минимального балла лабораторных из коллекции.");
            } else {
                add(labWork);
                System.out.println(CommandInformer.PS1
                        + "Наименьший балл введенной лабораторной работы является меньше "
                        + "минимального балла лабораторных из коллекции, "
                        + "поэтому лабораторная работа добавлена в коллекцию. Текущее количество элементов в коллекции: "
                        + collection.size());
            }
        }
    }

    /**
     * Removes the element from collection by id if it is the greatest.
     *
     * @param stringIdOfElement is a pre-int-converted id of one of the collection elements.
     */
    public void removeGreater(String stringIdOfElement) {
        if (collection.size() == 0) {
            System.out.println("Коллекция пуста.");
        } else {
            Float minimalPoint = null;
            long element = Long.parseLong(stringIdOfElement);
            for (LabWork labWork : collection) {
                if (labWork.getId() == element) {
                    minimalPoint = labWork.getMinimalPoint();
                    break;
                }
            }
            if (minimalPoint == null) {
                System.out.println("Лабораторная работа с введенным id не найдена.");
            } else {
                boolean wasAtLeastOneLesser = false;
                for (LabWork ticket : collection) {
                    if (ticket.getMinimalPoint().compareTo(minimalPoint) > 0) {
                        wasAtLeastOneLesser = true;
                        collection.remove(ticket);
                    }
                }
                if (!wasAtLeastOneLesser) {
                    System.out.println("Все лабораторные работы в коллекции сделаны на более высокие баллы, чем " +
                            "введенная лабораторная работа.");
                } else {
                    System.out.println(CommandInformer.PS1 + "Лаб. работы, сделанные на баллы больше, чем " + minimalPoint +
                            " удалены из коллекции. Оставшееся количество элементов в коллекции: " + collection.size());
                }
            }
        }
    }

    /**
     * Removes the element from collection by id if it is the minimal.
     *
     * @param stringIdOfElement is a pre-int-converted id of one of the collection elements.
     */
    public void removeLower(String stringIdOfElement) {
        if (collection.size() == 0) {
            System.out.println("Коллекция пуста.");
        } else {
            Float minimalPoint = null;
            long element = Long.parseLong(stringIdOfElement);
            for (LabWork ticket : collection) {
                if (ticket.getId() == element) {
                    minimalPoint = ticket.getMinimalPoint();
                    break;
                }
            }
            if (minimalPoint == null) {
                System.out.println("Лабораторная работа с введенным id не найдена.");
            } else {
                boolean wasAtLeastOneLesser = false;
                for (LabWork ticket : collection) {
                    if (ticket.getMinimalPoint().compareTo(minimalPoint) < 0) {
                        wasAtLeastOneLesser = true;
                        collection.remove(ticket);
                    }
                }
                if (!wasAtLeastOneLesser) {
                    System.out.println("Все лабораторные работы в коллекции сделаны на более низкие баллы, чем " +
                            "введенная лабораторная работа.");
                } else {
                    System.out.println(CommandInformer.PS1 + "Лаб. работы, сделанные на баллы меньше, чем " + minimalPoint +
                            " удалены из коллекции. Оставшееся количество элементов в коллекции: " + collection.size());
                }
            }
        }
    }

    /**
     * Prints the oldest author of any lab from collection.
     */
    public void maxByAuthor() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            LabWork[] arr = new LabWork[collection.size()];
            arr = collection.toArray(arr);
            LabWork lwWithOldestAuthor = arr[0];
            for (LabWork lw : collection) {
                if (lwWithOldestAuthor.getAuthor().getBirthday().compareTo(lw.getAuthor().getBirthday()) < 0) {
                    lwWithOldestAuthor = lw;
                }
            }
            System.out.println(CommandInformer.PS1
                    + "Лабораторная работа, написанная самым старым автором: " + lwWithOldestAuthor);
        }
    }

    /**
     * Prints the all authors who was born earlier than input author.
     *
     * @param stringAuthor is a name of one of the lab works authors.
     */
    public void countLessThanAuthor(String stringAuthor) {
        if (userCommand.length != 1) {
            System.out.println("Такой команды не существует.");
        } else if (collection.size() == 0) {
            System.out.println("Коллекция пуста.");
        } else {
            Person author = null;
            for (LabWork lw : collection) {
                if (lw.getName().equals(stringAuthor))
                    author = lw.getAuthor();
            }
            if (author != null) {
                long countOfAuthors = 0;
                for (LabWork lw : collection) {
                    if (lw.getAuthor().getBirthday().compareTo(author.getBirthday()) < 0) {
                        countOfAuthors++;
                    }
                }
                System.out.println(CommandInformer.PS1
                        + "Количество лабораторных работ, авторы которых родились раньше введенного автора: "
                        + countOfAuthors);
            } else {
                System.out.println("Автор не найден.");
            }
        }
    }

    /**
     * Sorts the elements of the collection by their difficulty.
     *
     * @param stringDifficult is a degree of difficult of one of the lab works in collection.
     */
    public void filterByDifficulty(String stringDifficult) {
        try {
            if (stringDifficult == null) {
                throw new IllegalArgumentException();
            } else if (collection.size() == 0) {
                throw new NullPointerException();
            } else {
                boolean wasAtLeastOneThisTypeOfElementInCollection = false;
                for (LabWork lw : collection) {
                    if (lw.getDifficulty().equals(Difficulty.valueOf(stringDifficult))) {
                        System.out.println(lw);
                        wasAtLeastOneThisTypeOfElementInCollection = true;
                        break;
                    }
                }
                if (wasAtLeastOneThisTypeOfElementInCollection) {
                    System.out.println(CommandInformer.PS1 + "Выведены все лаб. работы типа " + stringDifficult + ".");
                } else {
                    System.out.println(CommandInformer.PS1
                            + "В коллекции не нашлось ни одного элемента с заданным типом");
                }
            }
        } catch (NullPointerException exception) {
            System.out.println(CommandInformer.PS1 + "Коллекция пуста.");
        } catch (IllegalArgumentException exception) {
            System.out.println(CommandInformer.PS1
                    + "Неправильный аргумент, попробуйте ввести команду еще раз.");
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(commandInformer, creationTimeOfCollection, commandReader, isScript, br, collection);
        result = 31 * result + Arrays.hashCode(userCommand);
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        CollectionLogic that = (CollectionLogic) otherObject;
        return isScript == that.isScript && Objects.equals(commandInformer, that.commandInformer)
                && Arrays.equals(userCommand, that.userCommand) && Objects.equals(creationTimeOfCollection, that.creationTimeOfCollection)
                && Objects.equals(commandReader, that.commandReader)
                && Objects.equals(br, that.br)
                && Objects.equals(collection, that.collection);
    }

    @Override
    public String toString() {
        StringBuilder collectionInfo = new StringBuilder(CommandInformer.PS1);
        collectionInfo.append("Тип коллекции: ").append(collection.getClass()).
                append("\n Дата инициализации: ").append(creationTimeOfCollection).
                append("\n Количество элементов: ").append(collection.size());
        return collectionInfo.toString();
    }
}