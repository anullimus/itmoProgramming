package command;

import com.company.common.data.initial.LabWork;
import serverLogic.FileManager;

import java.util.LinkedHashSet;

public class SaveCommand implements AbstractCommand<String> {
    private final FileManager fileManager;
    private final LinkedHashSet<LabWork> collection;

    public SaveCommand(FileManager fileManager, LinkedHashSet<LabWork> collection) {
        this.fileManager = fileManager;
        this.collection = collection;
    }

    @Override
    public String execute() {
        fileManager.save(collection);
        return "Коллекция сохранена успешно.";
    }
}
