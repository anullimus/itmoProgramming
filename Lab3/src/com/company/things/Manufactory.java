package com.company.things;

import java.util.Objects;

public class Manufactory extends Indusrty {
    private final String quality;
    private final String size;

    @Override
    public boolean canMove() {
        return false;
    }

    {
        quality = "Ее качества - ";
        size = "Огромная";
    }

    public Manufactory() {
        super("Спрутсовская мануфактура");
    }

    public String skills() {
        return "Выпускает несметные количества самых разнообразных тканей.\n";
    }

    public String getSize() {
        return size;
    }

    public String getQuality() {
        return quality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quality, size);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Manufactory other = (Manufactory) otherObject;
        return quality.equals(other.getQuality())
                && size.equals(other.getSize());
    }

    @Override
    public String toString() {
        if (canMove()) {
            return "Unlifable thing can't move, there is a problem...";
        } else {
            return new StringBuilder().append(getTypeForDialogue()).append(enterMove()).append(getTypeForDialogue())
                    .append(getQuality()).append(getSize()).append(".\n").append(getTypeForDialogue())
                    .append("Ее способности - ").append(this.skills()).toString();
        }
    }

}
