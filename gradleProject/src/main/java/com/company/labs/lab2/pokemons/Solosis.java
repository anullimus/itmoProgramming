package com.company.labs.lab2.pokemons;

import com.company.labs.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public class Solosis extends Pokemon {
    public Solosis(String name, int level){
        super(name, level);
        setStats(45, 30, 40, 105, 50, 20);
        setType(Type.PSYCHIC);
        setMove(new ConfuseRay(), new IceBeam());
    }
}
