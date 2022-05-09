package command;

import com.google.gson.JsonSyntaxException;
import serverLogic.CollectionManager;
import utility.CommandAnalyzer;
import utility.Request;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class ExecuteScriptCommand extends Command {
    private final HashMap<String, Command> availableCommandsWithDescription;
    private final HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;
    private final Command errorCommand;

    public ExecuteScriptCommand(CollectionManager collectionManager,
                                HashMap<String, Command> availableCommandsWithDescription) {
        super(collectionManager);
        setDescription("Считать и исполнить скрипт из указанного файла.");
        this.availableCommandsWithDescription = availableCommandsWithDescription;
        this.nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        errorCommand = new Command(null) {
            @Override
            public String execute() {
                return "+1 некорректная команда.";
            }
        };
    }

    private Request createScriptCommandWrapper(CommandAnalyzer commandAnalyzer) {
        if (commandAnalyzer.isCommandHaveArgument()) {
            return new Request(commandAnalyzer.getCommandName(),
                    commandAnalyzer.getCommandArgumentString());
        } else {
            return new Request(commandAnalyzer.getCommandName());
        }
    }

    private void workWithScriptCommandWrapper(Request scriptCommandWrapper) {
        if (scriptCommandWrapper.isCommandHaveArgument())
            if (scriptCommandWrapper.getCommandName().equals("execute_script")) {
                new ExecuteScriptCommand(getCollectionManager(), availableCommandsWithDescription)
                        .execute(scriptCommandWrapper.getCommandArgument());
            } else {
                availableCommandsWithDescription.
                        getOrDefault(scriptCommandWrapper.getCommandName(), errorCommand).
                        execute(scriptCommandWrapper.getCommandArgument());
            }
        else {
            availableCommandsWithDescription.
                    getOrDefault(scriptCommandWrapper.getCommandName(), errorCommand).execute();
        }
    }

    @Override
    public String execute(String scriptPath) {
        Request scriptCommandWrapper;
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer();
        if (nameOfFilesThatWasBroughtToExecuteMethod.contains(scriptPath)) {
            System.out.println("В файле присутствует конструкция, которая приводит к рекурсии!\n" +
                    "Выполнение скрипта приостановлено");
        } else {
            nameOfFilesThatWasBroughtToExecuteMethod.add(scriptPath);
            try {
                File scriptFile = new File(scriptPath);
                String line;
                if (scriptFile.canRead()) {
                    BufferedReader br = new BufferedReader(new FileReader(scriptPath));
                    while ((line = br.readLine()) != null) {
                        if (commandAnalyzer.analyzeCommand(line.trim())) {
                            scriptCommandWrapper = createScriptCommandWrapper(commandAnalyzer);
                            workWithScriptCommandWrapper(scriptCommandWrapper);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                    nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
                    return "Исполнение скрипта завершено.";
                } else {
                    nameOfFilesThatWasBroughtToExecuteMethod.remove(scriptPath);
                    return "У вас нет доступа к файл-скрипту.";
                }
            } catch (FileNotFoundException fileNotFoundException) {
                return "Файл-скрипт не найден.";
            } catch (IOException ioException) {
                return "Возникла непредвиденная ошибка. Обратитесь в поддержку.";
            } catch (JsonSyntaxException jsonSyntaxException) {
                return "Ошибка в синтаксисе JSON. Не удалось добавить элемент.";
            } catch (IllegalArgumentException illegalArgumentException) {
                return errorCommand.execute();
            }
        }
        return "Бонжур. Вас приветствует костыль из execute метода у класса ExecuteScript в самом конце метода.";
    }
}
