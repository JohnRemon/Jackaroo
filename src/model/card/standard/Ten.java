package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;

import java.util.ArrayList;

public class Ten extends Standard {
    public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager){
        super(name, description, 10, suit, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        return marbles.isEmpty() || marbles.size() == 1;
    }
}
