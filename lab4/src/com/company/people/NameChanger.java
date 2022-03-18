package com.company.people;

public class NameChanger {
    public static void tryChangeNameForSingleCharacter(String newName, String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· попытка изменить имя для персонажа ").append(NAME)
                .append(" на ").append(newName).append("..."));
    }
    public static void changeNameForSingleCharacter(String newName, String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· Succeed!✔ Персонажа теперь зовут ").append(NAME).append(".\n"));
    }
    public static void tryChangeNameForMultipleCharacter(String newName, String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· попытка изменить имя для персонажей ").append(NAME)
                .append(" на ").append(newName).append("..."));
    }
    public static void changeNameForMultipleCharacter(String newName, String NAME) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append("· Succeed!✔ Персонажей теперь зовут ").append(NAME).append(".\n"));
    }
}
