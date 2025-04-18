package engine;

import engine.board.Board;

import engine.board.Cell;
import engine.board.SafeZone;
import exception.*;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import static model.card.Deck.*;

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
        this.board = new Board(colours,this);

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
            ArrayList<Card> hand = drawCards();
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


    public void selectCard(Card card) throws InvalidCardException {
        Player player = players.get(currentPlayerIndex);
        player.selectCard(card);
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {
        Player player = players.get(currentPlayerIndex);
        player.selectMarble(marble);
    }

    public void deselectAll() {
        Player player = players.get(currentPlayerIndex);
        player.deselectAll();
    }

    public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException{
        if (splitDistance < 1 || splitDistance > 6) {
            throw new SplitOutOfRangeException("Split distance out of range!");
        } else {
            board.setSplitDistance(splitDistance);
        }
    }

    public boolean canPlayTurn() {
        Player player = players.get(currentPlayerIndex);
        return  player.getHand().isEmpty();
    }

    public void playPlayerTurn() throws GameException {
        Player player = players.get(currentPlayerIndex);
        player.play();
    }

    public void endPlayerTurn() {
        Player player = players.get(currentPlayerIndex);
        //remove the selected card and add it to the player
        firePit.add(player.getSelectedCard());
        player.getHand().remove(player.getSelectedCard());
        //deselect everything
        player.deselectAll();
        //move onto the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        //if player 4 then we start a new turn
        if(currentPlayerIndex == 0){
            turn++;
        }
        //start a new round
        if(turn % 4 == 0){
            //reset the turn
            turn = 0;
            //refill players' hands
            for(Player p : players){
                p.setHand(drawCards());
            }
            //refill the cardPool
            if(getPoolSize() < 4){
                refillPool(firePit);
                firePit.clear();
            }
        }
    }

    public Colour checkWin() {
        ArrayList<SafeZone> safeZones = board.getSafeZones();
        for (SafeZone safeZone : safeZones) {
            if (safeZone.isFull()) {
                return safeZone.getColour();
            }
        }
        return null;
    }

    @Override
    public void sendHome(Marble marble) {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.regainMarble(marble);
    }

    @Override
    public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
        Player currentPlayer = players.get(currentPlayerIndex);

        //check if there is a marble to field
        if(currentPlayer.getMarbles().isEmpty()){
            throw new CannotFieldException("There is no marble to field");
        }else{
            //get marble to field
            Marble marble = currentPlayer.getMarbles().get(0);
            //Send the marble to the board
            board.sendToBase(marble);
            currentPlayer.getMarbles().remove(marble);
        }
    }

    @Override
    public void discardCard(Colour colour) throws CannotDiscardException {
        Player player = null;
        for (Player playerSearch : players) {
            if (playerSearch.getColour().equals(colour)) {
                player = playerSearch;
            }
        }
        assert player != null;
        ArrayList<Card> hand = player.getHand();
        if (hand.isEmpty()) {
            throw new CannotDiscardException(player.getName() + " has an empty hand");
        }
        int handSize = hand.size();
        int rand = (int) (Math.random() * handSize);
        Card randCard = hand.get(rand);
        firePit.add(randCard);
        hand.remove(randCard);
    }

    @Override
    public void discardCard() throws CannotDiscardException {
        ArrayList<Colour> colours = new ArrayList<>();
        for(Player player : players){
            if(player != players.get(currentPlayerIndex)){
                colours.add(player.getColour());
            }
        }
        int rand = (int) (Math.random() * colours.size());
        discardCard(colours.get(rand));
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
