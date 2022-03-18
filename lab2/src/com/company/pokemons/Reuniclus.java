package com.company.pokemons;

import com.company.moves.*;
import ru.ifmo.se.pokemon.Type;

public class Reuniclus extends Duosion {
    public Reuniclus(String name, int level) {
        super(name, level);
        setStats(110, 65, 75, 125, 85, 30);
        setType(Type.PSYCHIC);
        setMove(new ConfuseRay(), new IceBeam(), new Astonish(), new Bubble());
        }
}

