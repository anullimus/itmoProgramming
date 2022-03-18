package com.company.people;

import com.company.things.StockCommunity;

public class Dialogue {
    private final String companion1, companion2;
    public Dialogue(Human person1, Human person2){
        companion1 = person1.getName();
        companion2 = person2.getName();
        System.out.println("Диалог между " + companion1 + " и " + companion2 + ":");
    }
    public String sayHelloPhrase1(){
        return "Салам!\n";
    }
    public String sayHelloPhrase2(){
        return "Hola!\n";
    }
    public String sayHelloPhrase3(){
        return "Салют!\n";
    }
    public String sayByePhrase1(){
        return "Пока.";
    }
    public String sayByePhrase2(){
        return "Ciao!";
    }
    public String sayByePhrase3(){
        return "Bye!";
    }
    public String saySprutsPhrase1(){
        return "Послушайте, " + companion2 + ", что это еще за новое общество появилось?";
    }
    public String saySprutsPhrase2(){
        return "Какие-то "  + StockCommunity.GIANT_PLANTS.getTranslation() +".";
    }
    public String saySprutsPhrase3(){
        return "Вы ничего не слыхали?";
    }
    public void endDialogue(){
        System.out.println("Конец диалога.\n");
    }
}
