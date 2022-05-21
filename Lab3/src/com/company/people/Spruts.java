package com.company.people;

import com.company.things.Good;

import java.util.Objects;

public class Spruts extends Human {
    private final int amountBusiness;

    Business business = new Business();

    @Override
    public boolean canNotMove() {
        return false;
    }

    public Spruts() {
        super("Господин Спрутс");
        amountBusiness = 3;
    }

    public String getFunds() {
        return business.getFunds();
    }

    public String trade() {
        return "Получает навык - Торговля.\n";
    }

    public String getAmountBusinessText() {
        return business.getAmountBusinessText();
    }

    public int getAmountBusiness() {
        return amountBusiness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountBusiness);  // объединение значений
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Spruts other = (Spruts) otherObject;
        return amountBusiness == other.getAmountBusiness();
    }

    @Override
    public String toString() {
        if (canNotMove()) {
            return "Lazy...";
        } else {
            return new StringBuilder().append(getNameForDialogue()).append(enterMove())
                    .append(this.getNameForDialogue()).append(this.trade()).append(this.getNameForDialogue())
                    .append(getFunds()).append(Good.COTTON.getTranslation()).append(", ")
                    .append(Good.SUGAR_BEET.getTranslation()).append(", ").append(Good.MOON_RYE.getTranslation())
                    .append(" и ").append(Good.WHEAT.getTranslation()).append(".\n").append(this.getNameForDialogue())
                    .append(getAmountBusinessText()).append(getAmountBusiness()).append(".\n").toString();
        }
    }
}
