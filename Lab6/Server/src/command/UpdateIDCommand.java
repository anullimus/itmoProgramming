package command;

import data.initial.LabWork;
import serverLogic.CollectionManager;

import java.util.LinkedHashSet;


/**
 * Updates id of element from collection.
 */
public class UpdateIDCommand extends Command {

    public UpdateIDCommand(CollectionManager manager) {
        super(manager);
        setDescription("Обновить значение элемента коллекции, id которого равен заданному.");
    }

    @Override
    public String execute(String arg) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        try {
            long id = Long.parseLong(arg);

            for (LabWork labWork : collection) {
                if (labWork.getId() == id) {
                    labWork.changeId();
                    getCollectionManager().save();
                    return "Элемент с id = " + id + " успешно обновлен.";
                }
            }
            return "Here is no lab work find with id=" + id;
        } catch (NumberFormatException e) {
            return "Введеныные данные содержат неверный формат.";
        } catch (NullPointerException | IllegalArgumentException exception) {
            return "Передан некорректный аргумент.";
        }
    }
}
