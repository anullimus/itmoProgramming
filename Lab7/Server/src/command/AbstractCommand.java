package command;


import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.io.Serializable;

public class AbstractCommand implements Command, Serializable {
    private CollectionManager collectionManager; //Позволяет изменить коллекцию.
    private String description; //Содержит краткое руководство к команде.
    private DatabaseHandler databaseHandler;

    public AbstractCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        this.collectionManager = manager;
        this.databaseHandler = databaseHandler;
    }
    /**
     * Для команд, которым не нужно взаимодействие с базой данных
     */
    public AbstractCommand(CollectionManager manager) {
        this.collectionManager = manager;
    }

    /**
     * Метод служит для выполнения кода команды без агрументов(если его не переопределили).
     * @return строка, которая содержит результат операции.
     */
    public Response execute(){
        return new Response("Отсутствует аргумент.");
    }
    public Response execute(Request request) {
        return execute();
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public String getDescription() {
        return description;
    }

}