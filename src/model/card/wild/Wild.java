package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.Card;

public abstract class Wild extends Card {

    private final String image;

    public Wild(String name, String description, BoardManager boardManager, GameManager gameManager, String image) {
        super(name, description, boardManager, gameManager);
        this.image = image;
    }
}
