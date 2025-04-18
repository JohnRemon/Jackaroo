package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

import java.util.ArrayList;

public class Jack extends Standard{
    public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager
            gameManager){
        super(name, description, 11, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles.size() == 2) {
            Colour active = gameManager.getActivePlayerColour();
            Colour c1 = marbles.get(0).getColour();
            Colour c2 = marbles.get(1).getColour();

            boolean oneIsMine = c1.equals(active) || c2.equals(active);
            boolean notSameColour = !c1.equals(c2);

            return oneIsMine && notSameColour;
        }
// Standard card: only 1 marble — must belong to the current player
        return super.validateMarbleColours(marbles);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        return marbles.size() == 2 || marbles.size() == 1;
    }
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        boardManager.moveBy(marbles.getFirst(), this.getRank(), false);
    }
}
