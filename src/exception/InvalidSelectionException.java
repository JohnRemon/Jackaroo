package exception;

public abstract class InvalidSelectionException extends GameException {
    public InvalidSelectionException() {}
    public InvalidSelectionException(String message) {
        super(message);
    }
}
