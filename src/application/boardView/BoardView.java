package application.boardView;

import application.PegLoader;
import engine.Game;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Integer, Circle> trackCells = new HashMap<>();

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
        trackpane.getChildren().clear(); // Clear existing cells
        System.out.println("Drawing track with width: " + width + ", height: " + height);

        // Load traced positions from JSON
        List<Point2D> pegPositions = PegLoader.loadPegsFromJson();

        if (pegPositions == null || pegPositions.isEmpty()) {
            System.err.println("Error: Failed to load peg positions or list is empty.");
            return;
        }

        // Take the first 100 positions if more are present
        List<Point2D> trimmedPositions = pegPositions.subList(0, Math.min(100, pegPositions.size()));
        if (trimmedPositions.size() != 100) {
            System.err.println("Warning: Trimmed to " + trimmedPositions.size() + " peg positions.");
        } else {
            System.out.println("Successfully loaded 100 peg positions.");
        }

        // Log the first and last points for debugging
        System.out.println("First point: x=" + (width * trimmedPositions.get(0).getX()) + ", y=" + (height * trimmedPositions.get(0).getY()));
        System.out.println("Last point: x=" + (width * trimmedPositions.get(99).getX()) + ", y=" + (height * trimmedPositions.get(99).getY()));

        // Draw the track cells using the trimmed positions
        for (int i = 0; i < trimmedPositions.size(); i++) {
            Point2D relPos = trimmedPositions.get(i);
            double x = width * relPos.getX();
            double y = height * relPos.getY();
            Circle peg = new Circle(x, y, 7, Color.BLACK);
            peg.setStroke(Color.WHITE);
            peg.setStrokeWidth(1.5);
            peg.setId("cell" + i); // Add ID for reference
            trackCells.put(i, peg);
            trackpane.getChildren().add(peg);
        }
        System.out.println("Track drawing complete. Added " + trackCells.size() + " cells.");
    }

    // Firepit

    private AudioClip burnSound;

    @FXML
    public void initialize() {
//        burnSound = new AudioClip(getClass().getResource("application/Sounds/Big fire burning - Sound Effect.mp3").toExternalForm());

        // has an issue with returning null will solve it later

//        addHoverEffect(cardImage);
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

}
