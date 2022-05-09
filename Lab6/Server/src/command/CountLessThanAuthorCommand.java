package command;


import data.initial.LabWork;
import data.initial.Person;
import serverLogic.CollectionManager;

import java.util.LinkedHashSet;

public class CountLessThanAuthorCommand extends Command {

    public CountLessThanAuthorCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести кол-во объектов в коллекции, значение поля author которого меньше, чем у входного автора.");
    }

    @Override
    public String execute(String arg) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        if (collection.size() != 0) {
            try {
                // создаю объект, тк поиск сначала идет по имени, но потом мы сравниваем другие поля
                Person author = null;
                for (LabWork lw : collection) {
                    if (lw.getAuthor().getName().equals(arg)) {
                        author = lw.getAuthor();
                        break;
                    }
                }
                if (author != null) {
                    long countOfAuthors = 0;
                    for (LabWork lw : collection) {
                        if (lw.getAuthor().getBirthday().compareTo(author.getBirthday()) < 0) {
                            countOfAuthors++;
                        }
                    }
                    return "Количество лабораторных работ, авторы которых родились раньше введенного автора: "
                            + countOfAuthors;
                } else {
                    return "Автор не найден.";

                }
            }catch (NullPointerException | IllegalArgumentException exception) {
                return "Передан некорректный аргумент.";
            }
        } else {
            return "Элементу не с чем сравнивать. Коллекция пуста.";
        }
    }
}
