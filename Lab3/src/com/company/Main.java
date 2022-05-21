package com.company;

import com.company.people.Human;
import com.company.people.Shorty;
import com.company.people.Spruts;
import com.company.things.Indusrty;
import com.company.things.Latifundies;
import com.company.things.Manufactory;
import com.company.things.SugarFactory;

/*
вариант 315894
Господин Спрутс был владельцем огромной мануфактурной фабрики, известной под названием Спрутсовской мануфактуры,
выпускавшей несметные количества самых разнообразных тканей. Кроме того, у него было около тридцати сахарных
заводов и несколько латифундий, то есть громаднейших земельных участков. На всех этих земельных участках работали
тысячи коротышек, которые выращивали хлопок для Спрутсовской мануфактуры, сахарную свеклу для его сахарных заводов,
а также огромные количества лунной ржи и пшеницы, которыми господин Спрутс вел большую торговлю.

Внесенные изменения:
1) Конкатенация строк в необходимых местах заменена на StringBuilder.
2) Абстрактные классы Industry и Human дополнены до абстрактных по смыслу.
3) Добавлены новые классы для бОльшей связности.
4) Добавлены функциональный интерфейс и лямбда-выражение.
 */
public class Main {
    public static void main(String[] args) {
        Human spruts = new Spruts();
        System.out.println(spruts);

        Indusrty manufactory = new Manufactory();
        System.out.println(manufactory);

        Indusrty sugarFactory = new SugarFactory();
        System.out.println(sugarFactory);

        Indusrty latifundia = new Latifundies();
        System.out.println(latifundia);

        Human shorties = new Shorty(spruts, manufactory, sugarFactory);
        System.out.println(shorties);

        String[] members = {spruts.getNameForDialogue(), manufactory.getTypeForDialogue(),
                sugarFactory.getTypeForDialogue(), latifundia.getTypeForDialogue(), shorties.getNameForDialogue()};
        System.out.println("Прошло 12 часов увлекательной беседы.\n");
        for (String obj : members) {
            System.out.println(obj + "покидает(-ют) чат...");
        }

    }
}
/*
SOLID examples:
S: Spruts and Business
O: Getters/Setters (Общедоступное поведение для расширения делать нужно НЕприватным, а дынные нужно скрывать,
                                                                                   чтобы их никто не поменял.
L: Sugarfactory don't extends Latifundies
I: Lifable and Unlifable
D: Main: Human spruts = new Spruts();



                        Спрутс: ведет торговлю рожью и пшеницей; владеет производными Industry
creature(I) <- human(A) <-
                        Коротышки(тысяча штук)----------------(работают)----------------------------|
                                                                                                    |
                                                                                                    |
                    Мануфактурная фабрика(Огромная; Название - Спуртовская мануфактура;             |
                                          Выпускает ткани - много и разные)                         |
Industry(I)      <- Сахарная фабрика(около 30 штук)                                                 |
                    Латифундии(несколько; description - громаднейшие земельные участки)-------------|
                         |
       |-----------------|
       |        Хлопок
    Good(E) :   Сахарная свекла
                Лунная рожь и пшеница
 */