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
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.card.Card;
import model.player.CPU;
import model.player.Marble;
import model.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BoardView {

    public ArrayList<ImageView> playerCardsImages = new ArrayList<>();
    @FXML public AnchorPane rootPane;
    @FXML public ImageView boardImage;
    @FXML public Label playerLabel;
    @FXML public Label CPU1Label;
    @FXML public Label CPU2Label;
    @FXML public Label CPU3Label;
    ArrayList<Label> playerLabels = new ArrayList<>();
    @FXML public Label CPU1RemainingCards;
    @FXML public Label CPU2RemainingCards;
    @FXML public Label CPU3RemainingCards;
    ArrayList<Label> CPUCounter = new ArrayList<>();
    @FXML public HBox playerCardsRow;
    @FXML public Label cardDescription;
    @FXML public TextArea cardDescriptionOld;
    @FXML public Button returnMainMenu;
    private GameController gameController;
    @FXML public Label playerTurnNow;

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
    public static boolean trapped = false;
    public static UserSettings settings = new UserSettings();

    @FXML
    Label CurrentPlayerLabel;
    @FXML
    Label NextPlayerLabel;
    @FXML private ImageView firePitLastCard;


    public static void setBoardPane(String fxml, String username) throws IOException, GameException {
        FXMLLoader loader = new FXMLLoader(BoardView.class.getResource(fxml));
        Parent boardRoot = loader.load();
        BoardView controller = loader.getController();
        Game game = new Game(username);
        Board board = game.getBoard();
        controller.setBoard(board);
        settings = settings.LoadSettings();

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
        controller.assignPlayersClean(game);
        controller.assignCards(game);
        controller.assignCardsAnimation(game);
        gameController.handleTurn();
    }

    public void selectCard(int index, Game game) {
        try {
            Player player = game.getPlayers().get(0);
            Card select = player.getHand().get(index);

            //deselect card part
            if (playerCardsImages.get(index).getFitHeight() == 100)
            {
                playerCardsImages.get(index).setFitHeight(90);
                if (settings.getTheme().equals("Alien"))
                cardDescription.setText(null);
                else
                    cardDescriptionOld.setText(null);

                playerCardsImages.get(index).setEffect(null);
                if (game.getCurrentPlayerIndex() != 0)
                    greyOutCards(playerCardsImages, false);
                return;
            }
            player.selectCard(select);
            if (settings.getTheme().equals("Alien"))
            {
                cardDescription.setText(select.getDescription());
                cardDescription.getStyleClass().remove("error");
                cardDescription.getStyleClass().add("info");
            }
            else
                cardDescriptionOld.setText(select.getDescription());

            for (int i = 0; i < playerCardsImages.size(); i++) {
                ImageView iv = playerCardsImages.get(i);
                Effect originaleffect = iv.getEffect();
                iv.setEffect(null);
                iv.setScaleX(1.0);
                iv.setScaleY(1.0);
                if(i == index){
                    iv.setFitHeight(100);
                    DropShadow glow = new DropShadow();
                    glow.setColor(Color.LIMEGREEN);
                    glow.setRadius(20);

                    if (originaleffect instanceof ColorAdjust) //stacks the effects of greying out card (if not turn) & the glow
                        glow.setInput(originaleffect);

                    iv.setEffect(glow);
                }else{
                    iv.setEffect(null);
                    iv.setFitHeight(90);
                }
            }
            if (game.getCurrentPlayerIndex() != 0)
            {
                greyOutCards(playerCardsImages, false);
            }
        } catch (InvalidCardException | IndexOutOfBoundsException e) {
            playSound("error.mp3");
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
            } else {
                currentPlayer.selectMarble(marble);
                circle.getProperties().put("selected", true);
                circle.setRadius(circle.getRadius() + 2);
            }
            playSound("click.mp3");


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

    }

    public void assignCards(Game game) {
        playerCardsRow.getChildren().clear();
        playerCardsImages.clear();


        CurrentPlayerLabel.setText("Current Player: " + game.getPlayers().get(game.getCurrentPlayerIndex()).getName());
        CurrentPlayerLabel.setTextFill(game.getPlayers().get(game.getCurrentPlayerIndex()).getColourFX());
        NextPlayerLabel.setText("Next Player: " + game.getPlayers().get(game.getNextPlayerIndex()).getName());
        NextPlayerLabel.setTextFill(game.getPlayers().get(game.getNextPlayerIndex()).getColourFX());

        String theme = settings.getTheme();
        if (theme.equals("Alien"))
        {
            for (int i = 0; i < 4; i++)
            {
                playerLabels.get(i).getStyleClass().setAll("player-name");
                playerLabels.get(i).setText(game.getPlayers().get(i).getName());


                if (i != 0 )
                    CPUCounter.get(i-1).getStyleClass().setAll("player-name");
            }

            playerLabels.get(game.getCurrentPlayerIndex()).getStyleClass().add("current-turn");
            playerLabels.get(game.getCurrentPlayerIndex()).setText("► " + game.getPlayers().get(game.getCurrentPlayerIndex()).getName() + " ◄");
            if (game.getCurrentPlayerIndex() != 0) {
                CPUCounter.get(game.getCurrentPlayerIndex() - 1).getStyleClass().setAll("current-turn");
                playerTurnNow.setVisible(false);
            } else {
                playerTurnNow.setVisible(true);
            }
        }

        ArrayList<Card> playerCards = game.getPlayers().get(0).getHand();
        for (int i = 0; i < playerCards.size(); i++) {
            Card card = playerCards.get(i);
            String imageLocation = "/view/textures/cards/" + card.getFileName();
            if (card.getFileName().equals("saver.png") || card.getFileName().equals("burner.png"))
                imageLocation = "/view/textures/" + settings.getTheme() + "/" + card.getFileName();
            InputStream stream = getClass().getResourceAsStream(imageLocation);
            Image texture = (stream != null) ? new Image(stream) : null;

            ImageView imageView = new ImageView(texture);
            imageView.setFitHeight(90);
            imageView.setPreserveRatio(true);
            imageView.setEffect(null);

            int index = i; //for the love of everything near and dear bardo, dont remove
            imageView.setOnMouseClicked(event -> selectCard(index, game));

            playerCardsRow.getChildren().add(imageView);
            playerCardsImages.add(imageView);
        }
        if (game.getCurrentPlayerIndex() == 0) {
            greyOutCards(playerCardsImages, true);
        } else greyOutCards(playerCardsImages, false);

    }

    public void assignCardsAnimation(Game game) {
        playerCardsRow.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            for (int i = 0; i < playerCardsImages.size(); i++) {
                ImageView card = playerCardsImages.get(i);
                double cardCenterX = card.localToScene(card.getBoundsInLocal()).getMinX() + card.getBoundsInLocal().getWidth() / 2;

                double distance = Math.abs(mouseX - cardCenterX);

                double scale = closerBigger(1.0 + (150 - distance) / 300, 1.0, 1.2);
                double rotate = closerBigger((cardCenterX-mouseX) / 30, -10, 2);
                double offset = closerBigger((150 - distance) / 5, 0, 20);



                card.setScaleX(scale);
                card.setScaleY(scale);
                card.setTranslateY(-offset);
                card.setRotate(-rotate);
            }
        });

        playerCardsRow.setOnMouseExited(event -> {
            for (ImageView card : playerCardsImages) {
                card.setScaleX(1.0);
                card.setScaleY(1.0);
                card.setRotate(0);
                card.setTranslateY(0);
            }
        });
    }

    public void assignPlayersClean(Game game) throws IOException {
        UserSettings settings = new UserSettings().LoadSettings();
        ArrayList<Player> players = game.getPlayers();

        // ---Names and cards---
        String[] names = { settings.getName(), "CPU 1", "CPU 2", "CPU 3" };
        Label[] nameLabels = { playerLabel, CPU1Label, CPU2Label, CPU3Label };
        Label[] countLabels = { null, CPU1RemainingCards, CPU2RemainingCards, CPU3RemainingCards };

        // ---Name Lables and Colors---
        for (int i = 0; i < players.size(); i++) {
            nameLabels[i].setText(names[i]);
            nameLabels[i].setTextFill(players.get(i).getColourFX());

            if (i > 0) countLabels[i].setTextFill(players.get(i).getColourFX());

            playerLabels.add(nameLabels[i]);
        }

        // ---Marble Lables---
        Circle[][] marbleCircles = {
                { PlayerMarbleOne, PlayerMarbleTwo, PlayerMarbleThree, PlayerMarbleFour },
                { CPU1MarbleOne, CPU1MarbleTwo, CPU1MarbleThree, CPU1MarbleFour },
                { CPU2MarbleOne, CPU2MarbleTwo, CPU2MarbleThree, CPU2MarbleFour },
                { CPU3MarbleOne, CPU3MarbleTwo, CPU3MarbleThree, CPU3MarbleFour }
        };

        ArrayList<MarbleMapping>[] marbleMappings = new ArrayList[] {
                P1MarbleMappings, CPU1MarbleMappings, CPU2MarbleMappings, CPU3MarbleMappings
        };

        // ---Marble Colors---
        for (int i = 0; i < players.size(); i++) {
            ArrayList<Marble> playerMarbles = players.get(i).getMarbles();
            for (int j = 0; j < 4; j++) {
                Circle circle = marbleCircles[i][j];
                circle.setStroke(Color.BLACK);
                circle.setEffect(new DropShadow(5, Color.rgb(0,0,0,0.6)));

                Color playerColor = (Color) players.get(i).getColourFX(); // whatever color you assigned to that player
                circle.setFill(makeMarbleGradient(playerColor));

                MarbleMapping map = new MarbleMapping(playerMarbles.get(j), circle);
                marbleMappings[i].add(map);
            }
        }

        homeZones.addAll(List.of(PlayerHomeZone, CPU1HomeZone, CPU2HomeZone, CPU3HomeZone));
        safeZones.addAll(List.of(PlayerSafeZone, CPU1SafeZone, CPU2SafeZone, CPU3SafeZone));

        for (int i =0; i < safeZones.size(); i++) {
            Color color = (Color)players.get(i).getColourFX();
            GridPane safeZone = safeZones.get(i);
            safeZone.setStyle("-fx-border-color: #00ccff");
            safeZone.setStyle("-fx-effect: dropshadow(gaussian, " + toRgbString(color) + ", 10, 0.5, 0, 0);");
        }

        CPU1RemainingCards.setText(players.get(1).getHand().size() + "");
        CPU2RemainingCards.setText(players.get(2).getHand().size() + "");
        CPU3RemainingCards.setText(players.get(3).getHand().size() + "");
        CPUCounter.add(CPU1RemainingCards);
        CPUCounter.add(CPU2RemainingCards);
        CPUCounter.add(CPU3RemainingCards);

    }
    public void returnMainMenu() throws IOException {

        playSound("menuClick.mp3");
        BoardViewMedieval.stopMusic();
        UserSettings currentSettings = new UserSettings().LoadSettings();
        currentSettings.SaveSettings(currentSettings);
        UserSettings.KeyBinds keyBinds = new UserSettings.KeyBinds().loadBinds();
        keyBinds.saveBinds(keyBinds);


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



    private void testTrack(Game game) throws IOException {
        GridLoader.loadGrid();
        ArrayList<int[]> grid = GridLoader.getGrid();
        for (int i = 0; i < grid.size(); i++)
        {
            Circle marble = new Circle();
            marble.setRadius(9);
            marble.getStyleClass().add("track-cell");

            int[] point = grid.get(i);
            GridPane.setRowIndex(marble, point[0]);
            GridPane.setColumnIndex(marble, point[1]);

            gridInshallah.getChildren().add(marble);
        }
    }

    public void showException(String message){
        playSound("error.mp3");
        if (settings.getTheme().equals("Alien"))
        {
            System.out.println(message);
            cardDescription.getStyleClass().setAll("error");
          //  cardDescription.getStyleClass().add("error");
            cardDescription.setText("Invalid Action!" + "\n" + message);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show(); //showAndWait() throws an IllegalStateException if stepped on trap
        }
    }

    public void updateMarbles(Board.MarblePosition newPosition, MarbleMapping mp) {
        ArrayList<int[]> grid = GridLoader.getGrid();
        int position = newPosition.index();
        int playerIndex = newPosition.playerIndex();
        Board.PlaceType pt = newPosition.type();
        Circle c = mp.getCircle();

        HBox targetBox = homeZones.get(playerIndex);
        if (pt == Board.PlaceType.HOMEZONE)
        {
            c.getParent();// for the love of everything near and dear, do NOT remove this line.
            if (c.getParent() != targetBox) {
                if (trapped){
                    updateTrapFlag();
                    BoardViewAlien.playTeleportEffect(c, gridInshallah);
                    if (!(gameController.getGame().getPlayers().get(gameController.getGame().getCurrentPlayerIndex()) instanceof CPU)) {
                        javafx.application.Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Trap Activated");
                            alert.setHeaderText(null);
                            alert.setContentText("You have been teleported to your Home Zone!");
                            alert.show();
                        });
                    }
                }

                if (c.getParent() instanceof Pane)
                    ((Pane) c.getParent()).getChildren().remove(c);
                targetBox.getChildren().add(c);
            }
            return;
        }

        int[] point = grid.get(position);
        Integer oldRow = GridPane.getRowIndex(c);   //used wrapper class Integer as RowIndex and ColumnIndex can be null (wasn't on grid before)
        Integer oldCol = GridPane.getColumnIndex(c);
        int newRow = point[0];
        int newCol = point[1];
        boolean positionChanged = oldCol == null || oldRow == null || oldRow != newRow || oldCol != newCol;

        if (pt == Board.PlaceType.TRACK)
        {
            c.getParent();
            if (c.getParent() instanceof HBox) {
                ((HBox) c.getParent()).getChildren().remove(c);
            }

            if (positionChanged)
            {
                BoardViewAlien.playTeleportEffect(c, gridInshallah);
            }

            GridPane.setRowIndex(c, point[0]);
            GridPane.setColumnIndex(c, point[1]);
            placeMarbleOnTopOfGrid(c, gridInshallah, rootPane, point[0], point[1]);

            return;
        }

        GridPane targetPane = safeZones.get(playerIndex);
        if (pt == Board.PlaceType.SAFEZONE) {
            c.getParent(); // for the love of everything near and dear, do NOT remove this line.
            if (c.getParent() instanceof Pane parent) {
                parent.getChildren().remove(c);
            }

            positionChanged = position != GridPane.getRowIndex(c);
            if (positionChanged)
            {
                BoardViewAlien.playTeleportEffect(c, gridInshallah);
            }

            GridPane.setRowIndex(c, position);
            GridPane.setColumnIndex(c,0);
            targetPane.getChildren().add(c);
            return;
        }
    }

    public void moveMarbles(Game game){
        ArrayList<Player> players = game.getPlayers();

        for (Player p : players) {
            ArrayList<MarbleMapping> mapping = getMapping(p, game);
            ArrayList<Board.MarblePosition> newPositions =  board.getMarblePositions(mapping);

            for (int i = 0; i < mapping.size(); i++){
                updateMarbles(newPositions.get(i), mapping.get(i));

            }
            playSound("marble.mp3");  // always plays the sound
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
            firePitLastCard.setImage(imageView.getImage());
        }
    }

    public static void playSound(String fileName) {
        try {
            UserSettings userSettings = new UserSettings().LoadSettings();
            String path = "/view/Sounds/" + fileName;
            Media sound = new Media(BoardView.class.getResource(path).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(userSettings.getSfx() / 100.0);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Failed to play sound: " + fileName + " -> " + e.getMessage());
        }
    }

    public void onShuffle() {
        playSound("cardShuffle.mp3");
    }

    // -----------Helpers------------------
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
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
    public void setBoard(Board board){
        this.board = board;
    }
    public void updateCounters(Game game) {
        ArrayList<Player> players = game.getPlayers();

        CPU1RemainingCards.setText(players.get(1).getHand().size() + "");
        CPU2RemainingCards.setText(players.get(2).getHand().size() + "");
        CPU3RemainingCards.setText(players.get(3).getHand().size() + "");
    }
    private double closerBigger(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    public static void updateTrapFlag(){
        trapped = !trapped;
    }
    private String toRgbString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("rgb(%d, %d, %d)", r, g, b);
    }
    public static RadialGradient makeMarbleGradient(Color playerColor) {
        return new RadialGradient(
                45,                 // focus angle
                0.3,                // focus distance
                0.5, 0.5,           // center X, Y
                0.5,                // radius
                true,               // proportional
                CycleMethod.REFLECT,
                new Stop(0, Color.WHITE),
                new Stop(1, playerColor)
        );
    }
    void greyOutCards(List<ImageView> cards, boolean isActive) {
        if (isActive) {
            for (ImageView card : cards) {
                card.setEffect(null); // Full color, no grey
            }
        } else {
            DropShadow shadow = new DropShadow();
            shadow.setRadius(10);
            shadow.setOffsetX(0);
            shadow.setOffsetY(0);
            shadow.setColor(Color.rgb(0, 0, 0, 0.5)); // semi-transparent black

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-0.7);
            colorAdjust.setBrightness(-0.7);
            for (ImageView card : cards) {
                card.setEffect(colorAdjust);
            }
            if (settings.getTheme().equals("Alien")){
                cardDescription.setText(null);
                cardDescription.getStyleClass().setAll("card-description");
            }
        }
    }



    public static double getMusicVolume() throws IOException {
        UserSettings userSettings = new UserSettings().LoadSettings();
        return userSettings.getMusic() / 100.0;

    }

    //---- Old Methods----

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
        alert.showAndWait();
    }
}