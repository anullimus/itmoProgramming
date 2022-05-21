package com.company.pokemons;

import com.company.moves.*;
import ru.ifmo.se.pokemon.*;

public class Solosis extends Pokemon {
    public Solosis(String name, int level){
        super(name, level);
        setStats(45, 30, 40, 105, 50, 20);
        setType(Type.PSYCHIC);
        setMove(new ConfuseRay(), new IceBeam());
    }
}
