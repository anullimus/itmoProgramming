package com.company;

import com.company.exceptions.IncorrectNameException;
import com.company.people.Krabs;
import com.company.people.Shorty;
import com.company.people.Spruts;
import com.company.people.Dialogue;
import com.company.things.*;

/*
вариант 315894
Если коротышке удавайтесь сколотить капиталец в тысячу фертингов, о нем говорили, что он стоит тысячу фертингов;
если кто-нибудь располагал суммой всего лишь в сто фертингов, говорили, что он стоит сотняжку; если же у кого-нибудь
не было за душой ни гроша, то говорили с презрением, что он вообще дрянь коротышка -- совсем ничего не стоит.

Господин Спрутс был владельцем огромной мануфактурной фабрики, известной под названием Спрутсовской мануфактуры,
выпускавшей несметные количества самых разнообразных тканей. Кроме того, у него было около тридцати сахарных
заводов и несколько латифундий, то есть громаднейших земельных участков. На всех этих земельных участках работали
тысячи коротышек, которые выращивали хлопок для Спрутсовской мануфактуры, сахарную свеклу для его сахарных заводов,
а также огромные количества лунной ржи и пшеницы, которыми господин Спрутс вел большую торговлю.

Прослышав об успехах нового акционерного общества, господин Спрутс вызвал к себе своего главного управляющего
господина Крабса и сказал:
-- Послушайте, господин Крабс, что это еще за новое общество появилось? Какие-то гигантские растения.
Вы ничего не слыхали?
 */
public class Main {
    // если без try-catch
//    public static void main(String[] args) throws IncorrectNameException, MainCharacterIsAlreadyException {
    public static void main(String[] args) {
        Spruts spruts = new Spruts();
        System.out.println(spruts);

        /*
        Вложенный класс(non-static)
        Spruts.Business business = spruts.new Business();

        Внутренний класс(static)
        Shorties.Status status = new Shorties.Status();
        */

        Manufactory manufactory = new Manufactory();
        System.out.println(manufactory);

        SugarFactory sugarFactory = new SugarFactory();
        System.out.println(sugarFactory);

        Indusrty latifundia = new Latifundies();
        System.out.println(latifundia);

        Shorty shorties = new Shorty(spruts, manufactory, sugarFactory);
        System.out.print(shorties);
        System.out.println(shorties.whatOthersSayAboutThisShorty(0));

        Krabs krabs = new Krabs();
        System.out.println(krabs);

        Dialogue dialogue = new Dialogue(spruts, krabs);
        System.out.print(spruts.getNameForDialogue() + dialogue.sayHelloPhrase1());
        System.out.print(krabs.getNameForDialogue() + dialogue.sayHelloPhrase2());
        StringBuilder sb = new StringBuilder();
        System.out.print(sb.append(spruts.getNameForDialogue()).append(dialogue.saySprutsPhrase1()).append(" ")
                .append(dialogue.saySprutsPhrase2()).append(" ").append(dialogue.saySprutsPhrase3()).append("\n"));
        dialogue.endDialogue();


        String[] members = {spruts.getNameForDialogue(), manufactory.getTypeForDialogue(), sugarFactory.getTypeForDialogue(),
                latifundia.getTypeForDialogue(), shorties.getNameForDialogue()};
        System.out.println("· Прошло 12 часов живой и увлекательной беседы между объектами рассказа Незнайка.\n");
        for (String member : members) {
            System.out.println(member + "покидает(-ют) чат...\n");
        }

        System.out.println("☣Warning☣ Все живые существа возвращаются для экспериментов ☣Warning☣");
        shorties.makeTheMainCharacter();    // unchecked исключения можно не обрабатывать
//        spruts.makeTheMainCharacter();

        try {
            spruts.setName("Lord Voldemort");
        } catch (IncorrectNameException e) {
            e.printStackTrace();
        }
        try {
            shorties.setName("Azino777");
        } catch (IncorrectNameException e) {
            /*
            Multiple Catch (если не в try-catch, то method() throws IncorrectNameException, ClassCastException{})
        } catch (IncorrectNameException | ClassCastException e) {
            */
            e.printStackTrace();
        }
    }
}
