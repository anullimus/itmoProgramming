package serverLogic;

import com.company.common.data.initial.Difficulty;

import java.util.*;

/**
 * Provides the full information of commands that manage the collection{@link CollectionManager} and some else technical
 * elements.
 */
public class CommandManager {
    private static final HashMap<String, String> commands = new HashMap<>();
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private static final List<String> availableCommands;
    static {
        String relaxDesctiprtionMessage = "Чтобы использовать данную команду, вам достаточно ввести только ее " +
                "сигнатуру, а заполнение элемента вам будет предложено в последующих строчках в интерактивном режиме.";
        commands.put("help", "Вывести справку по доступным командам.");
        commands.put("info", "Вывести в стандартный поток вывода информацию " +
                "о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        commands.put("show", "Вывести в стандартный поток вывода все элементы " +
                "коллекции в строковом представлении.");
        commands.put("add", relaxDesctiprtionMessage);
        commands.put("update_id", "Команда 'update_id {id}' обновляет значение элемента коллекции, id которого " +
                "равен заданному.");
        commands.put("remove_by_id", "Команда 'remove_by_id {id}' удаляет элемент из коллекции по его id.");
        commands.put("clear", "Очистить коллекцию.");
        commands.put("save", "Сохранить коллекцию в файл.");
        commands.put("execute_script", "Команда 'execute_script {path}' читает и исполняет скрипт из указанного файла. " +
                "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        commands.put("exit", "Завершить программу (без сохранения в файл).");
        commands.put("add_if_min", "Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего " +
                "элемента этой коллекции. " + relaxDesctiprtionMessage);
        commands.put("remove_greater", "Команда 'remove_greater {id}' удаляет из коллекции все элементы, " +
                "превышающие заданный(по показателю минимального балла)");
        commands.put("remove_lower", "Команда 'remove_lower {id}' удаляет из коллекции все элементы, " +
                "не превышающие заданный(по показателю минимального балла)");
        commands.put("max_by_author", "Команда 'max_by_author' выводит любой объект из коллекции, значение " +
                "поля author которого является максимальным.");
        commands.put("count_less_than_author", "Команда 'count_less_than_author' выводит любой объект из коллекции, " +
                "значение поля author которого является максимальным.");
        commands.put("filter_by_difficulty", "Вывести все элементы коллекции, значение поля difficulty которых " +
                "равно заданному. Доступные типы: " + Arrays.toString(Difficulty.values()));

        availableCommands = new ArrayList<>(commands.keySet());
    }


    public static void help() {
        if (App.USER_COMMAND.length != 1) {
            System.out.println("Такой команды не существует.");
        } else {
            StringBuilder help = new StringBuilder(CommandManager.PS1);
            help.append("Доступные команды: ").append(commands.entrySet());
            System.out.println(help);
        }
    }

    public static List<String> getAvailableCommands() {
        return availableCommands;
    }

    public static Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }
}
