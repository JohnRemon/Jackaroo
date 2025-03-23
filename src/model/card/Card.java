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
    //el document m2alsh en dol msh abstract, bs it makes zero sense enohom mykonoosh abstract
    //since they're about to get overridden in each card class. In addition, you can't aslan
    //validate considering you can't access the subclass' attributes (in this case we need the ID of the card
    //to know how many marbles should be selected).

    public boolean validateMarbleSize(ArrayList<Marble> marbles);
    public boolean validateMarbleColours(ArrayList<Marble> marbles);
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException;

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
