package com.company.people;

import com.company.things.Good;
import com.company.things.Indusrty;
import com.company.things.Manufactory;
import com.company.things.SugarFactory;

import java.util.Objects;

public class Shorty extends Human {
    private final String amountShortyText, typeOfBusy;
    private final String manufactoryType, sugarFactoryType;
    private final int amountShorty;
    //    private final String statusText;
//    private final String status; //сделать классом и пусть хранит 20, 21 строка для human ГОТОВО
    private final Status status;

    {
        amountShortyText = "Количество существ - ";
        amountShorty = 1000;
//        statusText = "Их статус - ";
//        status = "работают на Господина Спрутса.\n";
        typeOfBusy = "Тип занятости вида 'Объект <- что добывают на Латифундии для этого объекта':\n";
    }

    public Shorty(Human employer, Indusrty manufactoryType, Indusrty sugarFactoryType) {
        super("Коротышки");
        status = new Status(employer.getName());
        this.manufactoryType = manufactoryType.getType();
        this.sugarFactoryType = sugarFactoryType.getType();
    }

    public String getAmountShortyText() {
        return amountShortyText;
    }

    public int getAmountShorty() {
        return amountShorty;
    }

    public String getStatusText() {
        return status.getStatusText();
    }

    public String getStatus() {
        return status.getStatus();
    }

    public String getTypeOfBusy() {
        return typeOfBusy;
    }

    @Override
    public boolean canNotMove() {
        return false;
    }

    @Override
    public String enterMove() {
        return "входят в чат.\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountShortyText, amountShorty, typeOfBusy);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Shorty other = (Shorty) otherObject;
        return amountShortyText.equals(other.getAmountShortyText())
                && amountShorty == other.getAmountShorty()
                && typeOfBusy.equals(other.getTypeOfBusy());
    }

    @Override
    public String toString() {
        if (canNotMove()) {
            return "Lazy...";
        } else {
            return new StringBuilder().append(getNameForDialogue()).append(enterMove()).append(getNameForDialogue())
                    .append(getAmountShortyText()).append(getAmountShorty()).append(".\n").append(getNameForDialogue())
                    .append(getStatusText()).append(getStatus()).append(getNameForDialogue()).append(getTypeOfBusy())
                    .append("\t\t\t")
                    .append(manufactoryType).append(" <- ").append(Good.COTTON.translation)
                    .append(",\n").append("\t\t\t")
                    .append(sugarFactoryType).append(" <- ").append(Good.SUGAR_BEET.getTranslation())
                    .append(",\n").append("\t\t\t")
                    .append("Прочие товары для торговли:").append(" <- ").append(Good.MOON_RYE.getTranslation())
                    .append(", ").append(Good.WHEAT.getTranslation()).append(".\n").toString();
        }
    }
}
