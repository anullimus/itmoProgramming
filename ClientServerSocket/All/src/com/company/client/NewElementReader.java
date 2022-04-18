package com.company.client;

import com.company.server.CommandInformer;
import com.company.common.data.initial.*;
import com.company.common.exception.ReadElementFromScriptException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;


public class NewElementReader {
    private final Scanner scanner;

    {
        scanner = new Scanner(System.in);
    }

    public LabWork readNewLabwork() {
        try {
            if (App.IS_SCRIPT) {
                return readNewElementFromScript();
            } else {
                return readNewElementFromConsole();
            }

        } catch (Exception exception) {
            System.out.println("Возникла непредвиденная ошибка при чтении элемента");
            return null;
        }
    }

    /**
     * @return new Lab work read from console
     */
    private LabWork readNewElementFromConsole() {
        Person author = new Person(readNameOfCreator(), readBirthdayOfCreator(), readCountry(), readLocation());
        return new LabWork(readNameOfLabwork(), readCoordinates(), readMinPoints(), readDifficulty(), author);
    }

    /**
     * @return new Lab work read from script
     */
    private LabWork readNewElementFromScript() {
        try {
            String[] elementValueList = App.USER_COMMAND[1].split(";");
            System.out.print(CommandInformer.PS1 + "Попытка добавить элемент в коллекцию...  ");

            String nameOfNewElement = elementValueList[0];

            long xForCoordinates = Long.parseLong(elementValueList[1]);
            int yForCoordinates = Integer.parseInt(elementValueList[2]);
            Coordinates coordinatesOfLab = new Coordinates(xForCoordinates, yForCoordinates);

            float minimalPoint = Float.parseFloat(elementValueList[3]);
            Difficulty difficulty = Difficulty.valueOf(elementValueList[4]);

            String nameOfPerson = elementValueList[5];
            LocalDate birthday = LocalDate.parse(elementValueList[6]);
            Country nationality = Country.valueOf(elementValueList[7]);

            int xForLocation = Integer.parseInt(elementValueList[8]);
            double yForLocation = Double.parseDouble(elementValueList[9]);
            int zForLocation = Integer.parseInt(elementValueList[10]);
            Location location = new Location(xForLocation, yForLocation, zForLocation);

            Person author = new Person(nameOfPerson, birthday, nationality, location);
            LabWork labWork = new LabWork(nameOfNewElement, coordinatesOfLab, minimalPoint, difficulty, author);
            labWork.changeId();
            System.out.println("Элемент успешно добавлен.");
            return labWork;
        } catch (Exception exception) {
            throw new ReadElementFromScriptException("Ошибка при попытке чтения элемента из скрипта. " +
                    "Проверьте правильность данных", exception);
        }
    }

    private String readNameOfLabwork() {
        System.out.println(CommandInformer.PS1 + "Введите название лабораторной работы: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Поле 'название лабораторной работы' не может быть пустым, повторите попытку: ");
            name = scanner.nextLine().trim();
        }
        return name;
    }

    private Coordinates readCoordinates() {
        long x;
        int y;
        System.out.println(CommandInformer.PS1 + "Введите координату 'x' места написания лаб. работы: ");
        x = MyValidator.readLong();
        System.out.println(CommandInformer.PS1 + "Введите координату 'y' места написания лаб. работы: ");
        y = MyValidator.readInt();
        return new Coordinates(x, y);
    }

    private Location readLocation() {
        int x;
        double y;
        int z;
        System.out.println(CommandInformer.PS1 + "Введите координату 'x' текущей локации автора: ");
        x = MyValidator.readInt();
        System.out.println(CommandInformer.PS1 + "Введите координату 'y' текущей локации автора: ");
        y = MyValidator.readDouble();
        System.out.println(CommandInformer.PS1 + "Введите координату 'z' текущей локации автора: ");
        z = MyValidator.readInt();
        return new Location(x, y, z);
    }

    private Float readMinPoints() {
        System.out.println(CommandInformer.PS1 + "Введите минимальный балл, который можно получить за lab work: ");
        float minPoints = MyValidator.readFloat();
        while (minPoints < 0) {
            System.out.println("Неправильный аргумент. Введите положительное число: ");
            minPoints = MyValidator.readFloat();
        }
        return minPoints;
    }

    private Difficulty readDifficulty() {
        System.out.println(CommandInformer.PS1 + "Введите один из следующих уровней сложности: ("
                + Arrays.toString(Country.values()) + "): ");
        String difficulty = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        while (!Arrays.toString(Difficulty.values()).contains(difficulty)) {
            System.out.println(MyValidator.getStandardErrorMessage());
            difficulty = scanner.nextLine().trim();
        }
        return Difficulty.valueOf(difficulty);
    }

    private String readNameOfCreator() {
        System.out.println(CommandInformer.PS1 + "Введите имя автора: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Поле 'имя автора' не может быть пустым, повторите попытку: ");
            name = scanner.nextLine().trim();
        }
        return name;
    }

    private LocalDate readBirthdayOfCreator() {
        LocalDate birthday;
        System.out.println(CommandInformer.PS1 + "Введите его дату рождения автора в формате: YYYY-MM-DD: ");
        while (true) {
            try {
                birthday = LocalDate.parse(scanner.nextLine().trim());
                break;
            } catch (DateTimeException | NullPointerException exception) {
                System.out.println(MyValidator.getStandardErrorMessage());
            }
        }
        return birthday;
    }

    private Country readCountry() {
        System.out.println(CommandInformer.PS1 + "Введите одну из предложенных стран проживания автора: ("
                + Arrays.toString(Country.values()) + "): ");
        String country = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        while (!Arrays.toString(Country.values()).contains(country)) {
            System.out.println(MyValidator.getStandardErrorMessage());
            country = scanner.nextLine().trim();
        }
        return Country.valueOf(country);
    }
}

