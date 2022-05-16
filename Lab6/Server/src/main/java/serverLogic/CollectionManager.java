package serverLogic;

import com.google.gson.reflect.TypeToken;
import data.initial.LabWork;

import java.lang.reflect.Type;
import java.time.LocalDateTime ;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Collection class manages the elements of collections with lab work{@link LabWork}.
 */
public class CollectionManager {
    private final FileManager fileManager;
    private final LinkedHashSet<LabWork> labWorks;
    private final LocalDateTime creationTimeOfCollection;
    public static Long MAX_ID;

    /**
     * Предоставляет доступ к коллекции и связанному с ней файлу.
     *
     * @param collectionPath путь к файлу коллекции в файловой системе.
     */

    public CollectionManager(String collectionPath) {
        fileManager = new FileManager(collectionPath);
        labWorks = fileManager.getLabWorks().stream().sorted(Comparator.comparing(LabWork::getMinimalPoint))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        MAX_ID = Collections.max(fileManager.getAddedIDOfLabWorks());
        creationTimeOfCollection = LocalDateTime.now();
    }

    /**
     * Записывает элементы коллекции в файл. Так как необходим нескольким командам, реализован в этом классе.
     */
    public String save() {
        return fileManager.save(labWorks.stream().sorted(Comparator.comparing(LabWork::getMinimalPoint))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public Type getCollectionType() {
        return new TypeToken<LinkedHashSet<LabWork>>() {}.getType();
    }

    /**
     * @return collection
     */
    public LinkedHashSet<LabWork> getLabWorks() {
        return labWorks;
    }

    /**
     * @return collection creation date
     */
    public LocalDateTime  getCreationDate() {
        return creationTimeOfCollection;
    }

    /**
     * @return size of collection
     */
    public int getSize() {
        return labWorks.size();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(creationTimeOfCollection, labWorks);
        result = 31 * result;
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        CollectionManager that = (CollectionManager) otherObject;
        return Objects.equals(creationTimeOfCollection, that.creationTimeOfCollection)
                && Objects.equals(labWorks, that.labWorks);
    }

    @Override
    public String toString() {
        return "Class for to manage the collection.";
    }
}