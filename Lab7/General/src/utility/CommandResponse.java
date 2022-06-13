package utility;

import data.initial.LabWork;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class CommandResponse implements IResponse{
    private String serverMessage;
    private LinkedHashSet<LabWork> collection;

    public CommandResponse(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public CommandResponse(LinkedHashSet<LabWork> collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return (collection == null ? serverMessage : collection.isEmpty() ? "Коллекция пуста" : collection.stream()
                .map(LabWork::toString).collect(Collectors.joining("\n")));
    }
}
