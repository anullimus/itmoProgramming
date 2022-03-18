package com.company.people;

import com.company.exceptions.IncorrectNameException;
import com.company.exceptions.CharacterIsAlreadyMainException;
import com.company.things.*;

import java.util.Objects;

public class Shorty extends Human {
    private final String amountShortyText, typeOfBusy;
    private final String manufactoryType, sugarFactoryType;
    private final int amountShorty;
    private boolean isTheMainCharacter = false;
    private final Status status;

    {
        amountShortyText = "Количество существ - ";
        typeOfBusy = "Тип занятости вида 'Объект <- что добывают на Латифундии для этого объекта':\n";
    }
    public Shorty(Human employer, Manufactory manufactoryType, SugarFactory sugarFactoryType) {
        super("Коротышки");
        amountShorty = 1000;
        status = new Status("Статус - ", "работают на человека, по имени: ", employer.getName());
        this.manufactoryType = manufactoryType.getType();
        this.sugarFactoryType = sugarFactoryType.getType();
    }

    public static class Status {
        private final String statusText;
        private final String status;
        private final String employer;

        public Status(String statusText, String status, String employer) {
            this.statusText = statusText;
            this.status = status;
            this.employer = employer;
        }

        public String getStatusText() {
            return statusText;
        }

        public String getStatus() {
            return status.concat(employer).concat(".\n");
        }

        @Override
        public int hashCode() {
            return Objects.hash(statusText, status);  // объединение значений
        }

        @Override
        public boolean equals(Object otherObj) {
            if (this == otherObj) {
                return true;
            }
            if (otherObj == null) {
                return false;
            }
            if (!(otherObj instanceof Shorty.Status)) {
                return false;
            }
            Shorty.Status other = (Shorty.Status) otherObj;
            return statusText.equals(other.statusText)
                    && status.equals(other.status);
        }

        @Override
        public String toString() {
            return "Here was called static nestedClass";
        }

    }

    public String whatOthersSayAboutThisShorty(int earnedFertings){
        String sayings = "Конкретная коротышка с капиталом в " + earnedFertings +
                " фертингов: Об этой Коротышке говорят, что она стоит ";

        switch (earnedFertings) {
            case 1000: return sayings + "тысячу фертингов.\n";
            case 100: return sayings + "стоит сотняжку.\n";
            case 0: return sayings + "совсем ничего. Также на нее смотрят с презрением и считают ее дрянью.\n";
            default: return sayings + "неизвестное количество фертингов.\n";
        }
    }

    public void makeTheMainCharacter() {
        PositionChanger.tryChangePositionForMultipleCharacter(getName());
        if (isTheMainCharacter) {
            throw new CharacterIsAlreadyMainException(getName());
        }
        isTheMainCharacter = true;
        PositionChanger.changePositionForMultipleCharacter(getName());
    }

    public void setName(String newName) throws IncorrectNameException {
        NameChanger.tryChangeNameForMultipleCharacter(newName, getName());
        if (!newName.matches("[a-zA-Z ]*") || newName.isEmpty()) {
            throw new IncorrectNameException();
        }
        super.setName(newName);
        NameChanger.changeNameForMultipleCharacter(newName, getName());
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
        return amountShortyText.equals(other.amountShortyText)
                && amountShorty == other.amountShorty
                && typeOfBusy.equals(other.typeOfBusy)
                && isTheMainCharacter == other.isTheMainCharacter;
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
