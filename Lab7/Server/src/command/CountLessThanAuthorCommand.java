package command;


import data.initial.LabWork;
import data.initial.Person;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

import java.util.LinkedHashSet;

public class CountLessThanAuthorCommand extends AbstractCommand {
    private Person author;

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
            for (LabWork lw : collection) {
                if (lw.getAuthor().getName().equals(inputAuthor)) {
                    author = lw.getAuthor();
                    break;
                }
            }
            if (author != null) {
                int countOfAuthors = (int) collection.stream().filter(labwork ->
                        author.getLocation().compareTo(labwork.getAuthor().getLocation()) < 0).count();
                return new Response("Количество лабораторных работ, локация авторов которых меньше, чем у заданного: "
                        + countOfAuthors);
            } else {
                return new Response("Автор не найден.");
            }
        } else {
            return new Response("Элементу не с чем сравнивать. Коллекция пуста.");
        }
    }
}
