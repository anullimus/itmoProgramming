package com.company.people;

import com.company.exceptions.IncorrectNameException;
import com.company.exceptions.CharacterIsAlreadyMainException;
import com.company.things.Good;

import java.util.Objects;

public class Spruts extends Human {
    private final int amountBusiness;
    private boolean isTheMainCharacter = true;
    private boolean isBusy = false;
    private final Business business;
    private String someVar;

    public Spruts() {
        super("Господин Спрутс");
        amountBusiness = 3;
        business = new Business("Средства торговли - ", "Количество бизнесов - ");
    }

    private Spruts(String newName) {
        super(newName);
        amountBusiness = 3;
        business = new Business("Средства торговли - ", "Количество бизнесов - ");
    }

    public class Business {
        private final String typeOfTrade;
        private final String amountBusinessText;

        //        private final String typeOfTrade12321 = Spruts.this.someVar;
        /*
        Вызвать поле/метод внешнего класса:
        Spruts.this.method();
         */
        public Business(String typeOfTrade, String amountBusinessText) {
            this.typeOfTrade = typeOfTrade;
            this.amountBusinessText = amountBusinessText;
//            Spruts.this.isBusy = true; если бы во вложенном классе тоже было поле isBusy
            isBusy = true;
        }

        private String getFunds() {
            return typeOfTrade;
        }

        private String getAmountBusinessText() {
            return amountBusinessText;
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeOfTrade, amountBusinessText);  // объединение значений
        }

        @Override
        public boolean equals(Object otherObj) {
            if (this == otherObj) {
                return true;
            }
            if (otherObj == null) {
                return false;
            }
            if (!(otherObj instanceof Spruts.Business)) {
                return false;
            }
            Spruts.Business other = (Spruts.Business) otherObj;
            return typeOfTrade.equals(other.typeOfTrade)
                    && amountBusinessText.equals(other.amountBusinessText);
        }

        @Override
        public String toString() {
            return "Here was called non-static nestedClass";
        }
    }

    @Override
    public boolean canNotMove() {
        return false;
    }

    public void makeTheMainCharacter() {
        PositionChanger.tryChangePositionForSingleCharacter(getName());
        if (isTheMainCharacter) {
            throw new CharacterIsAlreadyMainException(getName());
        }
        isTheMainCharacter = true;
        PositionChanger.changePositionForSingleCharacter(getName());
    }

    @Override
    public void setName(String newName) throws IncorrectNameException {
        NameChanger.tryChangeNameForSingleCharacter(newName, getName());
        if (!newName.matches("[a-zA-Z ]*") || newName.isEmpty()) {
            throw new IncorrectNameException();
        }
        super.setName(newName);
        NameChanger.changeNameForSingleCharacter(newName, getName());
    }


    public String getFunds() {
        if (isBusy) {
            return business.getFunds();
        } else {
            return getName() + " безработный";
        }
    }

    public String trade() {
        return "Получает навык - Торговля.\n";
    }

    public String getAmountBusinessText() {
        if (isBusy) {
            return business.getAmountBusinessText();
        } else {
            return getName() + " безработный";
        }
    }

    public int getAmountBusiness() {
        return amountBusiness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountBusiness, isTheMainCharacter, isBusy, business);  // объединение значений
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;       // 1
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;      // 2
        }
        // Вместо 1 и 2 можно   if (!(otherObject instanceof Spruts)) return false;  |при null inst... автоматом возвр. false|
        // если нам нужно, чтобы на вход подавался не только класс, но и его наследники
        Spruts other = (Spruts) otherObject;
        return amountBusiness == other.amountBusiness
                && isTheMainCharacter == other.isTheMainCharacter
                && isBusy == other.isBusy
                && Objects.equals(business, other.business);
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
