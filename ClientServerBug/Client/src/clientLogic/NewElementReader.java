package clientLogic;


import com.google.gson.*;
import data.initial.*;
import exception.ReadElementException;
import serverLogic.Tool;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;


public class NewElementReader {
    private final Scanner scanner;
    private final String standardErrorMessage;
    private final String standardErrorMessageForScript;
    private boolean isSriptExecuting;

    public NewElementReader() {
        this.scanner = new Scanner(System.in);
        this.isSriptExecuting = false;
        standardErrorMessage = "Ошибка при вводе, повторите попытку: ";
        standardErrorMessageForScript = "+1 ошибка при вводе";
    }

    /**
     * @return new Lab work read
     */
    public LabWork readNewLabwork(boolean isSriptExecuting) {
        this.isSriptExecuting = isSriptExecuting;
        Person author = new Person(readNameOfCreator(), readBirthdayOfCreator(), readCountry(), readLocation());
        return new LabWork(readNameOfLabwork(), readCoordinates(), readMinPoints(), readDifficulty(), author);
    }

    public int readInt() {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.err.print(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return value;
    }

    public long readLong() {
        long value;
        while (true) {
            try {
                value = Long.parseLong(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.err.print(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return value;
    }

    public double readDouble() {
        double value;
        while (true) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.err.print(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return value;
    }

    public float readFloat() {
        float value;
        while (true) {
            try {
                value = Float.parseFloat(scanner.nextLine());
                break;
            } catch (NumberFormatException | NullPointerException e) {
                System.err.print(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return value;
    }

    private String readNameOfLabwork() {
        System.out.println("Введите название лабораторной работы: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.err.print("Поле 'название лабораторной работы' не может быть пустым, повторите попытку: ");
            if (isSriptExecuting){
                System.err.println(standardErrorMessageForScript);;
            }
            name = scanner.nextLine().trim();
        }
        return name;
    }

    private Coordinates readCoordinates() {
        long x;
        int y;
        System.out.println(Tool.PS1 + "Введите координату 'x' места написания лаб. работы: ");
        x = readLong();
        System.out.println(Tool.PS1 + "Введите координату 'y' места написания лаб. работы: ");
        y = readInt();
        return new Coordinates(x, y);
    }

    private Location readLocation() {
        int x;
        double y;
        int z;
        System.out.println(Tool.PS1 + "Введите координату 'x' текущей локации автора: ");
        x = readInt();
        System.out.println(Tool.PS1 + "Введите координату 'y' текущей локации автора: ");
        y = readDouble();
        System.out.println(Tool.PS1 + "Введите координату 'z' текущей локации автора: ");
        z = readInt();
        return new Location(x, y, z);
    }

    private Float readMinPoints() {
        System.out.println(Tool.PS1 + "Введите минимальный балл, который можно получить за lab work: ");
        float minPoints = readFloat();
        while (minPoints < 0) {
            System.err.println("Неправильный аргумент. Введите положительное число: ");
            minPoints = readFloat();
        }
        return minPoints;
    }

    private String readNameOfCreator() {
        System.out.println(Tool.PS1 + "Введите имя автора: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.err.print("Поле 'имя автора' не может быть пустым, повторите попытку: ");
            if (isSriptExecuting){
                System.err.println(standardErrorMessageForScript);;
            }
            name = scanner.nextLine().trim();
        }
        return name;
    }

    private LocalDate readBirthdayOfCreator() {
        LocalDate birthday;
        System.out.println(Tool.PS1 + "Введите его дату рождения автора в формате: YYYY-MM-DD: ");
        while (true) {
            try {
                birthday = LocalDate.parse(scanner.nextLine().trim());
                break;
            } catch (DateTimeException | NullPointerException exception) {
                System.err.println(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return birthday;
    }

    private Country readCountry() {
        System.out.println(Tool.PS1 + "Введите одну из предложенных стран проживания автора: ("
                + Arrays.toString(Country.values()) + "): ");
        String country;
        while (true) {
            try {
                country = scanner.nextLine().trim();
                if (!country.equals("") & Arrays.toString(Country.values()).contains(country)) {
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                System.err.println(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return Country.valueOf(country);
    }

    private Difficulty readDifficulty() {
        System.out.println(Tool.PS1 + "Введите один из следующих уровней сложности: ("
                + Arrays.toString(Difficulty.values()) + "): ");
        String difficulty;
        while (true) {
            try {
                difficulty = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
                if (!difficulty.equals("") & Arrays.toString(Difficulty.values()).contains(difficulty)) {
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                System.err.println(standardErrorMessage);
                if (isSriptExecuting){
                    System.err.println(standardErrorMessageForScript);;
                }
            }
        }
        return Difficulty.valueOf(difficulty);
    }
}
