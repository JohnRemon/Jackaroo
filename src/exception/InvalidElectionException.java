package exception;

public abstract class InvalidElectionException extends GameException {
    public InvalidElectionException() {}
    public InvalidElectionException(String message) {
        super(message);
    }
}
