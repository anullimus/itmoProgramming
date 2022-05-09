package command;


import serverLogic.CollectionManager;

public class ClearCommand extends Command {
    public ClearCommand(CollectionManager manager) {
        super(manager);
        setDescription("Очистить коллекцию.");
    }

    @Override
    public String execute() {
        getCollectionManager().getLabWorks().clear();
        getCollectionManager().save();
        return "Коллекция очищена.";
    }
}
