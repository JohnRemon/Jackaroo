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
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import model.Colour;
import model.player.CPU;
import model.player.Player;
import model.card.Card;

import java.util.ArrayList;

//note that the discarded card is placed first then the card that discards it
public class GameController {
    private final Game game;
    private final BoardView boardView;
    private Scene scene;


    public GameController(Game game, BoardView boardView, Scene scene) {
        this.game = game;
        this.boardView = boardView;
        this.scene = scene;

        game.setShuffleListener(new Game.ShuffleListener() {
            @Override
            public void onShuffle() {
                boardView.onShuffle();
            }
        });
    }

    public void handleTurn() {
        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayerIndex());
        Player nextPlayer = game.getPlayers().get(game.getNextPlayerIndex());

        boardView.moveCard(game);
        if (!(currentPlayer instanceof CPU)) {
            if(game.canPlayTurn()) {
                UserSettings.KeyBinds keyBinds = new UserSettings.KeyBinds().loadBinds();
                scene.setOnKeyPressed(event -> {
                    KeyCode code = event.getCode();

                    // Switch case doesn't allow non-constants meaning we can't use our keybinds in it, i know this is disgusting but live with it
                    if (code == keyBinds.card1) boardView.selectCard(0, game);
                    else if (code == keyBinds.card2) boardView.selectCard(1, game);
                    else if (code == keyBinds.card3) boardView.selectCard(2, game);
                    else if (code == keyBinds.card4) boardView.selectCard(3, game);
                    else if (code == keyBinds.playTurn) {
                        if(currentPlayer.getSelectedCard() != null && currentPlayer.getSelectedCard().getName().equals("Seven") && currentPlayer.getSelectedMarbles().size() == 2){
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
                    else if (code == keyBinds.skipTurn) {
                        if (currentPlayer.getSelectedCard() == null) {
                            int cardIndex = (int) (Math.random() * currentPlayer.getHand().size());
                            boardView.selectCard(cardIndex, game);
                        }
                        //game.endPlayerTurn();
                        endTurn();
                    }
                    else if (code == keyBinds.fieldMarble){
                        ArrayList<Card> hand = currentPlayer.getHand();
                        for (int i = 0; i < hand.size(); i++) {
                            if (hand.get(i).getName().equals("King") || hand.get(i).getName().equals("Ace")) {
                                currentPlayer.deselectAll();
                                boardView.selectCard(i, game);
                                try {
                                    game.playPlayerTurn();
                                    endTurn();
                                } catch (GameException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    else if (code == keyBinds.marble1) {
                        boardView.selectMarble(0);
                    }
                    else if (code == keyBinds.marble2) {
                        boardView.selectMarble(1);
                    }
                    else if (code == keyBinds.marble4) {
                        boardView.selectMarble(2);
                    }
                    else if (code == keyBinds.marble3) {
                        boardView.selectMarble(3);
                    }
                });
            }else{
                endTurn();
            }
        } else {
            if (game.canPlayTurn()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
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
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
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
