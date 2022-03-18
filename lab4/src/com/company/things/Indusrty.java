package com.company.things;

import com.company.exceptions.IncorrectNameException;

public abstract class Indusrty implements Unlifeable {
    private final String type;

    public Indusrty(String type) {
        if (type == null) {
            throw new IllegalArgumentException("⛔Аргумент не может быть null!⛔");
        }
        this.type = type;
    }

    @Override
    public String getTypeForDialogue() {
        return type.concat(": ");
    }

    @Override
    public String getType(){
        return type;
    }

    public String amountText(){ return "Их количество - ";}

    public String enterMove() {
        return "входит в чат.\n";
    }

    public abstract boolean canMove();

}
