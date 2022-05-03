package com.company.labs.lab2.pokemons;

import com.company.labs.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public class Illumise extends Pokemon {
    public Illumise(String name, int level) {
        super(name, level);
        setStats(65, 47, 75, 73, 85, 85);
        setType(Type.BUG);
        setMove(new Confide(), new Bulldoze(), new FocusBlast(), new BulkUp());
    }
}
