package application.boardView;

import engine.Game;
import exception.GameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import model.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class BoardViewMedieval extends BoardView{
    public ImageView N8B;
    public ImageView N9B;
    public ImageView N11B;
    public ImageView N12B;
    public ImageView NOR2B;
    public ImageView NOR5B;
    public ImageView NOR6B;
    public ImageView NOR9B;
    public ImageView D3B;
    public ImageView D2B;
    public ImageView D4B;
    public ImageView D10B;
    public Pane skinPane;
    public Button skinButton;
    public ImageView playerSkin;
    private ImageView selectedSkin;
    private static MediaPlayer mediaPlayer;
    @FXML private Circle playerCircle;
    @FXML private Circle CPU1Circle;
    @FXML private Circle CPU2Circle;
    @FXML private Circle CPU3Circle;

    public static void setBoardPaneMedieval(String username) throws IOException, GameException {
        setBoardPane("BoardViewMedieval.fxml", username);
        playBackgroundMusic();
    }

    public void chooseSkin(ActionEvent actionEvent) {
        playSound("menuClick.mp3");
        skinPane.setVisible(true);
        setupSkinClickHandler(N8B);
        setupSkinClickHandler(N9B);
        setupSkinClickHandler(N11B);
        setupSkinClickHandler(N12B);
        setupSkinClickHandler(NOR2B);
        setupSkinClickHandler(NOR5B);
        setupSkinClickHandler(NOR6B);
        setupSkinClickHandler(NOR9B);
        setupSkinClickHandler(D3B);
        setupSkinClickHandler(D2B);
        setupSkinClickHandler(D4B);
        setupSkinClickHandler(D10B);
    }
    public void hideSkin(ActionEvent actionEvent) {
        if (selectedSkin != null) {
            Image chosenImage = selectedSkin.getImage();
            playerSkin.setImage(chosenImage);
            selectedSkin.setStyle("");
            selectedSkin = null;
        }
        skinPane.setVisible(false);
        playSound("menuClick.mp3");
        skinPane.getParent().getParent().requestFocus();
    }
    private void setupSkinClickHandler(ImageView imageView) {
        imageView.setOnMouseClicked(event -> {
            if (selectedSkin != null) {
                selectedSkin.setStyle("");
            }
            selectedSkin = imageView;
            selectedSkin.setStyle("-fx-effect: dropshadow(gaussian, yellow, 10, 0.5, 0, 0);");
        });
    }

    private static void playBackgroundMusic() throws IOException {
        Media media = new Media(new File("src/view/Sounds/cocMusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(getMusicVolume());
        mediaPlayer.play();
    }
    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void showException(String message){
        playSound("error.mp3");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Action");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/MedievalAlert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.LEFT));
        alert.show(); //showAndWait() throws an IllegalStateException if stepped on trap
    }

    public void showWinner(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(name + " has won the game!");
        alert.setGraphic(null);
        alert.setOnCloseRequest(event -> {
            try {
                returnMainMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/MedievalAlert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.LEFT));
        alert.showAndWait();
    }

    @Override
    public void updateCounters(Game game) {
        super.updateCounters(game);

        // Reset all circle styles
        playerCircle.setStyle("");
        CPU1Circle.setStyle("");
        CPU2Circle.setStyle("");
        CPU3Circle.setStyle("");

        // Highlight current player
        int currentPlayerIndex = game.getCurrentPlayerIndex();

        switch (currentPlayerIndex) {
            case 0 -> playerCircle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
            case 1 -> CPU1Circle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
            case 2 -> CPU2Circle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
            case 3 -> CPU3Circle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
        }

    }

    @Override
    public void assignPlayersClean(Game game) throws IOException{
        super.assignPlayersClean(game);
        playerCircle.setStyle("");
        CPU1Circle.setStyle("");
        CPU2Circle.setStyle("");
        CPU3Circle.setStyle("");

        // Highlight current player
        int currentPlayerIndex = game.getCurrentPlayerIndex();

        switch (currentPlayerIndex) {
            case 0 -> playerCircle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
            case 1 -> CPU1Circle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
            case 2 -> CPU2Circle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
            case 3 -> CPU3Circle.setStyle("-fx-effect: dropshadow(gaussian, sandybrown, 15, 0.7, 0, 0);");
        }
    }

}