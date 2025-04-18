package model.player;

import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import model.Colour;
import model.card.Card;
import java.util.ArrayList;

public class Player {
    private final String name;
    private final Colour colour;
    private ArrayList<Card> hand;
    private final ArrayList<Marble> marbles;
    private  Card selectedCard;
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

    public void regainMarble(Marble marble){
        marbles.add(marble);
    }

    public Marble getOneMarble(){
        return this.marbles.isEmpty() ? null : this.marbles.getFirst();
    }

    public void selectCard(Card card) throws InvalidCardException {
        if (this.hand.contains(card)){
            selectedCard = card;
        } else {
            throw new InvalidCardException("Card is not in hand");
        }
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {
        if (selectedMarbles.size() >= 2)
            throw new InvalidMarbleException("Cannot select more than two marbles!");
        else selectedMarbles.add(marble);
    }

    public void deselectAll(){
        selectedCard = null;
        selectedMarbles.clear();
    }


    public void play() throws GameException {
        if (selectedCard == null)
            throw new InvalidCardException("No card selected!");
        if (!(selectedCard.validateMarbleColours(selectedMarbles) || selectedCard.validateMarbleSize(selectedMarbles)))
            throw new InvalidMarbleException("Invalid marbles!");
        //TODO: make card do it's required functionallity after Mr. Sanad finishes his class
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
