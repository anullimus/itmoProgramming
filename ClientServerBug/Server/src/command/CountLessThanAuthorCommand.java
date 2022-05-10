package command;


import data.initial.LabWork;
import data.initial.Person;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;

public class CountLessThanAuthorCommand extends Command {

    public CountLessThanAuthorCommand(CollectionManager manager) {
        super(manager);
        setDescription("Вывести кол-во объектов в коллекции, значение поля author которого меньше, чем у входного автора.");
    }

    @Override
    public Response execute(Request request) {
        LinkedHashSet<LabWork> collection = getCollectionManager().getLabWorks();
        String inputAuthor = request.getStringArgument();
        if (collection.size() != 0) {
            // создаю объект, тк поиск сначала идет по имени, но потом мы сравниваем другие поля
            Person author = null;
            for (LabWork lw : collection) {
                if (lw.getAuthor().getName().equals(inputAuthor)) {
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
                return new Response("Количество лабораторных работ, авторы которых родились раньше введенного автора: "
                        + countOfAuthors);
            } else {
                return new Response("Автор не найден.");

            }
        } else {
            return new Response("Элементу не с чем сравнивать. Коллекция пуста.");
        }
    }
}
