package application.boardView;

import engine.Game;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Colour;
import model.player.Player;
import javafx.scene.media.AudioClip;

import java.io.IOException;
import java.util.ArrayList;

public class BoardView {

    @FXML
    private Pane boardPane;
    @FXML
    private Rectangle playerSafeZone;
    @FXML
    private Rectangle CPU1SafeZone;
    @FXML
    private Rectangle CPU2SafeZone;
    @FXML
    private Rectangle CPU3SafeZone;
    @FXML
    private Text playerTag;
    @FXML
    private Text CPU1Tag;
    @FXML
    private Text CPU2Tag;
    @FXML
    private Text CPU3Tag;

    @FXML
    private Pane playerHomeZone;
    @FXML
    private Pane cpu1HomeZone;
    @FXML
    private Pane cpu2HomeZone;
    @FXML
    private Pane cpu3HomeZone;

    @FXML
    private Rectangle firePit;


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

        controller.buildTrack();

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

    @FXML
    private Pane trackpane;

    private void buildTrack() {
        trackpane.getChildren().clear();

        // Wait until layout pass is complete to get accurate dimensions
        trackpane.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.setOnShown(e -> {
                            double width = trackpane.getWidth();
                            double height = trackpane.getHeight();
                            drawTrack(width, height);
                        });
                    }
                });
            }
        });
    }

    private void drawTrack(double width, double height) {
        int cellSize = 20;
        double radius = 250;
        double centerX = width / 2;
        double centerY = height / 2;

        for (int i = 0; i < 100; i++) {
            double angle = 2 * Math.PI * i / 100;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            Circle cell = new Circle(cellSize / 2);
            cell.setCenterX(x);
            cell.setCenterY(y);
            cell.setFill(Color.LIGHTGRAY);
            cell.setStroke(Color.BLACK);

            trackpane.getChildren().add(cell);
        }
    }

    // Firepit

    private AudioClip burnSound;

    @FXML
    public void initialize() {
//        burnSound = new AudioClip(getClass().getResource("application/Sounds/Big fire burning - Sound Effect.mp3").toExternalForm());

        // has an issue with returning null will solve it later

        addHoverEffect(cardImage);
    }

    Node cardImage = null;

    @FXML
    private void burnCard() {
        burnSound.play();

        // Cards integrated here....


        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), cardImage);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), cardImage);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.0);
        scale.setToY(0.0);

        ParallelTransition burnEffect = new ParallelTransition(fade, scale);
        burnEffect.setOnFinished(e -> {
            ((Pane) cardImage.getParent()).getChildren().remove(cardImage);
        });

        burnEffect.play();
    }


    // card hovering

    private void addHoverEffect(Node node) {
        DropShadow glow = new DropShadow(20, Color.GOLD);

        node.setOnMouseEntered(e -> {
            node.setEffect(glow);

            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
        });

        node.setOnMouseExited(e -> {
            node.setEffect(null);

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
    }
}
