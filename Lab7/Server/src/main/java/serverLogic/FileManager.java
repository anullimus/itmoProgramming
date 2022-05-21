package serverLogic;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.initial.LabWork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Tool;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * Provides the main file managing commands.
 */
public class FileManager {
    private static final Logger logger = LogManager.getLogger(FileManager.class);
    private File jsonCollection;
    private final LinkedHashSet<LabWork> labWorks;
    private HashSet<Long> addedIDOfLabWorks;

    public HashSet<Long> getAddedIDOfLabWorks() {
        return addedIDOfLabWorks;
    }

    public FileManager(String collectionPath) throws FileNotFoundException {
        this.labWorks = new LinkedHashSet<>();
        try {
            if (collectionPath == null) {
                throw new NullPointerException();
            }
            File file = new File(collectionPath);
            if (file.exists()) {
                if (file.canRead()) {
                    jsonCollection = new File(collectionPath);
                    readJsonFile();
                } else {
                    logger.error("You don't have the permit to file.");
                }
            } else {
                throw new FileNotFoundException();
            }
        } catch (NullPointerException nullPointerException) {
            throw new NullPointerException();
        } catch (FileNotFoundException fileNotFoundException) {
            throw new FileNotFoundException();
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
            logger.info("Try to load the elements to the program's collection...");
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
                                labObj.getDifficulty() != null &&
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
                logger.info("File-collection was successfully loaded.");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (NullPointerException nullPointerException) {
            logger.error("File is empty.");
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("File-collection was loaded.\n" +
                    "Attention! Not all elements from the file were added to the collection, because some of them are" +
                    "not correct. Line numbers with invalid elements: " + incorrectLinesNumbersInInputFileCollection);
        } catch (JsonSyntaxException jsonSyntaxException) {
            logger.error("The file contains gross violations of the Json format. For this reason, labworks" +
                    "cannot be uploaded to the program's collection.");
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error("File-collection wasn't found by inputted path.");
        } catch (IOException ioException) {
            logger.error("An unexpected error occurred while reading the file. Check the correctness of the data " +
                    "and/or report the problem to the admin.");
        }
    }

    /**
     * Saves the elements to collection{@link CollectionManager}.
     *
     * @param collection is actual collection
     * @return Message of result of save the collection
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
            return Tool.PS1 + "The collection was successfully saved to file.";
        } catch (FileNotFoundException exception) {
            return Tool.PS1 + "File-collection wasn't found.";
        } catch (IOException e) {
            return "An unexpected error has occurred. The collection could not be saved.";
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
