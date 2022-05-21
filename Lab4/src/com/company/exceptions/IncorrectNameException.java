package com.company.exceptions;

public class IncorrectNameException extends Exception {
    @Override
    public String getMessage() {
        return "⛔Ошибка! Имя должно включать в себя только буквы и пробелы!⛔";
    }
}