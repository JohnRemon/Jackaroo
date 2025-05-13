package application;

import application.boardView.BoardView;
import engine.Game;
import exception.GameException;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.util.Duration;
import model.player.CPU;
import model.player.Player;


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
        System.out.println("-----------------------------------");
        System.out.println("FirePit Size: " + game.getFirePit().size());
        System.out.println(currentPlayer.getName() + " turn");
        System.out.println("Current Player Hand: " + currentPlayer.getHand().size());
        if (!(currentPlayer instanceof CPU)) {
            if(game.canPlayTurn()) {
                System.out.println("You can play the turn");
                scene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case DIGIT1 -> boardView.selectCard(0, game);
                        case DIGIT2 -> boardView.selectCard(1, game);
                        case DIGIT3 -> boardView.selectCard(2, game);
                        case DIGIT4 -> boardView.selectCard(3, game);
                        case ENTER -> {
                            System.out.println("Card: " + currentPlayer.getSelectedCard().getName());
                            try {
                                game.playPlayerTurn();
                                boardView.moveCard(game);
                                endTurn();
                            } catch (GameException e) {
                                boardView.showException(e.getMessage());
                            }
                        }
                    }
                });
            }else{
                System.out.println("You can't play the game after the skip");
                game.deselectAll();
                endTurn();
            }
        } else {
            if (game.canPlayTurn()) {
                System.out.println("You can play the turn");
                try {
                    game.playPlayerTurn();
                    boardView.moveCard(game);
                    endTurn();
                } catch (GameException e) {
                    boardView.showException(e.getMessage());
                }
            }else{
                System.out.println("You can't play the game after the skip");
                game.deselectAll();
                endTurn();
            }
        }
    }
    private void endTurn() {
        game.endPlayerTurn();
        boardView.updateCounters(game);
        boardView.assignCards(game);
        if (game.checkWin() != null) {
            String winner = game.getPlayers().get(game.checkWin().ordinal()).getName();
            System.out.println("Winner: " + winner);
        } else {
            handleTurn();
        }
    }

    public Game getGame() {
        return game;
    }
}
