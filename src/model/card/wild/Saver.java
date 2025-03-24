package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;

import java.util.ArrayList;

public class Saver extends Wild {
    public Saver(String name, String description, BoardManager boardManager, GameManager gameManager){
        super(name, description, boardManager, gameManager);
    }

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        return marbles.size() == 1;
    }
}
