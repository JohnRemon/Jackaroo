package engine;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.CPU;
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

}
