package application.boardView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardView{

    @FXML
    private Pane boardPane;

    public static void setBoardPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(BoardView.class.getResource("BoardView.fxml"));
        Parent boardRoot = loader.load();

        Stage gameStage = new Stage();
        Scene gameScene = new Scene(boardRoot);
        gameStage.setScene(gameScene);
        gameStage.setTitle("Jackaroo");
        gameStage.show();
    }

    public void initGame() {
        // Your game loop setup, marble init, etc.
        System.out.println("Game starting...");
    }
}
