package com.company.labs.lab2.moves;

import ru.ifmo.se.pokemon.*;

public class BulkUp extends StatusMove{
    public BulkUp(){
        super(Type.FIGHTING, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p){
        p.addEffect(new Effect().stat(Stat.ATTACK, +1).stat(Stat.DEFENSE, +1));
    }
    @Override
    protected String describe() {
        return "Uses Bulk Up";
    }
}
