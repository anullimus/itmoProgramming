package com.company.labs.lab2.pokemons;

import com.company.labs.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public class Eevee extends Pokemon {
    public Eevee(String name, int level){
        super(name ,level);
        setStats(55, 55, 50, 45, 65, 55);
        setType(Type.NORMAL);
        setMove(new Swagger(), new Venoshock(), new SludgeBomb());
    }
}
