package serverLogic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.initial.LabWork;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * Provides the main file managing commands.
 */
public class FileManager {
    private File jsonCollection;
    private final LinkedHashSet<LabWork> labWorks;
    private HashSet<Long> addedIDOfLabWorks;

    public HashSet<Long> getAddedIDOfLabWorks() {
        return addedIDOfLabWorks;
    }

    public FileManager(String collectionPath) {
        System.out.println(collectionPath);
        this.labWorks = new LinkedHashSet<>();
        try {
            File file = new File(collectionPath);
            if (file.exists()) {
                if (file.canRead()) {
                    jsonCollection = new File(collectionPath);
                    readJsonFile();
                } else {
                    System.out.println(Tool.PS1 + "У вас нет доступа к файлу.");
                }
            } else {
                throw new FileNotFoundException();
            }
        } catch (NullPointerException exception) {
            System.out.println("Системе не удалось найти файл!(была передана null строка)");
        } catch (FileNotFoundException ex) {
            System.out.println(Tool.PS1 + "Файл-коллекция по указанному пути не существует.\n" +
                    "Элементы не добавлены в коллекцию программы.");
            System.out.println(Tool.PS1 + "Введите команду 'help' для начала работы.");
        }
    }

    /**
     * Reads json file with data.initial.LabWork{@link LabWork} elements in collection{@link CollectionManager}.
     *
     * @throws FileNotFoundException when file is not found.
     */
    private void readJsonFile() throws FileNotFoundException {
        ArrayList<Integer> incorrectLinesNumbersInInputFileCollection = new ArrayList<>();
        addedIDOfLabWorks = new HashSet<>();
        int iteratorForCountTheIncorrectElements = -1;

        LinkedHashSet<LabWork> addedLabWorks;
        try {
            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonCollection)));
            System.out.println("Попытка загрузки элементов в коллекцию...");
            String nextLine;
            StringBuilder dataString = new StringBuilder();
            while ((nextLine = inputStreamReader.readLine()) != null) {
                dataString.append(nextLine);
            }
            Type collectionType = new TypeToken<LinkedHashSet<LabWork>>() {
            }.getType();

            Gson gsonWithRewritedMethodForLocalDate = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
                }
            }).create();
            addedLabWorks = gsonWithRewritedMethodForLocalDate.fromJson(dataString.toString(), collectionType);


            if (addedLabWorks.size() != 0) {

                for (LabWork labObj : addedLabWorks) {
                    iteratorForCountTheIncorrectElements++;
                    try {
                        if (addedIDOfLabWorks.contains(labObj.getId())) {
                            throw new IllegalArgumentException();
                        } else {
                            addedIDOfLabWorks.add(labObj.getId());
                        }

                        if (labObj.getName() != null && !labObj.getName().isEmpty() && labObj.getCoordinates().getX() > 0 &&
                                labObj.getCoordinates().getY() > 0 && labObj.getCoordinates() != null &&
                                labObj.getCreationDate() != null && labObj.getMinimalPoint() > 0 &&
                                labObj.getDifficulty() != null  &&
                                labObj.getAuthor().getName() != null && !labObj.getAuthor().getName().isEmpty() &&
                                labObj.getAuthor().getBirthday() != null && labObj.getAuthor().getNationality() != null &&
                                labObj.getAuthor().getLocation().getX() > 0 && labObj.getAuthor().getLocation().getY() > 0 &&
                                labObj.getAuthor().getLocation().getZ() > 0 && labObj.getAuthor().getLocation() != null) {
                            labWorks.add(labObj);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } catch (IllegalArgumentException exception) {
                        incorrectLinesNumbersInInputFileCollection.add(iteratorForCountTheIncorrectElements);
                    }
                }
            } else {
                throw new NullPointerException();
            }
            if (incorrectLinesNumbersInInputFileCollection.size() == 0) {
                System.out.println(Tool.PS1 + "Файл-коллекция успешно загружен на сервер.");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (NullPointerException exception) {
            System.out.println("Файл пуст.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Файл-коллекция загружен на сервер.\n" +
                    "Внимание! В коллекцию добавлены не все элементы из файла, так как в нем содержатся некорректные " +
                    "элементы. Номера строк с некорректными элементами: " + incorrectLinesNumbersInInputFileCollection);
        } catch (JsonSyntaxException ex) {
            System.out.println("В файле имеются грубые нарушения формата Json. По этой причине лабораторные " +
                    "работы не могут быть загружены в коллекцию программы.");
        } catch (FileNotFoundException exception) {
            System.out.println("Файл-коллекция по указанному пути не найден.");
        } catch (IOException e) {
            System.out.println("Возникла непредвиденная ошибка при чтении файла. Проверьте корректность данных и/или " +
                    "сообщите проблему админу.");
        }
    }

    /**
     * Saves the elements to collection{@link CollectionManager}.
     * @param collection  is actual collection
     */
    public String save(LinkedHashSet<LabWork> collection) {
        try {
            OutputStream os = new FileOutputStream(jsonCollection);
            BufferedOutputStream br = new BufferedOutputStream(os, 16384);

            Gson gsonWithRewritedMethodForLocalDate = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
                    return new JsonPrimitive(localDate.toString());
                }
            }).create();
            br.write(gsonWithRewritedMethodForLocalDate.toJson(collection).getBytes(StandardCharsets.UTF_8));
            br.close();
            return Tool.PS1 + "Коллекция успешно сохранена в файл.";
        } catch (FileNotFoundException exception) {
            return Tool.PS1 + "Файл-коллекция не найден.";
        } catch (IOException e) {
            return "Возникла непредвиденная ошибка. Коллекция не может быть сохранена.";
        }
    }

    /**
     * @return updated collection
     * @see CollectionManager
     */
    public LinkedHashSet<LabWork> getLabWorks() {
        return labWorks;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof FileManager)) return false;
        FileManager fileManager = (FileManager) otherObject;
        return Objects.equals(jsonCollection, fileManager.jsonCollection) && Objects.equals(labWorks, fileManager.labWorks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonCollection, labWorks);
    }

    @Override
    public String toString() {
        return "Данный класс предназначен для организации файловой логики.";
    }
}
