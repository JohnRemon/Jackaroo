package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;
import model.player.Marble;

import java.util.ArrayList;


public class Standard extends Card {
    private final int rank;
    private final Suit suit;

    public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
        this.rank = rank;
        this.suit = suit;
    }

    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        if (marbles.size() != 1)
            return false;
        return true;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}
