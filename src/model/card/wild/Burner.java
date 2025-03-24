package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.player.Marble;

import java.util.ArrayList;

public class Burner extends Wild{

    public Burner(String name, String description, BoardManager boardManager, GameManager gameManager){
        super(name,description,boardManager,gameManager);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles){
        return !gameManager.getActivePlayerColour().equals(marbles.get(0).getColour());
    }
}
