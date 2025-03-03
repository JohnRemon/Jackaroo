package exception;

public class SplitOutOfRangeException extends InvalidSelectionException {
    public SplitOutOfRangeException() {}
    public SplitOutOfRangeException(String message) {
        super(message);
    }
}
