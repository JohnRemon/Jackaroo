package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;

import java.util.ArrayList;

public class Jack extends Standard{
    public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager
            gameManager){
        super(name, description, 11, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles.size() == 2) { //means you won't use as a standard card
            //checks that at theres a marble owned by you, and another that isnt.
            if (!marbles.get(0).getColour().equals(gameManager.getActivePlayerColour()) &&
                !marbles.get(1).getColour().equals(gameManager.getActivePlayerColour()) &&
                !marbles.get(1).getColour().equals(marbles.get(0).getColour())) return false;
        } else
            return marbles.get(0).getColour().equals(gameManager.getActivePlayerColour());
        return true;
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        return marbles.size() == 2 || marbles.size() == 1;
    }
}
