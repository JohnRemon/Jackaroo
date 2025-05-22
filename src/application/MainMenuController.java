package application;

import application.boardView.BoardViewAlien;
import application.boardView.BoardViewDefault;
import exception.GameException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainMenuController extends Application {
    @FXML
    //---Settings---
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

    //---Keybinds---
    @FXML
    public HBox selectCardHBox;
    public HBox selectMarbleHBox;
    public HBox turnHBox;
    public static ArrayList<Button> selectButtons = new ArrayList<>();
    public static ArrayList<Button> selectMarbleButtons = new ArrayList<>();
    public static ArrayList<Button> turnButtons = new ArrayList<>();

    public final UserSettings userSettings = new UserSettings().LoadSettings();
    public final UserSettings.KeyBinds keyBinds = new UserSettings.KeyBinds().loadBinds();

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
        UserSettings.KeyBinds binds = new UserSettings.KeyBinds().loadBinds();
        nameLabel.setText(temp.getName());
        sfxSlider.setValue(temp.getSfx());
        musicSlider.setValue(temp.getMusic());
        themeChosen.getItems().clear();
        themeChosen.getItems().addAll("Default", "Alien");
        themeChosen.setValue(temp.getTheme());
    }

    public void initialize() {
        //places bottons
        for (int i = 0; i < 4; i++){
            Button button = new Button();
            button.setPrefSize(83, 14);
            selectButtons.add(button);
            button.setOnMouseClicked(event -> {
                button.setText("Please enter a key...");
                button.getStyleClass().add(".rebind-button.listening");

                button.requestFocus();
                button.setOnKeyPressed(keyEvent -> {
                  button.setText(keyEvent.getCode().toString());
                }
            })
            selectCardHBox.getChildren().add(button);

            Button button1 = new Button();
            button1.setPrefSize(83, 14);
            selectMarbleHBox.getChildren().add(button1);
            selectMarbleButtons.add(button1);

            Button button2 = new Button();
            button2.setPrefSize(83, 14);
            turnButtons.add(button2);
            turnHBox.getChildren().add(button2);
        }

    }
}
