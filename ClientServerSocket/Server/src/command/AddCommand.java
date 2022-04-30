package command;

import clientLogic.MyValidator;
import serverLogic.CollectionManager;


public class AddCommand extends AbstractCommand {
    private final MyValidator myValidator;

    public AddCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription("Добавить новый элемент в коллекцию.");
        myValidator = new MyValidator();
    }

    @Override
    public String execute() {
        boolean success = myValidator.checkAddCommand(getCollectionManager().getLabWorks());
        if (success) {
            setCollectionManager(); = myValidator.getCollection());
            System.out.println("Элемент успешно добавлен");
        } else {
            System.out.println("Ошибка при добавлении элемента, возможно, такой элемент уже существует");
        }
        return collection;
    }
}
