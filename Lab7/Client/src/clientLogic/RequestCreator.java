package clientLogic;

import data.initial.LabWork;
import utility.CommandAnalyzer;
import utility.NewElementReader;
import utility.Request;

public class RequestCreator {
    private static String clientName;
    private static String clientPassword;

    private RequestCreator() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void setClientName(String clientName) {
        RequestCreator.clientName = clientName;
    }

    public static void setClientPassword(String clientPassword) {
        RequestCreator.clientPassword = clientPassword;
    }

    public static Request createRequest(String[] line, NewElementReader newElementReader, boolean isScriptExecuting) {
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer();
        commandAnalyzer.analyzeCommand(line, isScriptExecuting);
        Request request = new Request(commandAnalyzer, clientName, clientPassword);
        if (commandAnalyzer.isCommandHaveArgument()) {
            Class<?> argumentClass = commandAnalyzer.getArgumentClass();
            Number commandArgument = null;
            try {
                if (argumentClass.equals(Long.class)) {
                    commandArgument = Long.parseLong(commandAnalyzer.getCommandArgumentString());
                }
                if (argumentClass.equals(Double.class)) {
                    commandArgument = Double.parseDouble(commandAnalyzer.getCommandArgumentString());
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Ошибка при вводе аргумента");
            }
            assert commandArgument != null;
            request.setStringArgument(commandArgument.toString());
        }
        if (commandAnalyzer.isScriptExecuting()) {
            LabWork labWork = newElementReader.readNewLabwork(isScriptExecuting);
            labWork.setClientName(clientName);
            request.setLabWorkArgument(labWork);
        }
        return request;
    }
}
