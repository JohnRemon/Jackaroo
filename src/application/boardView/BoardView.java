package application.boardView;

import engine.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Colour;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;

public class BoardView {

    @FXML
    private Pane boardPane;
    @FXML private Rectangle playerSafeZone;
    @FXML private Rectangle CPU1SafeZone;
    @FXML private Rectangle CPU2SafeZone;
    @FXML private Rectangle CPU3SafeZone;
    @FXML private Text playerTag;
    @FXML private Text CPU1Tag;
    @FXML private Text CPU2Tag;
    @FXML private Text CPU3Tag;

    private Game game;

    public void initGame(Game game) {
        this.game = game;

        ArrayList<Player> players = game.getPlayers();

        playerSafeZone.setFill(assignColour(players.get(0).getColour()));
        CPU1SafeZone.setFill(assignColour(players.get(1).getColour()));
        CPU2SafeZone.setFill(assignColour(players.get(2).getColour()));
        CPU3SafeZone.setFill(assignColour(players.get(3).getColour()));
    }

    private Paint assignColour(Colour colour) {
        return switch (colour) {
            case RED -> Color.RED;
            case YELLOW -> Color.YELLOW;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            default -> Color.BLACK;
        };
    }

    public static void setBoardPane(String playerName) throws IOException {
        FXMLLoader loader = new FXMLLoader(BoardView.class.getResource("BoardView.fxml"));
        Parent boardRoot = loader.load();
        BoardView controller = loader.getController();
        Game game = new Game(playerName);
        controller.initGame(game);
        controller.setNames(playerName);


        Stage gameStage = new Stage();
        gameStage.setScene(new Scene(boardRoot));
        gameStage.setTitle("Jackaroo");
        gameStage.show();
    }

    private void setNames(String playerName) {
        playerTag.setText(playerName);
        System.out.println(playerName);
        CPU1Tag.setText("Joe");
        CPU2Tag.setText("Jane");
        CPU3Tag.setText("Jack");
    }
}
