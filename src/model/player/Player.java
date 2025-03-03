package model.player;

import model.Colour;
import model.card.Card;
import java.util.ArrayList;

public class Player {
    private final String name;
    private final Colour colour;
    private ArrayList<Card> hand;
    private final ArrayList<Marble> marbles;
    private final Card selectedCard;
    private final ArrayList<Marble> selectedMarbles;
    //READ getter
    //WRITE setter
    public Player(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
        this.hand = new ArrayList<>();
        this.marbles = new ArrayList<>();
        this.selectedMarbles = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Marble marble = new Marble(colour);
            marbles.add(marble);
        }
        this.selectedCard = null;
    }

    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Marble> getMarbles() {
        return new ArrayList<>(marbles);
    }

    public Card getSelectedCard() {
        return selectedCard;
    }
}
