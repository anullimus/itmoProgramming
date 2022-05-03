package com.company.logic;

import com.company.data.initial.Difficulty;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Provides the full information of commands that manage the collection{@link CollectionLogic} and some else technical
 * elements.
 */
public class CommandInformer {
    private final HashMap<String, String> commands = new HashMap<>();
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    {
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
        commands.put("clear", "Очистить коллекцию. Для принудительной очистки коллекции добавьте ключ '-f'.");
        commands.put("save", "Сохранить коллекцию в файл.");
        commands.put("execute_script", "Команда 'execute_script {path}' читает и исполняет скрипт из указанного файла. " +
                "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        commands.put("exit", "Завершить программу (без сохранения в файл). Для принудительного завершения программы " +
                "добавьте ключ '-f'. Также, существует ключ -secret.");


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
        commands.put("filter_by_difficulty ", "Вывести все элементы коллекции, значение поля difficulty которых " +
                "равно заданному. Доступные типы: " + Arrays.toString(Difficulty.values()));
    }

    /**
     * @param userCommand is a user wrote command.
     */
    public void help(String[] userCommand) {
        if (userCommand.length != 1) {
            System.out.println("Такой команды не существует.");
        } else {
            StringBuilder help = new StringBuilder(CommandInformer.PS1);
            help.append("Доступные команды: ").append(getCommands().keySet()).
                    append("\nИспользуйте команду 'info {command}' для справки по конкретной команде.");
            System.out.println(help);
        }
    }

    /**
     * @return commands that manage the collection.
     * @see CollectionLogic
     */
    public HashMap<String, String> getCommands() {
        return commands;
    }
}
