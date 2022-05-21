package com.company.people;

import com.company.exceptions.IncorrectNameException;

public abstract class Human implements Lifeable {
    private String name;

    public Human(String name) {
        if (name == null) {
            throw new IllegalArgumentException("⛔Аргумент не может быть null!⛔");
        }
        this.name = name;
    }

    @Override
    public String getNameForDialogue() {
        return name.concat(": ");
    }
    @Override
    public String getName() {
        return name;
    }

    public void setName(String newName) throws IncorrectNameException {
        this.name = newName;
    }

    public String enterMove() {
        return "входит в чат.\n";
    }

    public abstract boolean canNotMove();
}
