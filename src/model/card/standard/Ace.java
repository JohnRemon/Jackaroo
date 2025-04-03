package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;

public class Ace extends Standard{
    public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager
            gameManager){
        super(name, description, 1, suit, boardManager, gameManager);
    }

    public boolean validateMarbleSize(ArrayList<Marble> marbles)
    {
        return marbles.size() < 2;
    }


    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles.isEmpty())
            gameManager.fieldMarble();
        else
            boardManager.moveBy(marbles.getFirst(), this.getRank(),false);
    }
}
