package com.company.server.command;

@FunctionalInterface
public interface Command<T> {
    T execute();
}

/*
//Receiver
class MyValidator(){}


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

// invoker
class CollectionManager{

    void addLabwork(){
        add.execute();
    }
    void clearLabwork(){
        clear.execute();
    }
}

// client
class App{

}

}

 */