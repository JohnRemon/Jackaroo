package application;

import application.boardView.BoardView;
import engine.Game;
import exception.GameException;
import exception.InvalidMarbleException;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.card.Deck;
import model.player.CPU;
import model.player.Player;

import java.io.IOException;

import static model.card.Deck.getPoolSize;

public class GameController {
    private final Game game;
    private final BoardView boardView;
    private Scene scene;

    public GameController(Game game, BoardView boardView, Scene scene) {
        this.game = game;
        this.boardView = boardView;
        this.scene = scene;
    }

    public void handleTurn() {
        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayerIndex());
        Player nextPlayer = game.getPlayers().get(game.getNextPlayerIndex());

        System.out.println(currentPlayer.getMarbles().size());

        System.out.println(currentPlayer.getName() + " turn");

        if (!(currentPlayer instanceof CPU)) {
            scene.setOnKeyPressed(event -> {
                System.out.println(event.getCode());
                switch (event.getCode()) {
                    case DIGIT1 -> boardView.selectCard(0, game);
                    case DIGIT2 -> boardView.selectCard(1, game);
                    case DIGIT3 -> boardView.selectCard(2, game);
                    case DIGIT4 -> boardView.selectCard(3, game);
                    case ENTER -> {
                        System.out.println("ENTER key detected");
                        if (game.canPlayTurn()) {
                            try {
                                game.playPlayerTurn();
                                System.out.println("Turn played");
                                endTurn();
                            } catch (GameException e) {
                                e.printStackTrace(); // show error to player?
                            }
                        }
                    }
                }
                boardView.updateCounters(game);
            });
        } else {
            if (game.canPlayTurn()) {
                try {
                    game.playPlayerTurn();
                    System.out.println("Turn played");
                    endTurn();
                } catch (GameException e) {
                    e.printStackTrace(); // show error to player?
                }
            }
        }
    }
    private void endTurn() {
        System.out.println("End turn");
        game.endPlayerTurn();
        boardView.updateCounters(game);
        boardView.assignCards(game);

        if (game.checkWin() != null) {
            String winner = game.getPlayers().get(game.checkWin().ordinal()).getName();
            System.out.println("Winner: " + winner);
        } else {
            System.out.println("Next turn");
            handleTurn();
        }
    }
}
