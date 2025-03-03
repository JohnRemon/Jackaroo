package exception;

public class InvalidCardException extends InvalidSelectionException {
    public InvalidCardException() {}
    public InvalidCardException(String message) {
        super(message);
    }
}
