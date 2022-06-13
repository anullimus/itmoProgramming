package command;


import serverLogic.CollectionManager;
import utility.IResponse;
import utility.Request;
import utility.Response;

import java.io.Serializable;

public abstract class AbstractCommand implements Command, Serializable {
    private CollectionManager collectionManager; //Позволяет изменить коллекцию.
    private String description; //Содержит краткое руководство к команде.
    private final boolean needCheckAuthentication;

    public AbstractCommand(CollectionManager manager, boolean needCheckAuthentication) {
        this.collectionManager = manager;
        this.needCheckAuthentication = needCheckAuthentication;
    }

    /**
     * Метод служит для выполнения кода команды без агрументов(если его не переопределили).
     * @return строка, которая содержит результат операции.
     */
    public Response execute(){
        return new Response("Отсутствует аргумент.");
    }
//    public Response execute(Request request) {
//        return execute();
//    }
    public abstract IResponse execute(Request request);

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
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
    public boolean needCheckAuthentication() {
        return needCheckAuthentication;
    }



}