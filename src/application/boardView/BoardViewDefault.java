package application.boardView;

import application.Main;
import engine.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardViewDefault extends BoardView{
    public static void setBoardPaneDefault(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(BoardViewDefault.class.getResource("BoardViewDefault.fxml"));
        Parent boardRoot = loader.load();
        BoardViewDefault controller = loader.getController();
        Game game = new Game(username);
        // Set all the static objects in place
        controller.initGame(game);

        // Reuse the current stage
        Stage stage = (Stage) Main.getPrimaryStage();
        Scene gameScene = new Scene(boardRoot);
        stage.setScene(gameScene);
        stage.setTitle("Jackaroo");

        gameScene.setOnKeyPressed(e -> {
            System.out.println(e.getCode() + " pressed");
            switch (e.getCode()) {
                case DIGIT1 -> controller.selectCard(0, game);
                case DIGIT2 -> controller.selectCard(1, game);
                case DIGIT3 -> controller.selectCard(2, game);
                case DIGIT4 -> controller.selectCard(3, game);
            }
        });

        gameScene.getRoot().requestFocus(); // Ensure the anchor pane is taking input correctly
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}
