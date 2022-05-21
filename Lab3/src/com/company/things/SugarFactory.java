package com.company.things;

import java.util.Objects;

public class SugarFactory extends Indusrty {
    private final int amount;

    public SugarFactory() {
        super("Сахарные фабрики");
        amount = 30;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    public String getAmountText() {
        return amountText();
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String enterMove() {
        return "входят в чат.\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        SugarFactory other = (SugarFactory) otherObject;
        return amount == other.getAmount();
    }

    @Override
    public String toString() {
        if (canMove()) {
            return "Unlivable thing can't move, there is a problem...";
        } else {
            return new StringBuilder().append(getTypeForDialogue()).append(enterMove()).append(getTypeForDialogue())
                    .append(getAmountText()).append(getAmount()).append(" штук.\n").toString();
        }
    }

}
