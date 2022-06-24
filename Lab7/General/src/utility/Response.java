package utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = 3324532584203608458L;
    private String serverMessage;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;

    private String connectMessage;
    private boolean clientConnected;

    public Response(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public Response(String connectMessage, boolean clientConnected) {
        this.connectMessage = connectMessage;
        this.clientConnected = clientConnected;
    }
    public String getConnectMessage() {
        return connectMessage;
    }

    public boolean isClientConnected() {
        return clientConnected;
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
