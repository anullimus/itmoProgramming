package com.company.data.initial;

import java.util.Objects;

/**
 * Location class for presenting location of the author{@link Person} of the lab work{@link LabWork}.
 */
public class Location implements Comparable<Location> {
    private final Integer x;
    private final Double y;
    private final Integer z;

    public Location(Integer x, Double y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return x-coordinate
     */
    public Integer getX() {
        return x;
    }

    /**
     * @return y-coordinate
     */
    public Double getY() {
        return y;
    }

    /**
     * @return z-coordinate
     */
    public Integer getZ() {
        return z;
    }


    @Override
    public int compareTo(Location otherLocation) {
        int result = this.getX().compareTo(otherLocation.getX());
        if (result == 0) {
            result = this.getZ().compareTo(otherLocation.getZ());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(x, location.x) && Objects.equals(y, location.y) && Objects.equals(z, location.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        StringBuilder location = new StringBuilder();
        location.append(x).append("|").append(y).append("|").append(z);
        return location.toString();
    }
}
