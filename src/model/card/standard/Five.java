package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;

import java.util.ArrayList;

public class Five extends Standard{
    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager){
        super(name, description, 5, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
        //5 accepts ANY marble colour
        return true;
    }
}
