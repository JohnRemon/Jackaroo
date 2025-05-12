package application.boardView;

import application.GameController;
import application.Main;
import application.MainMenuController;
import application.UserSettings;
import engine.Game;
import engine.board.Board;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.card.Card;
import model.player.Marble;
import model.player.Player;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class BoardView {
    public ArrayList<ImageView> playerCardsImages = new ArrayList<>();
    @FXML public AnchorPane rootPane;
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
    @FXML private Circle PlayerMarbleOne;
    @FXML private Circle PlayerMarbleTwo;
    @FXML private Circle PlayerMarbleThree;
    @FXML private Circle PlayerMarbleFour;

    @FXML private Circle CPU1MarbleOne;
    @FXML private Circle CPU1MarbleTwo;
    @FXML private Circle CPU1MarbleThree;
    @FXML private Circle CPU1MarbleFour;

    @FXML private Circle CPU2MarbleOne;
    @FXML private Circle CPU2MarbleTwo;
    @FXML private Circle CPU2MarbleThree;
    @FXML private Circle CPU2MarbleFour;

    @FXML private Circle CPU3MarbleOne;
    @FXML private Circle CPU3MarbleTwo;
    @FXML private Circle CPU3MarbleThree;
    @FXML private Circle CPU3MarbleFour;

    @FXML private GridPane gridInshallah;
    private Board board;

    private final HashMap<Circle, Marble> marbleMapping = new HashMap<>();
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


    public void selectMarble(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        Player humanPlayer = gameController.getGame().getPlayers().get(0);
        Marble marble = marbleMapping.get(circle);

        if(circle.getFill().equals(humanPlayer.getColourFX())){
            Boolean isSelected = (Boolean) circle.getProperties().getOrDefault("selected", false);
            try{
                if(isSelected){
                    circle.getProperties().put("selected", false);
                    circle.setRadius(circle.getRadius() - 2);
                    humanPlayer.getSelectedMarbles().remove(marble);
                }else{
                    circle.getProperties().put("selected", true);
                    circle.setRadius(circle.getRadius() + 2);
                    humanPlayer.selectMarble(marble);
                }
            }catch(InvalidMarbleException e){
                e.printStackTrace();
            }
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

        playerLabel.setTextFill(players.get(0).getColourFX());
        CPU1Label.setTextFill(players.get(1).getColourFX());
        CPU2Label.setTextFill(players.get(2).getColourFX());
        CPU3Label.setTextFill(players.get(3).getColourFX());

        CPU1RemainingCards.setTextFill(players.get(1).getColourFX());
        CPU2RemainingCards.setTextFill(players.get(2).getColourFX());
        CPU3RemainingCards.setTextFill(players.get(3).getColourFX());

        // Set marble colors based on player colors
        PlayerMarbleOne.setFill(players.get(0).getColourFX());
        PlayerMarbleTwo.setFill(players.get(0).getColourFX());
        PlayerMarbleThree.setFill(players.get(0).getColourFX());
        PlayerMarbleFour.setFill(players.get(0).getColourFX());

        CPU1MarbleOne.setFill(players.get(1).getColourFX());
        CPU1MarbleTwo.setFill(players.get(1).getColourFX());
        CPU1MarbleThree.setFill(players.get(1).getColourFX());
        CPU1MarbleFour.setFill(players.get(1).getColourFX());

        CPU2MarbleOne.setFill(players.get(2).getColourFX());
        CPU2MarbleTwo.setFill(players.get(2).getColourFX());
        CPU2MarbleThree.setFill(players.get(2).getColourFX());
        CPU2MarbleFour.setFill(players.get(2).getColourFX());

        CPU3MarbleOne.setFill(players.get(3).getColourFX());
        CPU3MarbleTwo.setFill(players.get(3).getColourFX());
        CPU3MarbleThree.setFill(players.get(3).getColourFX());
        CPU3MarbleFour.setFill(players.get(3).getColourFX());

    }

    public void updateCounters(Game game) {
        ArrayList<Player> players = game.getPlayers();

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
    //implement a timer
    //implement the marble mover
    //implement the card moving into fire pit

    public static void setBoardPane(String fxml, String username) throws IOException, GameException {
        FXMLLoader loader = new FXMLLoader(BoardView.class.getResource(fxml));
        Parent boardRoot = loader.load();
        BoardView controller = loader.getController();
        Game game = new Game(username);
        Board board = game.getBoard();
        controller.setBoard(board);

        // Create the Scene and set it to the Stage
        Scene gameScene = new Scene(boardRoot);
        Stage stage = (Stage) Main.getPrimaryStage();
        stage.setScene(gameScene);
        stage.setTitle("Jackaroo");
        stage.setResizable(false);
        stage.centerOnScreen();
        boardRoot.requestFocus();
        controller.testTrack(game);
        stage.show();

        // Initialize the game and pass the Scene explicitly
        GameController gameController = new GameController(game, controller, gameScene);
        controller.setGameController(gameController);
        controller.assignPlayers(game);
        controller.assignCards(game);
        gameController.handleTurn();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public void setBoard(Board board){
        this.board = board;
    }

    private void testTrack(Game game){
        GridLoader.loadGrid();
        ArrayList<int[]> grid = GridLoader.getGrid();
        for (int i = 0; i < grid.size(); i++)
        {
            Circle marble = new Circle();
            marble.setFill(Color.BLACK);
            marble.setRadius(10);

            int[] point = grid.get(i);
            GridPane.setRowIndex(marble, point[0]);
            GridPane.setColumnIndex(marble, point[1]);

            gridInshallah.getChildren().add(marble);
        }
    }

    public void showException(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Action");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
