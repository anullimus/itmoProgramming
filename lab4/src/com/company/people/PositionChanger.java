package com.company.people;

public class PositionChanger {
    public static void tryChangePositionForSingleCharacter(String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· попытка сделать персонажа ").append(NAME)
                .append(" главным героем..."));
    }
    public static void changePositionForSingleCharacter(String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· Succeed!✔ ").append(NAME)
                .append(" теперь является одним из главных героев.\n"));
    }
    public static void tryChangePositionForMultipleCharacter(String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· попытка сделать персонажей ").append(NAME)
                .append(" главными героями..."));
        sb.setLength(0);
    }
    public static void changePositionForMultipleCharacter(String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· Succeed!✔ ").append(NAME)
                .append(" теперь являются одними из главных героев.\n"));
    }
}
