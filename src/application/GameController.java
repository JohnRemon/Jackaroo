package application;

import application.boardView.BoardView;
import engine.Game;
import exception.GameException;
import javafx.scene.Scene;
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
        System.out.println(currentPlayer.getName() + " turn");

        if (!(currentPlayer instanceof CPU)) {
            scene.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case DIGIT1 -> boardView.selectCard(0, game);
                    case DIGIT2 -> boardView.selectCard(1, game);
                    case DIGIT3 -> boardView.selectCard(2, game);
                    case DIGIT4 -> boardView.selectCard(3, game);
                    case ENTER -> {
                        if (game.canPlayTurn()) {
                            try {
                                game.playPlayerTurn();
                                //boardView.moveMarble(game);
                                endTurn();
                            } catch (GameException e) {
                                boardView.showException(e.getMessage());
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
                    endTurn();
                } catch (GameException e) {
                    boardView.showException(e.getMessage());
                }
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
