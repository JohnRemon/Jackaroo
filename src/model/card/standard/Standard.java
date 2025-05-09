package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.card.Card;
import model.player.Marble;

import java.util.ArrayList;


public class Standard extends Card {
    private final int rank;
    private final Suit suit;
    private final String image;

    public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
        this.rank = rank;
        this.suit = suit;
        this.image = String.valueOf(rank)+suit+".png";
    }

    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {

        for (Marble marble : marbles)
            boardManager.moveBy(marble, this.getRank(), false);
    }

    public int getRank() {
        return rank;
    }
    public Suit getSuit() {
        return suit;
    }
    public String getFileName() {
        return image;
    }
}
