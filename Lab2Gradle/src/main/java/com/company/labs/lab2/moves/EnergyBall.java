package com.company.labs.lab2.moves;

import ru.ifmo.se.pokemon.*;

public class EnergyBall extends SpecialMove {
    public EnergyBall(){
        super(Type.GRASS, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        p.addEffect(new Effect().chance(0.1).stat(Stat.SPECIAL_DEFENSE, -1));
    }
    @Override
    protected String describe() {
        return "Uses Energy Ball";
    }
}
