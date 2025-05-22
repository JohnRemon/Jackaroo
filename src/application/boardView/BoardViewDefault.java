package application.boardView;

import exception.GameException;

import java.io.IOException;

public class BoardViewDefault extends BoardView{
    public static void setBoardPaneDefault(String username) throws IOException, GameException {
        setBoardPane("BoardViewDefault.fxml", username);
    }
}
