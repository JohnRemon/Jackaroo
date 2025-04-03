package engine;

import engine.board.Board;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Game implements GameManager {

    private final Board board;
    private final ArrayList<Player> players;
    private final ArrayList<Card> firePit;
    private int currentPlayerIndex;
    private int turn;

    public Game(String playerName) throws IOException {


        //Created a new board with Game as game manager and randomised colours
        ArrayList<Colour> colours = new ArrayList<>();
        Collections.addAll(colours, Colour.values());
        Collections.shuffle(colours);
        this.board = new Board(colours, this);

        //Loaded the card pool
        Deck.loadCardPool(board, this);

        //Created the Players
        Player humanPlayer = new Player((playerName == null ? "Player" : playerName), colours.get(0));
        CPU cpu1 = new CPU("CPU 1", colours.get(1), board);
        CPU cpu2 = new CPU("CPU 2", colours.get(2), board);
        CPU cpu3 = new CPU("CPU 3", colours.get(3), board);

        //Added the Players to the players ArrayList
        this.players = new ArrayList<>();
        Collections.addAll(players, humanPlayer, cpu1, cpu2, cpu3);

        //Created a hand for every Player
        for (Player player : players) {
            ArrayList<Card> hand = Deck.drawCards();
            player.setHand(hand);
        }

        //Set the PlayerInced and Turn to 0
        this.currentPlayerIndex = 0;
        this.turn = 0;

        //Initialized the firePit
        this.firePit = new ArrayList<>();

    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Card> getFirePit() {
        return firePit;
    }

    void selectCard(Card card) throws InvalidCardException {
        
    }

    @Override
    public void sendHome(Marble marble) {

    }

    @Override
    public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
        Player currentPlayer = players.get(currentPlayerIndex);

        //get the marble to field
        Marble marble = currentPlayer.getOneMarble();

        //check if there is a marble to field
        if(marble == null){
            throw new CannotFieldException("There is no marble to field");
        }else{
            //Send the marble to the board
            board.sendToBase(marble);
            currentPlayer.getMarbles().remove(marble);
        }
        //TODO: revise
    }

    @Override
    public void discardCard(Colour colour) throws CannotDiscardException {

    }

    @Override
    public void discardCard() throws CannotDiscardException {

    }

    @Override
    public Colour getActivePlayerColour() {
        return players.get(currentPlayerIndex).getColour();
    }

    @Override
    public Colour getNextPlayerColour() {
        return players.get(currentPlayerIndex + 1).getColour();
    }


}
