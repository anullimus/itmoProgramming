package com.company.things;

import java.util.Objects;

public class Latifundies extends Indusrty {
    private static final String TYPE = "Латифундии";
    private final String amount;
    private final Describable descriptionOfObject = () -> "с типом: Громаднейшие";
    Description description = new Description();

    @Override
    public boolean canMove() {
        return false;
    }

    public Latifundies() {
        super(TYPE);
        amount = "Несколько";
    }

    public String getAmountText() {
        return amountText();
    }

    public String getAmount() {
        return amount;
    }

    public String getDescriptionTitle() {
        return description.getDescriptionTitle();
    }

    public String getDescription() {
        return description.getDescriptionObject();
    }

    @Override
    public String enterMove() {
        return "входят в чат.\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(TYPE, amount);
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
//        if (this == otherObj) return true;
//        if (otherObj == null) return false;
//        if (getClass() != otherObj.getClass()) return false;
//        if (!(otherObj instanceof Latifundies)) return false;
        return TYPE.equals(other.getType())
                && amount.equals(other.getAmount());
    }

    @Override
    public String toString() {
        if (canMove()) {
            return "Unlifable thing can't move, there is a problem...";
        } else {
            return new StringBuilder().append(getTypeForDialogue()).append(enterMove()).append(getTypeForDialogue())
                    .append(getAmountText()).append(getAmount()).append(" штук.\n").append(getTypeForDialogue())
                    .append(getDescriptionTitle()).append(" ").append(descriptionOfObject.describe()).append(".\n")
                    .toString();
        }
    }
}
