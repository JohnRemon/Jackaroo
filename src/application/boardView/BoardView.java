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
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
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
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.card.Card;
import model.player.Marble;
import model.player.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.media.AudioClip;

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

    private ArrayList<MarbleMapping> P1MarbleMappings = new ArrayList<>();
    @FXML private Circle PlayerMarbleOne;
    @FXML private Circle PlayerMarbleTwo;
    @FXML private Circle PlayerMarbleThree;
    @FXML private Circle PlayerMarbleFour;
    @FXML private HBox PlayerHomeZone;
    @FXML private GridPane PlayerSafeZone;

    private ArrayList<MarbleMapping> CPU1MarbleMappings = new ArrayList<>();
    @FXML private Circle CPU1MarbleOne;
    @FXML private Circle CPU1MarbleTwo;
    @FXML private Circle CPU1MarbleThree;
    @FXML private Circle CPU1MarbleFour;
    @FXML private HBox CPU1HomeZone;
    @FXML private GridPane CPU1SafeZone;

    private ArrayList<MarbleMapping> CPU2MarbleMappings = new ArrayList<>();
    @FXML private Circle CPU2MarbleOne;
    @FXML private Circle CPU2MarbleTwo;
    @FXML private Circle CPU2MarbleThree;
    @FXML private Circle CPU2MarbleFour;
    @FXML private HBox CPU2HomeZone;
    @FXML private GridPane CPU2SafeZone;

    private ArrayList<MarbleMapping> CPU3MarbleMappings = new ArrayList<>();
    @FXML private Circle CPU3MarbleOne;
    @FXML private Circle CPU3MarbleTwo;
    @FXML private Circle CPU3MarbleThree;
    @FXML private Circle CPU3MarbleFour;
    @FXML private HBox CPU3HomeZone;
    @FXML private GridPane CPU3SafeZone;

    @FXML private GridPane gridInshallah;
    private ArrayList<HBox> homeZones = new ArrayList<>();
    private ArrayList<GridPane> safeZones = new ArrayList<>();
    private Board board;

    private final ArrayList<Marble> globallySelectedMarbles = new ArrayList<>();

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

        playSound("click.mp3");
    }


    public void selectMarble(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        ArrayList<Player> players = gameController.getGame().getPlayers();
        Player currentPlayer = players.get(gameController.getGame().getCurrentPlayerIndex());

        ArrayList<MarbleMapping> allMappings = new ArrayList<>();
        allMappings.addAll(P1MarbleMappings);
        allMappings.addAll(CPU1MarbleMappings);
        allMappings.addAll(CPU2MarbleMappings);
        allMappings.addAll(CPU3MarbleMappings);

        Marble marble = null;
        for (MarbleMapping mapping : allMappings) {
            if (circle.equals(mapping.getCircle())) {
                marble = mapping.getMarble();
                break;
            }
        }

        boolean isSelected = (Boolean) circle.getProperties().getOrDefault("selected", false);

        try {
            if (isSelected) {
                circle.getProperties().put("selected", false);
                circle.setRadius(circle.getRadius() - 2);
                currentPlayer.getSelectedMarbles().remove(marble);
                System.out.println("Deselected Marble " + marble);
            } else {
                currentPlayer.selectMarble(marble);
                circle.getProperties().put("selected", true);
                circle.setRadius(circle.getRadius() + 2);
                System.out.println("Selected Marble " + marble);
            }

            System.out.println("Current Player Selected Marbles: " + currentPlayer.getSelectedMarbles());
        } catch (InvalidMarbleException e) {
            showException(e.getMessage());
        }
    }

    public void resetAllMarbleSelections() {
        ArrayList<MarbleMapping> allMappings = new ArrayList<>();
        allMappings.addAll(P1MarbleMappings);
        allMappings.addAll(CPU1MarbleMappings);
        allMappings.addAll(CPU2MarbleMappings);
        allMappings.addAll(CPU3MarbleMappings);

        for (MarbleMapping mapping : allMappings) {
            Circle circle = mapping.getCircle();
            circle.getProperties().put("selected", false);
            circle.setRadius(10);
        }

        // Clear all players' selectedMarbles
        for (Player player : gameController.getGame().getPlayers()) {
            player.getSelectedMarbles().clear();
        }

        System.out.println("All marbles and selections have been reset.");
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

        playSound("shuffle.mp3");
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

        // Assign  marbles
        ArrayList<Marble> p1marbles = game.getPlayers().getFirst().getMarbles();
        MarbleMapping p1Marble1 = new MarbleMapping(p1marbles.getFirst(), PlayerMarbleOne);
        MarbleMapping p1Marble2 = new MarbleMapping(p1marbles.get(1), PlayerMarbleTwo);
        MarbleMapping p1Marble3 = new MarbleMapping(p1marbles.get(2), PlayerMarbleThree);
        MarbleMapping p1Marble4 = new MarbleMapping(p1marbles.get(3), PlayerMarbleFour);

        // Set marble colors based on player colors
        p1Marble1.getCircle().setFill(players.get(0).getColourFX());
        p1Marble2.getCircle().setFill(players.get(0).getColourFX());
        p1Marble3.getCircle().setFill(players.get(0).getColourFX());
        p1Marble4.getCircle().setFill(players.get(0).getColourFX());
        P1MarbleMappings.add(p1Marble1);
        P1MarbleMappings.add(p1Marble2);
        P1MarbleMappings.add(p1Marble3);
        P1MarbleMappings.add(p1Marble4);

        ArrayList<Marble> CPU1marbles = game.getPlayers().get(1).getMarbles();
        MarbleMapping p2Marble1 = new MarbleMapping(CPU1marbles.get(0), CPU1MarbleOne);
        MarbleMapping p2Marble2 = new MarbleMapping(CPU1marbles.get(1), CPU1MarbleTwo);
        MarbleMapping p2Marble3 = new MarbleMapping(CPU1marbles.get(2), CPU1MarbleThree);
        MarbleMapping p2Marble4 = new MarbleMapping(CPU1marbles.get(3), CPU1MarbleFour);
        p2Marble1.getCircle().setFill(players.get(1).getColourFX());
        p2Marble2.getCircle().setFill(players.get(1).getColourFX());
        p2Marble3.getCircle().setFill(players.get(1).getColourFX());
        p2Marble4.getCircle().setFill(players.get(1).getColourFX());
        CPU1MarbleMappings.add(p2Marble1);
        CPU1MarbleMappings.add(p2Marble2);
        CPU1MarbleMappings.add(p2Marble3);
        CPU1MarbleMappings.add(p2Marble4);

        ArrayList<Marble> CPU2marbles = game.getPlayers().get(2).getMarbles();
        MarbleMapping p3Marble1 = new MarbleMapping(CPU2marbles.get(0), CPU2MarbleOne);
        MarbleMapping p3Marble2 = new MarbleMapping(CPU2marbles.get(1), CPU2MarbleTwo);
        MarbleMapping p3Marble3 = new MarbleMapping(CPU2marbles.get(2), CPU2MarbleThree);
        MarbleMapping p3Marble4 = new MarbleMapping(CPU2marbles.get(3), CPU2MarbleFour);
        p3Marble1.getCircle().setFill(players.get(2).getColourFX());
        p3Marble2.getCircle().setFill(players.get(2).getColourFX());
        p3Marble3.getCircle().setFill(players.get(2).getColourFX());
        p3Marble4.getCircle().setFill(players.get(2).getColourFX());
        CPU2MarbleMappings.add(p3Marble1);
        CPU2MarbleMappings.add(p3Marble2);
        CPU2MarbleMappings.add(p3Marble3);
        CPU2MarbleMappings.add(p3Marble4);


        ArrayList<Marble> CPU3marbles = game.getPlayers().get(3).getMarbles();
        MarbleMapping p4Marble1 = new MarbleMapping(CPU3marbles.get(0), CPU3MarbleOne);
        MarbleMapping p4Marble2 = new MarbleMapping(CPU3marbles.get(1), CPU3MarbleTwo);
        MarbleMapping p4Marble3 = new MarbleMapping(CPU3marbles.get(2), CPU3MarbleThree);
        MarbleMapping p4Marble4 = new MarbleMapping(CPU3marbles.get(3), CPU3MarbleFour);
        p4Marble1.getCircle().setFill(players.get(3).getColourFX());
        p4Marble2.getCircle().setFill(players.get(3).getColourFX());
        p4Marble3.getCircle().setFill(players.get(3).getColourFX());
        p4Marble4.getCircle().setFill(players.get(3).getColourFX());
        CPU3MarbleMappings.add(p4Marble1);
        CPU3MarbleMappings.add(p4Marble2);
        CPU3MarbleMappings.add(p4Marble3);
        CPU3MarbleMappings.add(p4Marble4);

        homeZones.add(PlayerHomeZone);
        homeZones.add(CPU1HomeZone);
        homeZones.add(CPU2HomeZone);
        homeZones.add(CPU3HomeZone);
        safeZones.add(PlayerSafeZone);
        safeZones.add(CPU1SafeZone);
        safeZones.add(CPU2SafeZone);
        safeZones.add(CPU3SafeZone);
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

    public void updateMarbles(int pos, MarbleMapping mp, Game game) {
        ArrayList<int[]> grid = GridLoader.getGrid();
        Circle c = mp.getCircle();


            if (pos < 100) //pos is on track
            {
                if (c.getParent() instanceof HBox){
                    ((HBox)c.getParent()).getChildren().remove(c);
                }
                int[] point = grid.get(pos);
                GridPane.setRowIndex(c, point[0]);
                GridPane.setColumnIndex(c, point[1]);
                placeMarbleOnTopOfGrid(c, gridInshallah, rootPane, point[0], point[1]);
            } else {
                //if in safezone OR in homezone
                int safeOrHome = pos/100;
               // int playerIndex = board.getMarbleOwnerIndex(mp.getMarble());
                int playerIndex = (pos/10)%10;
               // System.out.println("SafeOrHome: " + safeOrHome);
                if (safeOrHome == 1) //safezone
                {
                    int cellIndex = pos%10;
                    GridPane sz = safeZones.get(playerIndex);
                    if (c.getParent() instanceof Pane parent) {
                        parent.getChildren().remove(c);
                    }

                    GridPane.setRowIndex(c, cellIndex);
                    GridPane.setColumnIndex(c, 0);
                    sz.getChildren().add(c);
                }else {
                    if (!(c.getParent() instanceof HBox))
                        homeZones.get(playerIndex).getChildren().add(c);
        }

        }

    }
    public void updateMarbles(Board.MarblePosition newPosition, MarbleMapping mp) {
        ArrayList<int[]> grid = GridLoader.getGrid();
        int position = newPosition.index();
        int playerIndex = newPosition.playerIndex();
        Board.PlaceType pt = newPosition.type();
        Circle c = mp.getCircle();
        c.getParent(); // for the love of everything near and dear, do NOT remove this line.

        if (pt == Board.PlaceType.TRACK)
        {
            if (c.getParent() instanceof HBox)
                ((HBox)c.getParent()).getChildren().remove(c);

            int[] point = grid.get(position);

            GridPane.setRowIndex(c, point[0]);
            GridPane.setColumnIndex(c, point[1]);

            //gridInshallah.getChildren().add(c);
            placeMarbleOnTopOfGrid(c, gridInshallah, rootPane, point[0], point[1]);

            return;
        }


        HBox targetBox = homeZones.get(playerIndex);
        if (pt == Board.PlaceType.HOMEZONE)
        {
            c.getParent(); // for the love of everything near and dear, do NOT remove this line.
            if (c.getParent() != targetBox) {
                if (c.getParent() instanceof Pane)
                    ((Pane) c.getParent()).getChildren().remove(c);
                targetBox.getChildren().add(c);
                return;
           }
//            if (!(c.getParent() instanceof HBox))
//                homeZones.get(playerIndex).getChildren().add(c);
        }

        c.getParent(); // for the love of everything near and dear, do NOT remove this line.
        GridPane targetPane = safeZones.get(playerIndex);
        if (pt == Board.PlaceType.SAFEZONE) {
            if (c.getParent() != targetPane){
                if (c.getParent() instanceof Pane)
                    ((Pane) c.getParent()).getChildren().remove(c);

            if (c.getParent() instanceof Pane parent) {
                parent.getChildren().remove(c);
            }

            GridPane.setRowIndex(c, position);
            GridPane.setColumnIndex(c, 0);

            targetPane.getChildren().add(c);
            return;
            }
//            GridPane sz = safeZones.get(playerIndex);
//            if (c.getParent() instanceof Pane parent) {
//                parent.getChildren().remove(c);
//            }
//
//            GridPane.setRowIndex(c, position);
//            GridPane.setColumnIndex(c, 0);
//            sz.getChildren().add(c);

        }
    }

    public void moveMarbles(Game game){
        /*
            This method should take the game, find all the marbles whose position has changed, and animate those marbles
        */
        ArrayList<Player> players = game.getPlayers();

        for (Player p : players) {
            ArrayList<MarbleMapping> mapping = getMapping(p, game);
            ArrayList<Board.MarblePosition> newPositions =  board.getMarblePositions(mapping);

            for (int i = 0; i < mapping.size(); i++){
                updateMarbles(newPositions.get(i), mapping.get(i));

            }
        }

        playSound("ball_Rolling.mp3");
    }




    public ArrayList<MarbleMapping> getMapping(Player p, Game game){
        ArrayList<Player> players = game.getPlayers();
        int counter = 0;
        for (Player play : players)
        {
            if (p.equals(play))
                break;
            counter++;
        }
        switch (counter)
        {
            case 0 -> {
                return P1MarbleMappings;
            }
            case 1 -> {
                return CPU1MarbleMappings;
            }
            case 2 -> {
                return CPU2MarbleMappings;
            }
            case 3 -> {
                return CPU3MarbleMappings;
            }
            default -> {
                return null;
            }
        }
    }

    public void moveCard(Game game) {
        if(!game.getFirePit().isEmpty() && game.getFirePit().getLast() != null) {
            String imageLocation = "/view/textures/cards/" + game.getFirePit().getLast().getFileName();
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
            imageView.setLayoutX(362.5 - imageView.getFitWidth() / 2);
            imageView.setLayoutY(390 - imageView.getFitHeight() / 2);
            rootPane.getChildren().add(imageView);
        }
    }
    public void placeMarbleOnTopOfGrid(Circle marble, GridPane grid, AnchorPane root, int row, int col) {
        Node tile = getTileAt(grid, col, row); // Note: col, then row
        if (tile == null) {
            System.out.println("No tile found at (" + row + ", " + col + ")");
            return;
        }

        // Convert tile's bounds to root coordinate space
        Bounds sceneBounds = tile.localToScene(tile.getBoundsInLocal());
        Bounds localBounds = root.sceneToLocal(sceneBounds);

        // Remove from any old parent
        if (marble.getParent() != null && marble.getParent() instanceof Pane parent) {
            parent.getChildren().remove(marble);
        }

        // Add to rootPane and manually position
        root.getChildren().add(marble);
        double tileWidth = tile.getBoundsInLocal().getWidth();
        double tileHeight = tile.getBoundsInLocal().getHeight();

        marble.setLayoutX(localBounds.getMinX() + tileWidth / 2);
        marble.setLayoutY(localBounds.getMinY() + tileHeight / 2);
    }

    private Node getTileAt(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);
            if ((c != null && c == col) && (r != null && r == row)) {
                return node;
            }
        }
        return null;
    }


    // SOUND EFFECTS

    public void playSound(String fileName) {
        try {
            String path = "/view/Sounds/" + fileName;
            Media sound = new Media(getClass().getResource(path).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Failed to play sound: " + fileName + " -> " + e.getMessage());
        }
    }



}
