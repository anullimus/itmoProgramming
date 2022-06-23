package exception;

public class ExitException extends RuntimeException {
    public ExitException(String client) {
        super("Client {" + client + "} has disconnected.");
    }
}
