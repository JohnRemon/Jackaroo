package application.boardView;

import application.GameController;
import application.Main;
import application.MainMenuController;
import application.UserSettings;
import engine.Game;
import exception.GameException;
import exception.InvalidCardException;
import javafx.embed.swing.JFXPanel;
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
import model.Colour;
import model.card.Card;
import model.player.Player;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import model.card.Deck;

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
    private GameController gameController;

    public void selectCard(int index, Game game) {
        try {
            Player player = game.getPlayers().get(0);
            Card select = player.getHand().get(index);
            player.selectCard(select);
            cardDescription.setText(select.getDescription());

            for (int i = 0; i < playerCardsImages.size(); i++) {
                ImageView iv = playerCardsImages.get(i);
                iv.setFitHeight(i == index ? 100 : 90);
            }

        } catch (InvalidCardException | IndexOutOfBoundsException e) {
            // TODO: add user feedback here
        }
    }


    public void assignCards(Game game) {
        playerCardsRow.getChildren().clear();
        playerCardsImages.clear();

        ArrayList<Card> playerCards = game.getPlayers().get(0).getHand();
        for (Card card : playerCards) {
            String imageLocation = "/view/textures/cards/" + card.getFileName();
            Image texture = null;
            InputStream stream = getClass().getResourceAsStream(imageLocation);
            if (stream == null) {
                System.out.println("Image not found at: " + imageLocation);
            } else {
                texture = new Image(stream);
            }

            ImageView imageView = new ImageView(texture);

            imageView.setFitHeight(90);
            imageView.preserveRatioProperty().set(true);

            playerCardsRow.getChildren().add(imageView);
            playerCardsImages.add(imageView);
        }
    }

    public void assignPlayers(Game game) throws IOException {
        UserSettings settings = new UserSettings().LoadSettings();
        ArrayList<Player> players = game.getPlayers();

        playerLabel.setText(settings.getName());
        CPU1Label.setText("CPU 1");
        CPU2Label.setText("CPU 2");
        CPU3Label.setText("CPU 3");

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

        System.out.println("Player Remaining Cards: " + players.get(0).getHand().size());
        System.out.println("CPU1 Remaining Cards: " + players.get(1).getHand().size());
        System.out.println("CPU2 Remaining Cards: " + players.get(2).getHand().size());
        System.out.println("CPU3 Remaining Cards: " + players.get(3).getHand().size());

        CPU1RemainingCards.setText(players.get(1).getHand().size() + "");
        CPU2RemainingCards.setText(players.get(2).getHand().size() + "");
        CPU3RemainingCards.setText(players.get(3).getHand().size() + "");
    }
    public void returnMainMenu() throws IOException {
        UserSettings currentSettings = new UserSettings().LoadSettings();
        currentSettings.SaveSettings(currentSettings);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/TitleScreen.fxml"));
        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.loadValues();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/application/title_screen.css").toExternalForm());

        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    //implement current player turn
    //implement next player turn
    //implement a timer
    //implement the marble mover
    //implement the card moving into fire pit

    public static void setBoardPane(String fxml, String username) throws IOException, GameException {
        FXMLLoader loader = new FXMLLoader(BoardView.class.getResource(fxml));
        Parent boardRoot = loader.load();
        BoardView controller = loader.getController();
        Game game = new Game(username);

        // Create the Scene and set it to the Stage
        Scene gameScene = new Scene(boardRoot);
        Stage stage = (Stage) Main.getPrimaryStage();
        stage.setScene(gameScene);
        stage.setTitle("Jackaroo");
        stage.setResizable(false);
        stage.centerOnScreen();
        boardRoot.requestFocus();
        stage.show();

        // Initialize the game and pass the Scene explicitly
        GameController gameController = new GameController(game, controller, gameScene);
        controller.assignPlayers(game);
        controller.assignCards(game);
        gameController.handleTurn();
    }
}
