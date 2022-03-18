package com.company.things;

import java.util.Objects;

public class Latifundies extends Indusrty {
    private final String amount;
//    private final Describable descriptionOfObject = () -> "с типом: Громаднейшие";
    /*
    В тот момент, когда мы пишем:
Describable descriptionOfObject = new Describable() {
};

внутри Java-машины происходит следующее:
Создается безымянный Java-класс, реализующий интерфейс Describable.
Компилятор, увидев такой класс, требует от тебя реализовать все методы интерфейса Describable.
Создается один объект этого класса.
     */

    @Override
    public boolean canMove() {
        return false;
    }

    public Latifundies() {
        super("Латифундии");
        amount = "Несколько";
    }

    public String getAmountText() {
        return amountText();
    }

    public String getAmount() {
        return amount;
    }

    public String getAppearanceTitle() {
        return "Описание - ";
    }

    public String getAppearance() {
        class Appearance {
            /*
Захват внешних переменных в локальных классах возможен при effectively final(переменные, которые ПО СМЫСЛУ константы)
            private String getAmountForExapmle = amount;
             */
            private String appearanceObject = "Земельные участки";

            public void setAppearanceObject(String appearanceObject) {
                this.appearanceObject = appearanceObject;
            }

            public String getAppearanceObject() {
                return appearanceObject;
            }
        }
        Appearance description = new Appearance();
        return description.getAppearanceObject();
    }
    public String getDesctiption(){
        Describable descriptionOfObject = new Describable() {
            @Override
            public String describe() {
                return "с типом: Громаднейшие";
            }
        };
        return descriptionOfObject.describe();
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
        Latifundies other = (Latifundies) otherObject;
        return amount.equals(other.amount);
    }


    @Override
    public String toString() {
        if (canMove()) {
            return "Unlivable thing can't move, there is a problem...";
        } else {
            return new StringBuilder().append(getTypeForDialogue()).append(enterMove()).append(getTypeForDialogue())
                    .append(getAmountText()).append(getAmount()).append(" штук.\n").append(getTypeForDialogue())
                    .append(getAppearanceTitle()).append(getAppearance()).append(" ").append(getDesctiption())
                    .append(".\n").toString();
        }
    }
}
