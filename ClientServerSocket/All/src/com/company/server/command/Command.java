package com.company.server.command;

import com.company.common.data.initial.LabWork;
import com.company.common.util.Request;

@FunctionalInterface
public interface Command<T> {
    T execute();
}

//Receiver нам нафиг не нужен, все реализации команд будем осуществлять в самих классах команд

/*
//ConcreteCommand0
class Command0 implements Command{
    @Override
    public void execute(){
        System.out.println("adding...");
    }
}

//ConcreteCommand1
class Command1 implements Command{
    @Override
    public void execute(){
        System.out.println("clearing...");
    }
}

// client-invoker
class CollectionManager0{
    Command add;
    Command clear;

    public CollectionManager0(Command add, Command clear) {
        this.add = add;
        this.clear = clear;
    }
    void addLabwork(){
        add.execute();
    }
    void clearLabwork(){
        clear.execute();
    }
}

}

 */