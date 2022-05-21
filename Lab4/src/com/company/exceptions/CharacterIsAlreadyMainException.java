package com.company.exceptions;

public class CharacterIsAlreadyMainException extends RuntimeException {
    private final String name;

    public CharacterIsAlreadyMainException(String name){
        this.name = name;
    }
    @Override
    public String getMessage(){
        return "⛔" + name + " уже является одним из главных героев!⛔";
    }
}
