package clientLogic;

import com.company.server.CommandManager;
import com.company.common.exception.ReadElementFromScriptException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;


public class NewElementReader {
    private final Scanner scanner;
    private final boolean isScriptExecuting;

    public NewElementReader(Scanner scanner, boolean isScriptExecuting) {
        this.scanner = scanner;
        this.isScriptExecuting = isScriptExecuting;
    }

    /**
     * @return new Lab work read
     */
    private LabWork readNewLabwork() {
        Person author = new Person(readNameOfCreator(), readBirthdayOfCreator(), readCountry(), readLocation());
        return new LabWork(readNameOfLabwork(), readCoordinates(), readMinPoints(), readDifficulty(), author);
    }

    private String readNameOfLabwork() {
        System.out.println(CommandManager.PS1 + "Введите название лабораторной работы: ");
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
        System.out.println(CommandManager.PS1 + "Введите координату 'x' места написания лаб. работы: ");
        x = MyValidator.readLong();
        System.out.println(CommandManager.PS1 + "Введите координату 'y' места написания лаб. работы: ");
        y = MyValidator.readInt();
        return new Coordinates(x, y);
    }

    private Location readLocation() {
        int x;
        double y;
        int z;
        System.out.println(CommandManager.PS1 + "Введите координату 'x' текущей локации автора: ");
        x = MyValidator.readInt();
        System.out.println(CommandManager.PS1 + "Введите координату 'y' текущей локации автора: ");
        y = MyValidator.readDouble();
        System.out.println(CommandManager.PS1 + "Введите координату 'z' текущей локации автора: ");
        z = MyValidator.readInt();
        return new Location(x, y, z);
    }

    private Float readMinPoints() {
        System.out.println(CommandManager.PS1 + "Введите минимальный балл, который можно получить за lab work: ");
        float minPoints = MyValidator.readFloat();
        while (minPoints < 0) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Неправильный аргумент. Он не может быть меньше 0.");
            }
            System.out.println("Неправильный аргумент. Введите положительное число: ");
            minPoints = MyValidator.readFloat();
        }
        return minPoints;
    }

    private Difficulty readDifficulty() {
        System.out.println(CommandManager.PS1 + "Введите один из следующих уровней сложности: ("
                + Arrays.toString(Country.values()) + "): ");
        String difficulty = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        while (!Arrays.toString(Difficulty.values()).contains(difficulty)) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Неправильный аргумент сложности.");
            }
            System.out.println(MyValidator.getStandardErrorMessage());
            difficulty = scanner.nextLine().trim();
        }
        return Difficulty.valueOf(difficulty);
    }

    private String readNameOfCreator() {
        System.out.println(CommandManager.PS1 + "Введите имя автора: ");
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
        System.out.println(CommandManager.PS1 + "Введите его дату рождения автора в формате: YYYY-MM-DD: ");
        while (true) {
            try {
                birthday = LocalDate.parse(scanner.nextLine().trim());
                break;
            } catch (DateTimeException | NullPointerException exception) {
                if (isScriptExecuting) {
                    throw new ReadElementFromScriptException("Дата рождения введена неверно.");
                }
                System.out.println(MyValidator.getStandardErrorMessage());
            }
        }
        return birthday;
    }

    private Country readCountry() {
        System.out.println(CommandManager.PS1 + "Введите одну из предложенных стран проживания автора: ("
                + Arrays.toString(Country.values()) + "): ");
        String country = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
        while (!Arrays.toString(Country.values()).contains(country)) {
            if (isScriptExecuting) {
                throw new ReadElementFromScriptException("Неправильный аргумент страны.");
            }
            System.out.println(MyValidator.getStandardErrorMessage());
            country = scanner.nextLine().trim();
        }
        return Country.valueOf(country);
    }
}

