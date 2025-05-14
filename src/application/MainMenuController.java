package application;

import application.boardView.BoardView;
import application.boardView.BoardViewAlien;
import application.boardView.BoardViewDefault;
import engine.Game;
import exception.GameException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    public Button saveAndExit;
    public AnchorPane mainMenu;
    public Slider sfxSlider;
    public Slider musicSlider;
    public ChoiceBox<String> themeChosen;
    public final UserSettings userSettings = new UserSettings().LoadSettings();

    public MainMenuController() throws IOException {
    }

    @Override
    public void start(Stage stage) throws Exception {
        //required to be declared
    }

    @FXML
    public void openSettings(ActionEvent actionEvent) throws IOException {
        themeChosen.getItems().clear();
        themeChosen.getItems().addAll("Default", "Alien");
        themeChosen.setValue(userSettings.getTheme());
        settingsPane.setVisible(true);
    }
    @FXML
    private void transitionToBoard(ActionEvent event) throws IOException, GameException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/boardView/boardView.fxml"));
//        Parent boardRoot = loader.load();
//
//        Stage gameStage = new Stage();
//        Scene gameScene = new Scene(boardRoot);
//        gameStage.setScene(gameScene);
//        gameStage.setTitle("Jackaroo");

        //close launcher
        Stage stage = Main.primaryStage;
        switch (userSettings.getTheme())
        {
            case "Alien": BoardViewAlien.setBoardPaneAlien(nameLabel.getText());
            break;
            default: BoardViewDefault.setBoardPaneDefault(nameLabel.getText());
            break;
        }
        //show the board
        //gameStage.show();
    }

    public void hideSettingsMenu(ActionEvent actionEvent) throws IOException {
        settingsPane.setVisible(false);
        saveSettings();

        UserSettings updatedSettings = new UserSettings().LoadSettings();
        userSettings.setName(updatedSettings.getName());
        userSettings.setSfx((float) updatedSettings.getSfx());
        userSettings.setMusic((float) updatedSettings.getMusic());
        userSettings.setTheme(updatedSettings.getTheme());
    }

    @FXML
    public void saveSettings() throws IOException {
        String playerName = nameLabel.getText();
        double sfx = sfxSlider.getValue();
        double music = musicSlider.getValue();
        String theme = themeChosen.getValue();

        UserSettings settings = new UserSettings(playerName, sfx, music, theme);
        settings.SaveSettings(settings);
    }

    public void loadValues() throws IOException {
        UserSettings controller = new UserSettings();
        UserSettings temp = controller.LoadSettings();
        nameLabel.setText(temp.getName());
        sfxSlider.setValue(temp.getSfx());
        musicSlider.setValue(temp.getMusic());
        themeChosen.getItems().clear();
        themeChosen.getItems().addAll("Default", "Alien");
        themeChosen.setValue(temp.getTheme());
    }
}
