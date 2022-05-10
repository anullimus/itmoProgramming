package exception;

public class WrongCommandInputException extends RuntimeException{
    public WrongCommandInputException() {
        super("Передана неверная команда. Воспользуйтесь командами 'help' и 'man'");
    }
}
