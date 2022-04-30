package serverLogic;

import command.*;
import data.initial.LabWork;

import java.time.LocalDate;
import java.util.*;

/**
 * Collection class manages the elements of collections with lab work{@link LabWork}.
 */
public class CollectionManager {
//    private String[] userCommand;
    private final Scanner commandReader;
    private LocalDate creationTimeOfCollection;
    private FileManager fileManager;
    private LinkedHashSet<LabWork> labWorks;
    public static HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    static {
        nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
    }

    {
        commandReader = new Scanner(System.in);
    }

    public CollectionManager() {
        System.out.println(CommandManager.PS1 + "Вы забыли ввести путь к файлу коллекции в переменной окружения\n" +
                CommandManager.PS1 + "Но ничего страшного, вы можете ввести его здесь: ");
        String[] userCommand = commandReader.next().trim().split(" ");
        init(userCommand[0]);
    }

    public CollectionManager(String collectionPath) {
        init(collectionPath);
    }

    /**
     * Finally initializes the constructions.
     */
    private void init(String collectionPath) {
        fileManager = new FileManager(collectionPath);
        labWorks = fileManager.getCollection();
        creationTimeOfCollection = LocalDate.now();
    }

    /**
     * Prints full information about the command that manages the collection.
     */
    public void info() {
        System.out.println(new InfoCommand(this).execute());
    }

    /**
     * Prints all elements of the collection.
     */
    public void show() {
        System.out.println(new ShowCommand(labWorks).execute());
    }

    /**
     * Adds new element to collection.
     */
    public void add() {
        labWorks = new AddCommand(labWorks).execute();
    }

    public void save() {
        System.out.println(new SaveCommand(fileManager, labWorks));
    }

    /**
     * Updates id of element from collection.
     */
    public void updateId() {
        labWorks = new UpdateIDCommand(labWorks).execute();
    }

    /**
     * Removes the element from collection by id.
     */

    public void removeById() {
        labWorks = new RemoveByIDCommand(labWorks).execute();
    }

    /**
     * Clears the collection.
     */
    public void clear() {
        labWorks = new ClearCommand(labWorks).execute();
    }

    /**
     * Ends the program.
     */
    public void exit() {
        System.out.println(new ExitCommand().execute());
    }

    /**
     * Adds new element to collection if it is minimal.
     */
    public void addIfMin() {
        labWorks = new AddIfMinCommand(labWorks).execute();
    }

    /**
     * Removes the element from collection by id if it is the greatest.
     */
    public void removeGreater() {
        labWorks = new RemoveGreaterCommand(labWorks).execute();
    }

    /**
     * Removes the element from collection by id if it is the minimal.
     **/
    public void removeLower() {
        labWorks = new RemoveLowerCommand(labWorks).execute();
    }

    /**
     * Prints the oldest author of any lab from collection.
     */
    public void maxByAuthor() {
        System.out.println(new MaxByAuthorCommand(labWorks).execute());
    }

    /**
     * Prints the all authors who was born earlier than input author.
     */
    public void countLessThanAuthor() {
        System.out.println(new CountLessThanAuthorCommand(labWorks).execute());
    }

    /**
     * Sorts the elements of the collection by their difficulty.
     */
    public void filterByDifficulty() {
        System.out.println(new FilterByDifficultyCommand(labWorks).execute());
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

    @Override
    public int hashCode() {
        int result = Objects.hash(creationTimeOfCollection, commandReader, labWorks);
        result = 31 * result + Arrays.hashCode(App.USER_COMMAND);
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        CollectionManager that = (CollectionManager) otherObject;
        return Objects.equals(creationTimeOfCollection, that.creationTimeOfCollection)
                && Objects.equals(commandReader, that.commandReader)
                && Objects.equals(labWorks, that.labWorks);
    }

    @Override
    public String toString() {
        return "Class for to manage the collection.";
    }
}