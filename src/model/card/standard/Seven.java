package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;

public class Seven extends Standard{
    public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager){
        super(name, description, 7, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        if (marbles.isEmpty()) return false;
        return marbles.size() == 2 || marbles.size() == 1;
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles.size() == 2) {
            // if seven is used to move 2 marbles, check if both are owned by player
            if (!marbles.get(0).getColour().equals(gameManager.getActivePlayerColour())) return false;
            if (!marbles.get(0).getColour().equals(marbles.get(1).getColour())) return false;
        }
        //otherwise check if the lone marble is owned by player
        return super.validateMarbleColours(marbles);
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (!validateMarbleColours(marbles) || !validateMarbleSize(marbles)) throw new InvalidMarbleException("Invalid Marble selection");
        if (marbles.size() == 2) {
            int splitDistance = getBoardManager().getSplitDistance();

            boardManager.moveBy(marbles.getFirst(), splitDistance, false);
            boardManager.moveBy(marbles.getLast(), 7 - splitDistance, false);
        } else super.act(marbles);
    }
}
