package exception;

/**
 * exception class shows that script contains error
 */
public class ReadElementException extends RuntimeException {
    public ReadElementException(String message) {
        super(message);
    }
}
