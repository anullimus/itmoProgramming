package command;

import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

public class RegisterUserCommand extends AbstractCommand{

    public RegisterUserCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        super(databaseHandler, manager);
        setDescription("Удаляет элемент из коллекции по его ID.");
    }

    @Override
    public Response execute(Request request) {
        synchronized (getDatabaseHandler()) {
            if (getDatabaseHandler().checkIfUserExist(request.getClientName())) {
                return new Response("Пользователь с таким именем уже существует", false);
            }
            if (getDatabaseHandler().registerUser(request.getClientName(), request.getClientPassword())) {
                System.out.println("Клиент " + request.getClientName() + " подключился");
                return new Response("Новый клиент зарегистрированы", true);
            }
            return new Response("Непредвиденная ошибка", false);
        }
    }
}
