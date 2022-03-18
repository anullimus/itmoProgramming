package com.company.labs.lab2.moves;

import ru.ifmo.se.pokemon.*;

public class Astonish extends PhysicalMove {
    public Astonish(){
        super(Type.GHOST, 30, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random() < 0.1) {
            Effect.flinch(p);
        }
    }
    @Override
    protected String describe() {
        return "Uses Astonish";
    }
}
