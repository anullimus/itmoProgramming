package serverLogic;

import data.initial.LabWork;
import database.DBManager;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Collection class manages the elements of collections with lab work{@link LabWork}.
 */
public class CollectionManager {
    private Set<LabWork> labWorks;
    private LocalDate creationTimeOfCollection;
    private DBManager dbManager;
    public static Long MAX_ID;

    /**
     * Предоставляет доступ к коллекции и связанному с ней файлу.
     */

    public CollectionManager(DBManager dbManager){
        try {
            this.dbManager = dbManager;
            this.labWorks = Collections.synchronizedSet(new LinkedHashSet<>());
            this.labWorks.addAll(dbManager.readElementsFromDB());
            creationTimeOfCollection = LocalDate.now();


//            MAX_ID = Collections.max(fileManager.getAddedIDOfLabWorks());
            MAX_ID = 2000L;
        } catch (NullPointerException nullPointerException) {
            throw new NullPointerException();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ServerLogger.logErrorMessage(e.getMessage());
        }
    }

    public DBManager getDbManager() {
        return dbManager;
    }
    //    /**
//     * Записывает элементы коллекции в файл. Так как необходим нескольким командам, реализован в этом классе.
//     *
//     * @return Message of result of save the collection
//     */
//    public String save() {
//        return fileManager.save(labWorks.stream().sorted(Comparator.comparing(LabWork::getMinimalPoint))
//                .collect(Collectors.toCollection(LinkedHashSet::new)));
//    }

    /**
     * @return collection
     */
    public Set<LabWork> getLabWorks() {
        return labWorks;
    }

    /**
     * @return collection creation date
     */
    public LocalDate getCreationDate() {
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