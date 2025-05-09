package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;

public class Saver extends Wild {
    public Saver(String name, String description, BoardManager boardManager, GameManager gameManager){
        super(name, description, boardManager, gameManager, "saver.png");
    }

    public String getFileName() {
        return "saver.png";
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        boardManager.sendToSafe(marbles.getFirst());
    }
}
