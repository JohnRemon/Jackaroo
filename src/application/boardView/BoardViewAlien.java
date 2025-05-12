package application.boardView;

import application.Main;
import application.UserSettings;
import engine.Game;
import exception.GameException;
import exception.InvalidCardException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.card.Card;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;

import static application.Main.getPrimaryStage;

public class BoardViewAlien extends BoardView {
    public static void setBoardPaneAlien(String username) throws IOException, GameException {
        setBoardPane("BoardViewAlien.fxml", username);
    }
}