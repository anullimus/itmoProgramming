package com.company.people;

public abstract class Human implements Lifeable {
    private final String name;

    public Human(String name) {
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

    public String enterMove() {
        return "входит в чат.\n";
    }

    public abstract boolean canNotMove();
}
