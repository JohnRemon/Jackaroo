package exception;


import model.Colour;

public abstract class GameException extends Exception {
    public GameException() {}
    public GameException(String message) {
        super(message);
    }
}
