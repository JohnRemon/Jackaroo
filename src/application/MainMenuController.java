package application;

import engine.Game;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class MainMenuController extends Application {
    @FXML
    public TextField nameLabel;
    public Button playButton;
    public Button settingsButton;
    public Pane settingsPane;
    public Button SubmitName;
    public Button SubmitName1;
    public TextField nameLabel1;
    public Label resetName;
    public Button saveAndExit;

    public static String nameChosen;

    @Override
    public void start(Stage stage) throws Exception {

    }

    @FXML
    public void openSettings(ActionEvent actionEvent) throws IOException {
        settingsPane.setVisible(true);
    }
    @FXML
    public void transitionBoard(ActionEvent actionEvent) throws IOException {
        Game game = new Game(nameChosen);
    }

    public void setName(ActionEvent actionEvent) {
        //TODO: clean this garbage. Will be cleaned when UserSettings is implemented.
        nameChosen = nameLabel.getText() == null ? nameLabel1.getText() : nameLabel.getText();
    }

    public void hideSettingsMenu(ActionEvent actionEvent) {
        settingsPane.setVisible(false);
    }

    /*public Game initGame() {

    }

    public Game initStartup() {
        //initalizes the start menu.
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

     */
}
