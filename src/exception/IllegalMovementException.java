package exception;

public class IllegalMovementException extends ActionException {
    public IllegalMovementException() {}
    public IllegalMovementException(String message) {
        super(message);
    }
}
