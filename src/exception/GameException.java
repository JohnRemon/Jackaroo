package exception;


import model.Colour;

public abstract class GameException extends Exception {
    GameException() {}
    GameException(String message) {
        super(message);
    }
}
