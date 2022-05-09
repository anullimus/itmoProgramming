package command;


import serverLogic.CollectionManager;

public class Command {
    private CollectionManager collectionManager; //Позволяет изменить коллекцию.
    private String description; //Содержит краткое руководство к команде.

    public Command(CollectionManager manager) {
        this.collectionManager = manager;
    }

    /**
     * Метод служит для выполнения кода команды без агрументов(если его не переопределили).
     * @return строка, которая содержит результат операции.
     */
    public String execute(){
        return "Отсутствует аргумент.";
    }
    public String execute(String arg) {
        return execute();
    }

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
}