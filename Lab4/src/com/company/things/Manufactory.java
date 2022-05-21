package com.company.things;

import java.util.Objects;

public class Manufactory extends Indusrty {
    public static final String TYPE = "Спрутсовская мануфактура";
    private final String quality;
    private final String size;

    @Override
    public boolean canMove() {
        return false;
    }

    public Manufactory() {
        super("Спрутсовская мануфактура");
        quality = "Ее качества - ";
        size = "Огромная";
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
        return quality.equals(other.quality)
                && size.equals(other.size);
    }

    @Override
    public String toString() {
        if (canMove()) {
            return "Unlivable thing can't move, there is a problem...";
        } else {
            return new StringBuilder().append(getTypeForDialogue()).append(enterMove()).append(getTypeForDialogue())
                    .append(getQuality()).append(getSize()).append(".\n").append(getTypeForDialogue())
                    .append("Ее способности - ").append(this.skills()).toString();
        }
    }

}
