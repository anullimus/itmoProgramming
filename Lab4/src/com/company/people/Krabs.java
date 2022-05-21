package com.company.people;

import com.company.exceptions.CharacterIsAlreadyMainException;
import com.company.exceptions.IncorrectNameException;

import java.util.Objects;

public class Krabs extends Human{
    private final String status;
    private boolean isTheMainCharacter = false;

    public Krabs(){
        super("Господин Крабс");
        status = "Главный управляющий";
    }

    @Override
    public boolean canNotMove() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Krabs other = (Krabs) otherObject;
        return status.equals(other.status) ;
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

    @Override
    public String toString() {
        if (canNotMove()) {
            return "Lazy...";
        } else {
            String statusText = "Его должность - ";
        return new StringBuilder().append(this.getNameForDialogue()).append(enterMove())
                .append(this.getNameForDialogue()).append(statusText).append(status).append(".\n").toString();
        }
    }
}
