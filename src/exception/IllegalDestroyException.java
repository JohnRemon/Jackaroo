package exception;

public class IllegalDestroyException extends ActionException {
    public IllegalDestroyException() {}
    public IllegalDestroyException(String message) {
        super(message);
    }
}
