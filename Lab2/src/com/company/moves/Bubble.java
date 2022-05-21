package com.company.moves;

import ru.ifmo.se.pokemon.*;

public class Bubble extends SpecialMove {
    public Bubble(){
        super(Type.WATER, 40, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random() < 0.3) {
            Effect.flinch(p);
        }
    }
    @Override
    protected String describe() {
        return "Uses Bubble";
    }
}
