package application.boardView;

import exception.GameException;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BoardViewOnePiece extends BoardView {
    public static void setBoardPaneOnePiece(String username) throws IOException, GameException {
        setBoardPane("BoardViewOnePiece.fxml", username);
    }

//    @Override
//    public void showException(String message) {
//        playSound("error.mp3");
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Invalid Action");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        // Load One Piece-themed CSS for the alert
//        alert.getDialogPane().getStylesheets().add(
//                getClass().getResource("/application/BoardViewOnePiece.css").toExternalForm()
//        );
//        alert.getDialogPane().getStyleClass().add("one-piece-alert");
//        alert.show(); // Use show() as in base method to avoid IllegalStateException
//    }

    @Override
    public void showException(String message) {
        playSound("error.mp3");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Action");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(message);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/boardView/Sheets/BoardViewOnePiece.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("one-piece-alert");

        alert.getButtonTypes().set(0, new ButtonType("Aye Aye!", ButtonBar.ButtonData.LEFT));

        DialogPane pane = alert.getDialogPane();
        pane.setMinHeight(300);
        pane.setMinWidth(500);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setWidth(700);
        stage.setHeight(500);

        alert.show();
    }

    public void showWinnerAlert(String winnerName) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("We Have a Winner!");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText(winnerName + " has won the game!");

        DialogPane pane = alert.getDialogPane();
        pane.setMinHeight(300);
        pane.setMinWidth(500);

        // Apply One Piece CSS
        pane.getStylesheets().add(getClass().getResource("/application/boardView/Sheets/BoardViewOnePiece.css").toExternalForm());
        pane.getStyleClass().add("one-piece-alert");

        ImageView winnerImage = new ImageView(getClass().getResource("/view/textures/win.jpg").toExternalForm());

        // Customize the button
        alert.getButtonTypes().set(0, new ButtonType("Celebrate!", ButtonBar.ButtonData.LEFT));

        // Resize the stage for better image fit
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setWidth(700);
        stage.setHeight(500);

        alert.show();
    }



}
