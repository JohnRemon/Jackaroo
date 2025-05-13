package application.boardView;

import application.Main;
import engine.Game;
import exception.GameException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardViewDefault extends BoardView{
    public static void setBoardPaneDefault(String username) throws IOException, GameException {
        setBoardPane("BoardViewDefault.fxml", username);
    }
}
