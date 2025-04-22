package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;

public class Burner extends Wild{

    public Burner(String name, String description, BoardManager boardManager, GameManager gameManager){
        super(name,description,boardManager,gameManager);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
        if (marbles.isEmpty()) return false;
        return !gameManager.getActivePlayerColour().equals(marbles.get(0).getColour());
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        System.out.println(marbles.toString());
        boardManager.destroyMarble(marbles.get(0));
    }
}
