package com.company.server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.company.common.data.initial.LabWork;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * Provides the main file managing commands.
 */
public class FileManager {
    private File fileCollection;
    private final LinkedHashSet<LabWork> collection;
    public static long MAX_ID = 0;

    public FileManager(String collectionPath) {
        System.out.println(collectionPath);
        this.collection = new LinkedHashSet<>();
        try {
            File file = new File(collectionPath);
            if (file.exists()) {
                if (file.canRead()) {
                    fileCollection = new File(collectionPath);
                    readJsonFile();
                } else {
                    System.out.println(CommandInformer.PS1 + "У вас нет доступа к файлу.");
                }
                System.out.println(CommandInformer.PS1 + "Введите команду 'help' для начала работы.");
            } else {
                throw new FileNotFoundException();
            }
        } catch (NullPointerException exception) {
            System.out.println("Системе не удалось найти файл!(была передана null строка)");
        } catch (FileNotFoundException ex) {
            System.out.println(CommandInformer.PS1 + "Файл-коллекция по указанному пути не существует.\n" +
                    "Элементы не добавлены в коллекцию программы.");
            System.out.println(CommandInformer.PS1 + "Введите команду 'help' для начала работы.");
        }
    }

    /**
     * Reads json file with LabWork{@link LabWork} elements in collection{@link CollectionManager}.
     *
     * @throws FileNotFoundException when file is not found.
     */
    private void readJsonFile() throws FileNotFoundException {
        ArrayList<Integer> incorrectLinesNumbersInInputFileCollection = new ArrayList<>();
        ArrayList<Long> addedIDOfLabWorks = new ArrayList<>();
        int iteratorForCountTheIncorrectElements = 0;

        LinkedHashSet<LabWork> addedLabWorks = null;
        try {
            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileCollection)));
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
                    try {
                        if (addedIDOfLabWorks.contains(labObj.getId())) {
                            throw new IllegalArgumentException();
                        } else {
                            addedIDOfLabWorks.add(labObj.getId());
                        }

                        if (labObj.getName() != null && !labObj.getName().isEmpty() && labObj.getCoordinates().getX() > 0 &&
                                labObj.getCoordinates().getY() > 0 && labObj.getCoordinates() != null &&
                                labObj.getCreationDate() != null && labObj.getMinimalPoint() > 0 &&
                                labObj.getDifficulty() != null && (labObj.getAuthor() != null || MAX_ID == 0) &&
                                labObj.getAuthor().getName() != null && !labObj.getAuthor().getName().isEmpty() &&
                                labObj.getAuthor().getBirthday() != null && labObj.getAuthor().getNationality() != null &&
                                labObj.getAuthor().getLocation().getX() > 0 && labObj.getAuthor().getLocation().getY() > 0 &&
                                labObj.getAuthor().getLocation().getZ() > 0 && labObj.getAuthor().getLocation() != null) {
                            collection.add(labObj);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } catch (IllegalArgumentException exception) {
                        incorrectLinesNumbersInInputFileCollection.add(iteratorForCountTheIncorrectElements);
                    }
                }
                MAX_ID = Collections.max(addedIDOfLabWorks);
            } else {
                throw new NullPointerException();
            }
            if (incorrectLinesNumbersInInputFileCollection.size() == 0) {
                System.out.println(CommandInformer.PS1 + "Файл успешно загружен.");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (NullPointerException exception) {
            System.out.println("Файл пуст.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Файл загружен.\n" +
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
    public void save(LinkedHashSet<LabWork> collection) {
        try {
            OutputStream os = new FileOutputStream(fileCollection);
            BufferedOutputStream br = new BufferedOutputStream(os, 16384);

            Gson gsonWithRewritedMethodForLocalDate = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
                    return new JsonPrimitive(localDate.toString());
                }
            }).create();

            br.write(gsonWithRewritedMethodForLocalDate.toJson(collection).getBytes(StandardCharsets.UTF_8));
            br.close();
            System.out.println(CommandInformer.PS1 + "Коллекция успешно сохранена в файл.");
        } catch (FileNotFoundException exception) {
            System.out.println(CommandInformer.PS1 + "Файл-коллекция не найден.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return updated collection
     * @see CollectionManager
     */
    public LinkedHashSet<LabWork> getCollection() {
        return collection;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof FileManager)) return false;
        FileManager fileManager = (FileManager) otherObject;
        return Objects.equals(fileCollection, fileManager.fileCollection) && Objects.equals(collection, fileManager.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileCollection, collection);
    }

    @Override
    public String toString() {
        return "Данный класс предназначен для организации файловой логики.";
    }
}
