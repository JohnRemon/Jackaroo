package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;
import model.player.Player;

import java.util.ArrayList;

public class Queen extends Standard{
    public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager
            gameManager){
        super(name, description, 12, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        return marbles.isEmpty() || marbles.size() == 1;
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles.isEmpty())
            gameManager.discardCard();
        else
            super.act(marbles);
    }

}
