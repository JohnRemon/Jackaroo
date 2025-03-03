package exception;

public class InvalidMarbleException extends InvalidSelectionException {
    public InvalidMarbleException() {}
    public InvalidMarbleException(String message) {
        super(message);
    }
}
