package com.company.labs.lab2.pokemons;

import com.company.labs.lab2.moves.*;
import ru.ifmo.se.pokemon.Type;

public class Reuniclus extends Duosion {
    public Reuniclus(String name, int level) {
        super(name, level);
        setStats(110, 65, 75, 125, 85, 30);
        setType(Type.PSYCHIC);
        setMove(new ConfuseRay(), new IceBeam(), new Astonish(), new Bubble());
        }
}

