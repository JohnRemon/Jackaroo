package exception;

public abstract class ActionException extends GameException {
    public ActionException() {}
    public ActionException(String message) {
        super(message);
    }
}
