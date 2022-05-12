package utility;

import data.initial.LabWork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Response implements Serializable {
    private String serverMessage;
    private LinkedHashSet<LabWork> collection;
    private long maxIdInCollection;
    private ArrayList<String> availableCommands;
    private Map<String, Class<?>> commandsNeedArgument;

    public Response(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public Response(String serverMessage, long maxIdInCollection, ArrayList<String> availableCommands,
                    Map<String, Class<?>> commandsNeedArgument) {
        this.availableCommands = availableCommands;
        this.commandsNeedArgument = commandsNeedArgument;
        this.serverMessage = serverMessage;
        this.maxIdInCollection = maxIdInCollection;
    }

    public long getMaxIdInCollection() {
        return maxIdInCollection;
    }

    public ArrayList<String> getAvailableCommands() {
        return availableCommands;
    }

    public Map<String, Class<?>> getCommandsNeedArgument() {
        return commandsNeedArgument;
    }

    public Response(LinkedHashSet<LabWork>  collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return (collection == null ? serverMessage : collection.stream().map(LabWork::toString)
                .collect(Collectors.joining("\n")));
    }
}
