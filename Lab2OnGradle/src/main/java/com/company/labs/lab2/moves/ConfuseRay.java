package com.company.labs.lab2.moves;

import ru.ifmo.se.pokemon.*;

public class ConfuseRay extends StatusMove {
    public ConfuseRay(){
        super(Type.GHOST, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect.confuse(p);
    }
    @Override
    protected String describe() {
        return "Uses Confuse Ray";
    }
}
