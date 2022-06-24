package command;

import db.DatabaseHandler;
import serverLogic.CollectionManager;
import utility.Request;
import utility.Response;

public class ConnectUserCommand extends AbstractCommand{

    public ConnectUserCommand(DatabaseHandler databaseHandler, CollectionManager manager) {
        super(databaseHandler, manager);
        setDescription("подключает клиента к базе данных");
    }

    @Override
    public Response execute(Request request) {
        synchronized (getDatabaseHandler()) {
            if (getDatabaseHandler().checkIfUserExist(request.getClientName())) {
                if (getDatabaseHandler().checkIfUserConnect(request.getClientName(), request.getClientPassword())) {
                    System.out.println("Клиент " + request.getClientName() + " подключился");
                    return new Response("Клиент успешно подключился", true);
                } else {
                    return new Response("Ошибка при вводе пароля", false);
                }
            } else {
                return new Response("Пользователя с таким именем не существует", false);
            }
        }
    }
}
