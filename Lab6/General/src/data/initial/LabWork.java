package data.initial;


import serverLogic.ServerConnection;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Main data class data.initial.LabWork. Objects of this class will be figured in <Strong>collection</Strong>.
 */
public class LabWork implements Comparable<LabWork> {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private final LocalDate creationDate;
    private Float minimalPoint;
    private Difficulty difficulty;
    private Person author;

    public LabWork(String name, Coordinates coordinates, Float minimalPoint, Difficulty difficulty, Person author) {
        this.id = generateID();
        this.creationDate = LocalDate.now();
        try {
            if (name != null && !name.isEmpty() && coordinates != null && minimalPoint > 0
                    && difficulty != null && author != null) {
                this.name = name;
                this.coordinates = coordinates;
                this.minimalPoint = minimalPoint;
                this.difficulty = difficulty;
                this.author = author;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException exception) {
            System.out.println(ServerConnection.PS1 + "Вы ввели некорректные данные и попали туда, куда не стоило " +
                    "попадать, поэтому на этой сессии вы получаете бан.");
            System.exit(0);
        }
    }
    private long generateID(){
        return this.hashCode() * 17L + 32;
    }

    /**
     * @return Id of the Lab work.
     */
    public Long getId() {
        return id;
    }

//    // need from class FileLogic when adding labWork to collection
//    public void setId(Long id) {
//        this.id = id;
//    }

    /**
     * Changes id of the Lab work.
     */
    public void changeId() {
        this.id = generateID();
    }

    /**
     * @return Name of the Lab work.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the Lab work.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Date of creation of of the Lab work.
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * @return Minimal point for the labWork that user can get
     */
    public Float getMinimalPoint() {
        return minimalPoint;
    }

    /**
     * @return Difficulty of the Lab work.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return Author of the Lab work.
     */
    public Person getAuthor() {
        return author;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof LabWork)) return false;
        LabWork labWork = (LabWork) otherObject;
        return Objects.equals(id, labWork.id);
    }

    @Override
    public int compareTo(LabWork ticketObject) {
        return creationDate.compareTo(ticketObject.getCreationDate());
    }

    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append("Лабораторная работа №: ").append(id).
                append("\n Название: ").append(name).
                append("\n Координаты(x|y): ").append(coordinates).
                append("\n Дата создания: ").append(creationDate).
                append("\n Минимальный балл: ").append(minimalPoint).
                append("\n Уровень сложности: ").append(difficulty).
                append("\n Имя автора работы: ").append(author.getName()).
                append("\n Дата его рождения: ").append(author.getBirthday()).
                append("\n Национальность: ").append(author.getNationality()).
                append("\n Местоположение(x|y|z): ").append(author.getLocation());
        return details.toString();
    }
}