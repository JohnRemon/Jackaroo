package application.boardView;

import exception.GameException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    public static void setBoardPaneMedieval(String username) throws IOException, GameException {
        setBoardPane("BoardViewMedieval.fxml", username);
        playBackgroundMusic();
    }

    public void chooseSkin(ActionEvent actionEvent) {
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
}