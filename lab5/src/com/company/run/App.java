package com.company.run;

//import com.company.data.secretDontOpen.ScreenSizer;
import com.company.logic.CollectionLogic;

/**
 * @author Amir Khanov
 * @version 1.0
 * Main class for <Strong>run</Strong> the application.
 * <img src="doc-files" alt-":^"/>
 */
public class App {
    /**
     * Start point of the program
     * @param args command line values
     */
    public static void main(String[] args) {
//        ScreenSizer sizer = new ScreenSizer();
//        try {
//            sizer.tryToMaximize();
//        }catch (InterruptedException e){
//            System.out.println("Расширьте экран.");
//        }
        System.out.println("Добро пожаловать в обитель моей лабы 5. Здесь вы можете ознакомиться с коллекцией LabWorks.");
        CollectionLogic collectionLogic0 = new CollectionLogic(System.getenv("VARRY"));   // переменная окружения
//        CollectionLogic collectionLogic = new CollectionLogic(); // добавьте в аргумент путь к файлу коллекции
        collectionLogic0.interactiveMode();

    }
}
