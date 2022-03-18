package com.company.moves;

import ru.ifmo.se.pokemon.*;

public class Bulldoze extends PhysicalMove {
    public Bulldoze(){
        super(Type.GROUND, 60, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.SPEED, -1);
    }
    @Override
    protected String describe() {
        return "Uses Bulk Up";
    }
}
