package com.company.moves;

import ru.ifmo.se.pokemon.*;

public class FocusBlast extends SpecialMove {
    public FocusBlast(){
        super(Type.GRASS, 120, 70);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        p.addEffect(new Effect().chance(0.1).stat(Stat.SPECIAL_DEFENSE, -1));
    }
    @Override
    protected String describe() {
        return "Uses Focus Blast";
    }
}
