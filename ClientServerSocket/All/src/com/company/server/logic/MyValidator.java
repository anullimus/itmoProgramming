package com.company.server.logic;

import java.util.Scanner;

public class MyValidator {
    private static final Scanner scanner;
    private static final String standardErrorMessage;
    static {
        scanner = new Scanner(System.in);
        standardErrorMessage = "Ошибка при вводе, повторите попытку: ";
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

    public static String getStandardErrorMessage(){
        return standardErrorMessage;
    }
}
