package application;

import application.boardView.BoardView;
import engine.Game;
import exception.GameException;
import exception.InvalidCardException;
import exception.SplitOutOfRangeException;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;
import model.Colour;
import model.player.CPU;
import model.player.Player;
import model.card.Card;

//note that the discarded card is placed first then the card that discards it
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

        boardView.moveCard(game);
        if (!(currentPlayer instanceof CPU)) {
            if(game.canPlayTurn()) {
                scene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case DIGIT1 -> boardView.selectCard(0, game);
                        case DIGIT2 -> boardView.selectCard(1, game);
                        case DIGIT3 -> boardView.selectCard(2, game);
                        case DIGIT4 -> boardView.selectCard(3, game);
                        case ENTER -> {
                            if(currentPlayer.getSelectedCard().getName().equals("Seven") && currentPlayer.getSelectedMarbles().size() == 2){
                                TextInputDialog dialog = new TextInputDialog();
                                dialog.setTitle("Enter Split Distance");
                                dialog.setHeaderText(null);
                                dialog.setContentText("Split Distance:");
                                dialog.setGraphic(null);
                                dialog.showAndWait().ifPresent(input -> {
                                    try {
                                        int splitDistance = Integer.parseInt(input);
                                        game.editSplitDistance(splitDistance);
                                    } catch (NumberFormatException | SplitOutOfRangeException e) {
                                        boardView.showException("Invalid input. Please enter a valid number.");
                                    }
                                });
                            }
                            try {
                                game.playPlayerTurn();
                                endTurn();
                            } catch (GameException e) {
                                boardView.showException(e.getMessage());
                            }
                        }
                        case BACK_SPACE -> {
                                if (currentPlayer.getSelectedCard() == null) {
                                    int cardIndex = (int) (Math.random() * currentPlayer.getHand().size());
                                    boardView.selectCard(cardIndex, game);
                                }
                                endTurn();
                        }
                    }
                });
            }else{
                endTurn();
            }
        } else {
            if (game.canPlayTurn()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(0));
                pause.setOnFinished(event -> {
                    try {
                        game.playPlayerTurn();
                        endTurn();
                    } catch (GameException e) {
                        boardView.showException(e.getMessage());
                    }
                });
                pause.play();
            } else {
                game.deselectAll();
                PauseTransition pause = new PauseTransition(Duration.seconds(0));
                pause.setOnFinished(event -> endTurn());
                pause.play();
            }
        }
    }
    private void endTurn() {
        boardView.moveMarbles(game);
        game.endPlayerTurn();
        boardView.resetAllMarbleSelections();
        boardView.updateCounters(game);
        boardView.assignCards(game);
        if (game.checkWin() != null) {
            Colour colour = game.checkWin();
            for (Player player : game.getPlayers()) {
                if (player.getColour() == colour) {
                    boardView.showWinner(player.getName());
                    break;
                }
            }

        } else {
            handleTurn();
        }
    }

    public Game getGame() {
        return game;
    }
}
