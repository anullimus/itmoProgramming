package command;

//import serverLogic.MyValidator;
import com.google.gson.JsonSyntaxException;
import data.initial.LabWork;
import serverLogic.CollectionManager;
import serverLogic.NewElementReader;


public class AddCommand extends AbstractCommand {
//    private final MyValidator myValidator;
    private LabWork labWork;

    public AddCommand(CollectionManager collectionManager) {
        super(collectionManager);
        setDescription("!!!!!!!!!!!должен быть релакс месседж, тк элементы будут добавляться поочередно!!!");
//        myValidator = new MyValidator();
    }

    @Override
    public String execute(String arg) {
        try {
//            labWork = new NewElementReader();
            getCollectionManager().getLabWorks().add(getCollectionManager().getSerializer().fromJson(arg, LabWork.class));
            getCollectionManager().save();
            return "Элемент успешно добавлен.";
        } catch (JsonSyntaxException ex) {
            return "Синтаксическая ошибка JSON. Не удалось добавить элемент.";        }
    }

}
