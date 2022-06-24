package utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = 3324532584203608458L;
    private final String serverMessage;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;

    public Response(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }

    // below 2 methods are for first init with connection
    public Response(String serverMessage, ArrayList<String> availableCommands,
                    Map<String, Class<?>> commandsNeedArgument) {
        this.availableCommands = availableCommands;
        this.commandsNeedArgument = commandsNeedArgument;
        this.serverMessage = serverMessage;
    }

    @Override
    public String toString() {
        return serverMessage;
    }
}
