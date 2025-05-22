package application.external;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GridClickLoggerApp extends Application {

    private static final int GRID_SIZE = 27;
    private static final int TILE_SIZE = 25;
    private static String OUTPUT_FILE = "tile_clicks.";

    private final AtomicInteger clickCounter = new AtomicInteger(0);

    @Override
    public void start(Stage primaryStage) throws IOException {
        // === 1. Main container ===
        HBox root = new HBox();

        // === 2. StackPane for image + grid ===
        StackPane boardLayer = new StackPane();
        boardLayer.setPrefSize(800, 800);

        Image boardImage = new Image(getClass().getResourceAsStream("/view/textures/alien/oard.png"));
        ImageView boardImageView = new ImageView(boardImage);
        boardImageView.setFitWidth(800);
        boardImageView.setFitHeight(800);
        boardImageView.setPreserveRatio(false); // force fit

        // Grid on top
        GridPane grid = new GridPane();
        grid.setPrefSize(800, 800);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                StackPane tile = createTile(row, col);
                grid.add(tile, col, row);
            }
        }

        // Set row/column constraints
        for (int i = 0; i < GRID_SIZE; i++) {
            ColumnConstraints cc = new ColumnConstraints(800.0 / GRID_SIZE);
            RowConstraints rc = new RowConstraints(800.0 / GRID_SIZE);
            grid.getColumnConstraints().add(cc);
            grid.getRowConstraints().add(rc);
        }

        boardLayer.getChildren().addAll(boardImageView, grid);

        // === 3. Sidebar (right side) ===
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #222; -fx-padding: 10;");
        // Add whatever UI elements you want:
        sidebar.getChildren().addAll(
                new javafx.scene.control.Label("Choose Theme"),
                new javafx.scene.control.Label("default"),
                new javafx.scene.control.Label("Alien")
        );

        // === 4. Combine all ===
        root.getChildren().addAll(boardLayer, sidebar);

        Scene scene = new Scene(root, 1000, 800); // total width: 800 + 200 sidebar
        primaryStage.setScene(scene);
        primaryStage.setTitle("Grid Click Logger");
        primaryStage.show();
    }


    private StackPane createTile(int row, int col) {
        Rectangle rect = new Rectangle(800.0 / GRID_SIZE, 800.0 / GRID_SIZE);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.rgb(0, 0, 0, 0.2));

        StackPane tile = new StackPane(rect);
        tile.setOnMouseClicked(event -> handleClick(tile, row, col));
        return tile;
    }


    private void handleClick(StackPane tile, int row, int col) {
        Rectangle red = new Rectangle(TILE_SIZE, TILE_SIZE);
        red.setFill(Color.RED);
        tile.getChildren().setAll(red);

        int clickNumber = clickCounter.incrementAndGet();
        String log = String.format("%d,%d,%d", clickNumber, row, col);
        System.out.println(log);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}