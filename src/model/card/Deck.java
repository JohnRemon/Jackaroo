package model.card;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.*;
import model.card.wild.Burner;
import model.card.wild.Saver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Deck {
   private static final String CARDS_FILE= "Cards.csv";
   private static ArrayList<Card> cardsPool;

    public static void loadCardPool(BoardManager boardManager, GameManager gameManager) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(CARDS_FILE));
    cardsPool = new ArrayList<>();
    String currentLine = null;
    int code, frequency, rank;
    String name, description;
    Suit suit;

    while ((currentLine = reader.readLine()) != null) {

        // parses the current line into an arraylist where each value is seperated by index
        ArrayList<String> currentCard = new ArrayList<>(Arrays.asList(currentLine.split(",")));
        try {
            code = Integer.parseInt(currentCard.get(0));
            frequency = Integer.parseInt(currentCard.get(1));
            name = currentCard.get(2);
            description = currentCard.get(3);

            if(code == 14 || code == 15) {
                rank = 0;
                suit = null;
            }else {
                rank = Integer.parseInt(currentCard.get(4));
                suit = Suit.valueOf(currentCard.get(5));
            }




            // decide on which constructor
            for (int i = 0; i < frequency; i++) {
                switch (code) {
                    case 0 -> cardsPool.add(new Standard(name, description, rank, suit, boardManager, gameManager));
                    case 1 -> cardsPool.add(new Ace(name, description, suit, boardManager, gameManager));
                    case 4 -> cardsPool.add(new Four(name, description, suit, boardManager, gameManager));
                    case 5 -> cardsPool.add(new Five(name, description, suit, boardManager, gameManager));
                    case 7 -> cardsPool.add(new Seven(name, description, suit, boardManager, gameManager));
                    case 10 -> cardsPool.add(new Ten(name, description, suit, boardManager, gameManager));
                    case 11 -> cardsPool.add(new Jack(name, description, suit, boardManager, gameManager));
                    case 12 -> cardsPool.add(new Queen(name, description, suit, boardManager, gameManager));
                    case 13 -> cardsPool.add(new King(name, description, suit, boardManager, gameManager));
                    case 14 -> cardsPool.add(new Burner(name, description, boardManager, gameManager));
                    case 15 -> cardsPool.add(new Saver(name, description, boardManager, gameManager));
                }
            }
        } catch (Exception e) {
            System.out.println("Could not parse card");
            System.out.println(e);
        }
    }
    reader.close();
}

    public static ArrayList<Card> drawCards() {
    Collections.shuffle(cardsPool);
    ArrayList<Card> cards = new ArrayList<>();
    // Assuming that if cardsPool is less than 4, it wants the current cards still
    for(int i = 0; i < 4 && !cardsPool.isEmpty(); i++) {
        cards.add(cardsPool.remove(0));
    }
    return cards;
    }

    public static void refillPool(ArrayList<Card> cards){
        cardsPool.addAll(cards);
    }
    public static int getPoolSize() {
        return cardsPool.size();
    }

}
