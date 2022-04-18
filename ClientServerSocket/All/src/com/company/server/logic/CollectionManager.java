package com.company.server.logic;

import com.company.common.data.initial.*;
import com.company.server.command.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Collection class manages the elements of collections with lab work{@link LabWork}.
 */
public class CollectionManager {
    private final CommandInformer commandInformer;
    private String[] userCommand;
    private final Scanner commandReader;
    private boolean isScript;
    private LocalDate creationTimeOfCollection;
    private FileManager fileManager;
    private LinkedHashSet<LabWork> collection;
    private final HashSet<String> nameOfFilesThatWasBroughtToExecuteMethod;

    {
        this.nameOfFilesThatWasBroughtToExecuteMethod = new HashSet<>();
        this.userCommand = new String[]{""};
        this.commandInformer = new CommandInformer();
        this.isScript = false;
        this.commandReader = new Scanner(System.in);
    }

    public CollectionManager() {
        System.out.println(CommandInformer.PS1 + "Вы забыли ввести путь к файлу коллекции в переменной окружения\n" +
                CommandInformer.PS1 + "Но ничего страшного, вы можете ввести его здесь: ");
        userCommand = commandReader.next().trim().split(" ");
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
        collection = fileManager.getCollection();
        creationTimeOfCollection = LocalDate.now();
    }

    /**
     * Opens interactive mode for manage the collection.
     */
//    public void interactiveMode() {
//        try {
//            while (!userCommand[0].equals("exit")) {
//                if (!isScript) {
//                    System.out.print(CommandInformer.PS2);
//                    userCommand = commandReader.nextLine().trim().split(" ", 2);
//                }
//                switch (userCommand[0]) {
//                    case "":
//                        break;
//                    case "help":
//                        commandInformer.help(userCommand);
//                        break;
//                    case "info":
//                        info();
//                        break;
//                    case "show":
//                        show();
//                        break;
//                    case "add":
//                        add();
//                        break;
//                    case "update_id":
//                        updateId(userCommand[1]);
//                        break;
//                    case "remove_by_id":
//                        removeById(userCommand[1]);
//                        break;
//                    case "clear":
//                        clear();
//                        break;
//                    case "save":
//                        fileManager.save(userCommand, collection);
//                        break;
//                    case "execute_script":
//                        executeScript();
//                        break;
//                    case "exit":
//                        break;
//                    case "add_if_min":
//                        addIfMin();
//                        break;
//                    case "remove_greater":
//                        removeGreater(userCommand[1]);
//                        break;
//                    case "remove_lower":
//                        removeLower(userCommand[1]);
//                        break;
//                    case "max_by_author":
//                        maxByAuthor();
//                        break;
//                    case "count_less_than_author":
//                        countLessThanAuthor(userCommand[1]);
//                        break;
//                    case "filter_by_difficulty ":
//                        filterByDifficulty(userCommand[1]);
//                        break;
//                    default:
//                        System.out.println(CommandInformer.PS1 + "Такой команды не существует.");
//                        break;
//                }
//                if (isScript) {
//                    break;
//                }
//            }
//            if (!isScript) {
//                exit();
//            }
//        } catch (NoSuchElementException exception) {
//            System.out.println(CommandInformer.PS1
//                    + "Возникла непредвиденная ошибка. Программа остановлена, обратитесь в поддержку.");
//            fileManager.makeDump(userCommand, collection);
////            exception.printStackTrace();
//            System.exit(0);
//        } catch (ArrayIndexOutOfBoundsException exception) {
//            if (!isScript) {
//                System.out.println("Возможно вы забыли добавить аргумент рядом с командой.");
//                interactiveMode();
//            }
//        }
//    }

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
        System.out.println(new ShowCommand(collection).execute());
    }

    /**
     * Adds new element to collection.
     */
    public void add() {
        collection.add(new AddCommand(userCommand[1], isScript).execute());
    }

    public void save(){
        System.out.println(new SaveCommand(fileManager, collection));
    }
    /**
     * Updates id of element from collection.
     */
    public void updateId(String id) {
        collection = new UpdateIDCommand(id, collection).execute();
    }

    /**
     * Removes the element from collection by id.
     */

    public void removeById(String id) {
        collection = new RemoveByIDCommand(id, collection).execute();
    }

    /**
     * Clears the collection.
     */
    public void clear() {
        collection = new ClearCommand(collection, userCommand, isScript).execute();
    }

    /**
     * Ends the program.
     */
    public void exit() {
        System.out.println(new ExitCommand(userCommand, isScript).execute());
    }

    /**
     * Adds new element to collection if it is minimal.
     */
    public void addIfMin() {
        collection = new AddIfMinCommand(collection, isScript, userCommand[1]).execute();
    }

    /**
     * Removes the element from collection by id if it is the greatest.
     */
    public void removeGreater(String id) {
        collection = new RemoveGreaterCommand(collection, id).execute();
    }

    /**
     * Removes the element from collection by id if it is the minimal.
     **/
    public void removeLower(String id) {
        collection = new RemoveLowerCommand(collection, id).execute();
    }

    /**
     * Prints the oldest author of any lab from collection.
     */
    public void maxByAuthor() {
        System.out.println(new MaxByAuthorCommand(collection).execute());
    }

    /**
     * Prints the all authors who was born earlier than input author.
     */
    public void countLessThanAuthor(String authorName) {
        System.out.println(new CountLessThanAuthorCommand(collection, authorName).execute());
    }

    /**
     * Sorts the elements of the collection by their difficulty.
     */
    public void filterByDifficulty(String difficultValue) {
        System.out.println(new FilterByDifficultyCommand(collection, difficultValue).execute());
    }

    /**
     * @return collection
     */
    public LinkedHashSet<LabWork> getCollection() {
        return collection;
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
        return collection.size();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(commandInformer, creationTimeOfCollection, commandReader, isScript, collection);
        result = 31 * result + Arrays.hashCode(userCommand);
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        CollectionManager that = (CollectionManager) otherObject;
        return isScript == that.isScript && Objects.equals(commandInformer, that.commandInformer)
                && Arrays.equals(userCommand, that.userCommand) && Objects.equals(creationTimeOfCollection, that.creationTimeOfCollection)
                && Objects.equals(commandReader, that.commandReader)
                && Objects.equals(collection, that.collection);
    }

    @Override
    public String toString() {
        return "Class for to manage the collection.";
    }
}