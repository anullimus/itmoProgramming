package utility;

import clientLogic.NewElementReader;

public class RequestCreator {
    private RequestCreator() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static Request createRequest(String command, NewElementReader NewElementReader) {
        CommandAnalyzer commandAnalyzer = new CommandAnalyzer(command);
        Request request = new Request(commandAnalyzer.getCommandName());
        if (commandAnalyzer.isCommandHaveArgument()) {
            Class<?> argumentClass = commandAnalyzer.getArgumentClass();
            Number commandArgument = null;
            try {
                if (argumentClass.equals(Long.class)) {
                    commandArgument = Long.parseLong(commandAnalyzer.getCommandArgument());
                }
                if (argumentClass.equals(Double.class)) {
                    commandArgument = Double.parseDouble(commandAnalyzer.getCommandArgument());
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Ошибка при вводе аргумента");
            }
            request.setCommandArgument(commandArgument);
        }
        if (commandAnalyzer.isCommandNeedRoute()) {
            request.setRouteToSend(NewElementReader.readNewLabwork());
        }
        return request;
    }
}
