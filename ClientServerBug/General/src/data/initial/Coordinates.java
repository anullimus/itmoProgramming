package data.initial;


import java.io.Serializable;
import java.util.Objects;

/**
 * Coordinates class for presenting where the lab work was actually have been written.
 */
public class Coordinates implements Serializable {
    private final Long x;
    private final int y;

    public Coordinates(Long x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return x-coordinate
     */
    public long getX() {
        return x;
    }

    /**
     * @return y-coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        Coordinates that = (Coordinates) otherObject;
        return Objects.equals(x, that.x) && Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        StringBuilder coordinates = new StringBuilder();
        coordinates.append(x).append("|").append(y);
        return coordinates.toString();
    }
}
