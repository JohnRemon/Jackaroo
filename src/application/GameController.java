package application;

import application.boardView.BoardView;
import engine.Game;
import exception.GameException;
import exception.InvalidCardException;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
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
        System.out.println("-----------------------------------");
        System.out.println("FirePit Size: " + game.getFirePit().size());
        if(game.getFirePit().size() > 0) {
            for(Card c : game.getFirePit()) {
                System.out.println("Card in FirePit: " + c.getName());
            }
        }
        System.out.println(currentPlayer.getName() + " turn");
        System.out.println("Current Player Hand: " + currentPlayer.getHand().size());

        boardView.moveCard(game);
        if (!(currentPlayer instanceof CPU)) {
            if(game.canPlayTurn()) {
                System.out.println("You can play the turn");
                scene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case DIGIT1-> boardView.selectCard(0, game);
                        case DIGIT2 -> boardView.selectCard(1, game);
                        case DIGIT3 -> boardView.selectCard(2, game);
                        case DIGIT4 -> boardView.selectCard(3, game);
                        case ENTER -> {
                            System.out.println("Card: " + currentPlayer.getSelectedCard().getName());
                            try {
                                game.playPlayerTurn();
                                System.out.println("Card Moved");
                                endTurn();
                            } catch (GameException e) {
                                boardView.showException(e.getMessage());
                            }
                        }
                        case BACK_SPACE -> {
                            if (game.canPlayTurn()) {
                                //game.endPlayerTurn();
                                endTurn();
                            } else {
                                boardView.showException("You can't skip this turn.");
                            }
                        }
                    }
                });
            }else{
                System.out.println("You can't play the game after the skip");
                endTurn();
            }
        } else {
            if (game.canPlayTurn()) {
                System.out.println("You can play the turn");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> {
                    try {
                        game.playPlayerTurn();

                        System.out.println("CPU played a card");
                        endTurn();
                    } catch (GameException e) {
                        boardView.showException(e.getMessage());
                    }
                });
                pause.play();
            } else {
                System.out.println("You can't play the game after the skip");
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
            String winner = game.getPlayers().get(game.checkWin().ordinal()).getName();
            System.out.println("Winner: " + winner);

            boardView.playSound("win.mp3");

        } else {
            handleTurn();
        }
    }

    public Game getGame() {
        return game;
    }
}
