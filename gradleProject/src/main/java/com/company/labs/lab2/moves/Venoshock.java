package com.company.labs.lab2.moves;

import ru.ifmo.se.pokemon.*;

public class Venoshock extends SpecialMove {
    public Venoshock(){
        super(Type.POISON, 65, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        Status p_stat = def.getCondition();
        if (p_stat.equals(Status.POISON)) {
            super.applyOppDamage(def, damage * 2);
        }
    }

    @Override
    protected String describe() {
        return "Uses Venoshock";
    }
}
