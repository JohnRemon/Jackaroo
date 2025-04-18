package model.card;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;

import java.util.ArrayList;


public abstract class Card {
    private final String name;
    private final String description;
    protected BoardManager boardManager;
    protected GameManager gameManager;

    public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
        this.name = name;
        this.description = description;
        this.boardManager = boardManager;
        this.gameManager = gameManager;
    }


    public boolean validateMarbleSize(ArrayList<Marble> marbles){
        return marbles.size() == 1;
    }
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {

        return gameManager.getActivePlayerColour().equals(marbles.get(0).getColour());
    }
    // TODO: abstract void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException;

    public abstract void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //gameManager and boardManager getters
    public BoardManager getBoardManager() {
        return boardManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
