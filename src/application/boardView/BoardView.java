package application.boardView;

import application.UserSettings;
import engine.Game;
import exception.InvalidCardException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.card.Card;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class BoardView {
    public ArrayList<ImageView> playerCardsImages = new ArrayList<>();
    @FXML public AnchorPane rootPane;
    @FXML public ImageView gridImage;
    @FXML public ImageView boardImage;
    @FXML public Label playerLabel;
    @FXML public Label CPU1Label;
    @FXML public Label CPU2Label;
    @FXML public Label CPU3Label;
    @FXML public Label CPU1RemainingCards;
    @FXML public Label CPU2RemainingCards;
    @FXML public Label CPU3RemainingCards;
    @FXML public HBox playerCardsRow;
    @FXML public TextArea cardDescription;
    @FXML public Button returnMainMenu;

    public void selectCard(int index, Game game) {
        try {
            Player player = game.getPlayers().get(0);
            Card select = player.getHand().get(index);
            player.selectCard(select);
            cardDescription.setText(select.getDescription());

            for (int i = 0; i < playerCardsImages.size(); i++) {
                ImageView iv = playerCardsImages.get(i);
                iv.setFitHeight(i == index ? 110 : 100);
            }

        } catch (InvalidCardException | IndexOutOfBoundsException e) {
            // TODO: add user feedback here
        }
    }

    public void initGame(Game game) {
        this.assignPlayers(game);
        this.assignCards(game);
    }

    public void assignCards(Game game) {
        playerCardsRow.getChildren().clear();
        playerCardsImages.clear();

        ArrayList<Card> playerCards = game.getPlayers().get(0).getHand();
        for (Card card : playerCards) {
            String imageLocation = "/view/textures/cards/" + card.getFileName();
            Image texture = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageLocation)));
            ImageView imageView = new ImageView(texture);

            imageView.setFitHeight(100);
            imageView.preserveRatioProperty().set(true);

            playerCardsRow.getChildren().add(imageView);
            playerCardsImages.add(imageView);
        }
    }

    public void assignPlayers(Game game) {
        UserSettings settings = new UserSettings();
        ArrayList<Player> players = game.getPlayers();

        playerLabel.setText(settings.getName());
        CPU1Label.setText("Gnurthuul");
        CPU2Label.setText("Qwutzhu'nal");
        CPU3Label.setText("Joe");

        playerLabel.setTextFill(players.get(0).getColourFX());
        CPU1Label.setTextFill(players.get(1).getColourFX());
        CPU2Label.setTextFill(players.get(2).getColourFX());
        CPU3Label.setTextFill(players.get(3).getColourFX());

        CPU1RemainingCards.setTextFill(players.get(1).getColourFX());
        CPU2RemainingCards.setTextFill(players.get(2).getColourFX());
        CPU3RemainingCards.setTextFill(players.get(3).getColourFX());
    }

    public void updateCounters(Game game) {
        ArrayList<Player> players = game.getPlayers();
        CPU1RemainingCards.setText(players.get(1).getHand().size() + "");
        CPU2RemainingCards.setText(players.get(2).getHand().size() + "");
        CPU3RemainingCards.setText(players.get(3).getHand().size() + "");
    }

    public void returnMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/TitleScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/application/title_screen.css").toExternalForm());

        Stage stage = (Stage) rootPane.getScene().getWindow(); // Reuse the current stage
        stage.setScene(scene);
        stage.centerOnScreen(); // Center the window on the screen
        stage.show();
    }
}
