package com.company.things;

public abstract class Indusrty implements Unlifeable {
    private final String type;

    public Indusrty(String type) {
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
