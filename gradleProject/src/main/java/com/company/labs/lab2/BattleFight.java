package com.company.labs.lab2;

import com.company.labs.lab2.pokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class BattleFight {

    public static void main(String ... args) {
        Battle battle = new Battle();

        Duosion first = new Duosion("Duosion", 2);
        Eevee second = new Eevee("Eevee", 1);
        Glaceon third = new Glaceon("Glaceon", 3);
        Illumise fourth = new Illumise("Illumise", 1);
        Reuniclus fifth = new Reuniclus("Reuniclus", 2);
        Solosis sixth = new Solosis("Solosis", 1);

        Pokemon[] names = {first, second, third, fourth, fifth, sixth}; // полиморфизм
//        for (Pokemon name: names){    // полиморфный вызов метода(здесь не подойдет, тк нам нужен индекс ниже)
        for (int i = 0; i < 6; i++){
            if (i < 3)battle.addAlly(names[i]);
            else battle.addFoe(names[i]);
        }
        battle.go();
    }
}
