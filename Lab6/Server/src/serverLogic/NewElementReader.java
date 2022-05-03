package serverLogic;


import data.initial.*;
import exception.ReadElementFromScriptException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;


public class NewElementReader {
    private final Scanner scanner;
    private final boolean isScriptExecuting;
    private final String standardErrorMessage;

    public NewElementReader(boolean isScriptExecuting) {
        this.scanner = new Scanner(System.in);
        this.isScriptExecuting = isScriptExecuting;
        standardErrorMessage = "Ошибка при вводе, повторите попытку: ";
    }

    /**
     * @return new Lab work read
     */
    private LabWork readNewLabwork() {
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
                System.out.print(standardErrorMessage);
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
                System.out.print(standardErrorMessage);
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
                System.out.print(standardErrorMessage);
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
                System.out.print(standardErrorMessage);
            }
        }
        return value;
    }

    private String readNameOfLabwork() {
        System.out.println("Введите название лабораторной работы: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Название маршрута не может быть пустым");
            }
            System.out.print("Поле 'название лабораторной работы' не может быть пустым, повторите попытку: ");
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
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Неправильный аргумент. Он не может быть меньше 0.");
            }
            System.out.println("Неправильный аргумент. Введите положительное число: ");
            minPoints = readFloat();
        }
        return minPoints;
    }

    private Difficulty readDifficulty() {
        System.out.println(Tool.PS1 + "Введите один из следующих уровней сложности: ("
                + Arrays.toString(Country.values()) + "): ");
        String difficulty = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        while (!Arrays.toString(Difficulty.values()).contains(difficulty)) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Неправильный аргумент сложности.");
            }
            System.out.println(standardErrorMessage);
            difficulty = scanner.nextLine().trim();
        }
        return Difficulty.valueOf(difficulty);
    }

    private String readNameOfCreator() {
        System.out.println(Tool.PS1 + "Введите имя автора: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Поле 'имя автора' не может быть пустым.");
            }
            System.out.print("Поле 'имя автора' не может быть пустым, повторите попытку: ");
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
                if (isScriptExecuting) {
                    throw new ReadElementFromScriptException("Дата рождения введена неверно.");
                }
                System.out.println(standardErrorMessage);
            }
        }
        return birthday;
    }

    private Country readCountry() {
        System.out.println(Tool.PS1 + "Введите одну из предложенных стран проживания автора: ("
                + Arrays.toString(Country.values()) + "): ");
        String country = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        while (!Arrays.toString(Country.values()).contains(country)) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Неправильный аргумент страны.");
            }
            System.out.println(standardErrorMessage);
            country = scanner.nextLine().trim();
        }
        return Country.valueOf(country);
    }
}

