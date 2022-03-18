package com.company.labs.lab2.pokemons;

import com.company.labs.lab2.moves.*;
import ru.ifmo.se.pokemon.Type;

public class Duosion extends Solosis {
    public Duosion(String name, int level) {
        super(name, level);
        setStats(65, 40, 50, 125, 60, 30);
        setType(Type.PSYCHIC);
        setMove(new ConfuseRay(), new IceBeam(), new Astonish());
    }
}