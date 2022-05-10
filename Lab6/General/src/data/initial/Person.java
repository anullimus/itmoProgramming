package data.initial;

import serverLogic.Tool;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.Objects;

/**
 * Objects of the Person class are authors of lab work{@link LabWork}.
 */
public class Person implements Comparable<Person>, Serializable {
    private String name;
    private LocalDate birthday;
    private Country nationality;
    private Location location;

    public Person(String name, LocalDate birthday, Country nationality, Location location) {
        try {
            if (name != null && !name.isEmpty() && birthday != null && nationality != null && location != null) {
                this.name = name;
                this.birthday = birthday;
                this.nationality = nationality;
                this.location = location;

            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException exception) {
            System.out.println(Tool.PS1 + "Вы ввели некорректные данные и попали туда, куда не стоило " +
                    "попадать, поэтому на этой сессии вы получаете бан.");
            exception.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * @return Name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Birthday of the person.
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * @return Nationality of the person.
     */
    public Country getNationality() {
        return nationality;
    }

    /**
     * @return Current location of the person.
     */
    public Location getLocation() {
        return location;
    }


    @Override
    public int compareTo(Person otherPerson) {
        int result = this.getBirthday().compareTo(otherPerson.getBirthday());
        if (result == 0) {
            result = this.getLocation().compareTo(otherPerson.getLocation());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof Person)) return false;
        Person person = (Person) otherObject;
        return Objects.equals(name, person.name);
    }

    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append("Имя человека: ").append(name).
                append("\n Дата рождения: ").append(birthday).
                append("\n Национальность: ").append(nationality).
                append("\n Местоположение: ").append(location);
        return details.toString();
    }
}