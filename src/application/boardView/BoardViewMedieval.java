package application.boardView;

import engine.Game;
import exception.GameException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class BoardViewMedieval extends BoardView{
    public static void setBoardPaneMedieval(String username) throws IOException, GameException {
        setBoardPane("BoardViewMedieval.fxml", username);
    }
}
