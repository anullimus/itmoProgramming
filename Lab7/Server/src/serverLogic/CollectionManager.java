package serverLogic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.initial.LabWork;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * Collection class manages the elements of collections with lab work{@link LabWork}.
 */
public class CollectionManager {
    private final FileManager fileManager;
    private final LinkedHashSet<LabWork> labWorks;
    private final LocalDate creationTimeOfCollection;

    /**
     * Предоставляет доступ к коллекции и связанному с ней файлу.
     *
     * @param collectionPath путь к файлу коллекции в файловой системе.
     */

    public CollectionManager(String collectionPath) {
        fileManager = new FileManager(collectionPath);
        labWorks = fileManager.getLabWorks();
        creationTimeOfCollection = LocalDate.now();
    }

    /**
     * Записывает элементы коллекции в файл. Так как необходим нескольким командам, реализован в этом классе.
     */
    public String save() {
        return fileManager.save(labWorks);
//
//        try {
//            OutputStream os = new FileOutputStream(System.getenv("VARRY"));
//            BufferedOutputStream br = new BufferedOutputStream(os, 16384);
//
//            Gson gsonWithRewritedMethodForLocalDate = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
//                @Override
//                public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
//                    return new JsonPrimitive(localDate.toString());
//                }
//            }).create();
//            System.out.println(labWorks.toString());
//            br.write(gsonWithRewritedMethodForLocalDate.toJson(labWorks).getBytes(StandardCharsets.UTF_8));
//            br.close();
//            System.out.println(ServerConnection.PS1 + "Коллекция успешно сохранена в файл.");
//        } catch (FileNotFoundException exception) {
//            System.out.println(ServerConnection.PS1 + "Файл-коллекция не найден.");
//        } catch (IOException e) {
//            System.out.println("Возникла непредвиденная ошибка. Коллекция не может быть сохранена.");
//        }
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
    public LocalDate getCreationDate() {
        return creationTimeOfCollection;
    }

    /**
     * @return size of collection
     */
    public int getSize() {
        return labWorks.size();
    }
    public Gson getSerializer() {
        return new Gson();
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