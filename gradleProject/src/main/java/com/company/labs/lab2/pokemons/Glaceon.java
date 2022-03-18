package com.company.labs.lab2.pokemons;

import com.company.labs.lab2.moves.*;
import ru.ifmo.se.pokemon.Type;

public class Glaceon extends Eevee{
    public Glaceon(String name, int level){
        super(name, level);
        setStats(65, 60, 110, 130, 95, 65);
        setType(Type.ICE);
        setMove(new Swagger(), new Venoshock(), new SludgeBomb(), new EnergyBall());
    }
}
