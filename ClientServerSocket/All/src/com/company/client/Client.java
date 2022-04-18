package com.company.client;

import com.company.server.logic.CollectionManager;

public class Client {
    public static void main(String[] args) {
        App app = new App(new CollectionManager(System.getenv("VARRY")));
        app.interactiveMode();
    }
}
