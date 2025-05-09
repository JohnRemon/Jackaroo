package application.boardView;

import application.UserSettings;
import engine.Game;
import exception.InvalidCardException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class BoardViewAlien {
     ArrayList<ImageView> playerCardsImages = new ArrayList<>();
    @FXML AnchorPane rootPane;
    @FXML  ImageView gridImage;
    @FXML  ImageView boardImage;
    @FXML Label playerLabel;
    @FXML Label CPU1Label;
    @FXML Label CPU2Label;
    @FXML Label CPU3Label;
    @FXML Label CPU1RemainingCards;
    @FXML Label CPU2RemainingCards;
    @FXML Label CPU3RemainingCards;
    @FXML HBox playerCardsRow;
    @FXML TextArea cardDescription;


    public static void setBoardPane(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(BoardViewAlien.class.getResource("BoardViewAlien.fxml"));
        Parent boardRoot = loader.load();
        BoardViewAlien controller = loader.getController();
        Game game = new Game(username);
        //set all the static objects in place
        controller.initGame(game);


        Stage gameStage = new Stage();
        Scene gameScene = new Scene(boardRoot);
        gameStage.setScene(gameScene);
        gameStage.setTitle("Jackaroo");


        gameScene.setOnKeyPressed(e -> {
            System.out.println(e.getCode() + " pressed");
            switch (e.getCode()) {
                case DIGIT1 -> controller.selectCard(0, game);
                case DIGIT2 -> controller.selectCard(1, game);
                case DIGIT3 -> controller.selectCard(2, game);
                case DIGIT4 -> controller.selectCard(3, game);
            }
        });

        gameStage.show();
        gameScene.getRoot().requestFocus(); //ensure the anchorpane is taking input correctly
        //msh hat3b nafsy delwa2ty
        gameStage.setResizable(false);

        /*
        //auto-scale images depending on window size
        controller.boardImage.fitWidthProperty().bind(controller.rootPane.widthProperty());
        controller.boardImage.fitHeightProperty().bind(controller.rootPane.heightProperty());
        controller.gridImage.fitWidthProperty().bind(controller.rootPane.widthProperty());
        controller.gridImage.fitHeightProperty().bind(controller.rootPane.heightProperty());
        //TODO: force window to only scale while maintaining aspect ratio
        */

    }

private void selectCard(int index, Game game) {
    try {
        Player player = game.getPlayers().get(0);
        Card select = player.getHand().get(index);
        player.selectCard(select);
        cardDescription.setText(select.getDescription());

        for (int i = 0; i < playerCardsImages.size(); i++) {
            ImageView iv = playerCardsImages.get(i);
            iv.setFitHeight(i == index ? 110 : 100); // only selected card grows
        }

    } catch (InvalidCardException | IndexOutOfBoundsException e) {
        // TODO: error sound or feedback
    }
}


    private  void initGame(Game game) {
        //assign icons, assign colors, assign names
        this.assignPlayers(game);
        this.assignCards(game);
    }

    private void assignCards(Game game) {
        //clear any previous cards for new turn
        playerCardsRow.getChildren().clear();
        playerCardsImages.clear();

        ArrayList<Card> playerCards = game.getPlayers().get(0).getHand();
        int counter = 1;

        for (Card card : playerCards) {
            String imageLocation = "/view/textures/cards/" + card.getFileName();
            Image texture = new Image(getClass().getResourceAsStream(imageLocation));
            ImageView imageView = new ImageView(texture);

            imageView.setFitHeight(100);
            imageView.preserveRatioProperty().set(true);

            playerCardsRow.getChildren().add(imageView);
            playerCardsImages.add(imageView);
            counter++;
        }
    }

    private void assignPlayers(Game game) {
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

    private void updateCounters(Game game) {
        ArrayList<Player> players = game.getPlayers();
        CPU1RemainingCards.setText(players.get(1).getHand().size() + "");
        CPU2RemainingCards.setText(players.get(2).getHand().size() + "");
        CPU3RemainingCards.setText(players.get(3).getHand().size() + "");
    }

}
