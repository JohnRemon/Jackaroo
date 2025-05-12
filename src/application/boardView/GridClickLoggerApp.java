package application.boardView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GridClickLoggerApp extends Application {

    private static final int GRID_SIZE = 27;
    private static final int TILE_SIZE = 25;
    private static final String OUTPUT_FILE = "tile_clicks.txt";

    private final AtomicInteger clickCounter = new AtomicInteger(0);

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                StackPane tile = createTile(row, col);
                grid.add(tile, col, row);
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        Scene scene = new Scene(scrollPane, 700, 700);
        primaryStage.setTitle("Grid Click Logger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createTile(int row, int col) {
        Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLACK);

        StackPane stack = new StackPane(rect);
        stack.setOnMouseClicked(event -> handleClick(stack, row, col));

        return stack;
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
