package com.company.server.command;

import com.company.server.CollectionManager;
import com.company.server.CommandInformer;

/**
 * Prints full information about the command that manages the collection.
 */
public class InfoCommand implements Command<String> {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        StringBuilder collectionInfo = new StringBuilder(CommandInformer.PS1);
        collectionInfo.append("Тип коллекции: ").append(collectionManager.getCollection().getClass()).
                append("\n Дата инициализации: ").append(collectionManager.getCreationDate()).
                append("\n Количество элементов: ").append(collectionManager.getSize());
        return collectionInfo.toString();
    }
}

