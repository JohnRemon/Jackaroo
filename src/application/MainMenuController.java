package application;

import application.boardView.BoardViewAlien;
import application.boardView.BoardViewDefault;
import application.boardView.BoardViewMedieval;
import application.boardView.BoardViewOnePiece;
import exception.GameException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static application.boardView.BoardView.playSound;


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
    public VBox selectCardHBox;
    @FXML
    public VBox selectMarbleHBox;
    @FXML
    public VBox turnHBox;
    @FXML Pane keyBindsMenu;
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
        playSound("menuClick.mp3");
        themeChosen.getItems().clear();
        themeChosen.getItems().addAll("Default", "Alien", "Medieval", "OnePiece");
        themeChosen.setValue(userSettings.getTheme());
        settingsPane.setVisible(true);
    }
    @FXML
    public void openKeyBinds(MouseEvent mouseEvent) throws IOException {
        keyBindsMenu.setVisible(true);

    }

    @FXML
    private void transitionToBoard(ActionEvent event) throws IOException, GameException {
        Stage stage = Main.primaryStage;
        switch (userSettings.getTheme())
        {
            case "Alien": BoardViewAlien.setBoardPaneAlien(nameLabel.getText());
            break;
            case "Medieval": BoardViewMedieval.setBoardPaneMedieval(nameLabel.getText());
            break;
            case "OnePiece": BoardViewOnePiece.setBoardPaneOnePiece(nameLabel.getText());
            break;
            default: BoardViewDefault.setBoardPaneDefault(nameLabel.getText());
            break;
        }

    }

    public void hideSettingsMenu(ActionEvent actionEvent) throws IOException {
        settingsPane.setVisible(false);
        saveSettings();
        playSound("menuClick.mp3");
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
        themeChosen.getItems().addAll("Default", "Alien", "Medieval", "OnePiece");
        themeChosen.setValue(temp.getTheme());
        setBindNames();
    }

    public void initialize() {
        //clear old stuff
        selectCardHBox.getChildren().clear();
        selectMarbleHBox.getChildren().clear();
        turnHBox.getChildren().clear();
        selectButtons.clear();
        selectMarbleButtons.clear();
        turnButtons.clear();

        //places card buttons
        for (int i = 0; i < 4; i++){
            Button card = new Button();
            card.setPrefSize(83, 14);
            selectButtons.add(card);
            int finalI = i;
            card.setOnMouseClicked(event -> {
                card.setText("Please enter a key...");
                card.getStyleClass().add(".rebind-button.listening");

                card.requestFocus();
                card.setOnKeyPressed(keyEvent -> {
                    card.setText(keyEvent.getCode().toString());
                    keyBinds.bindKey(keyEvent, finalI);
                });
            });
            selectCardHBox.getChildren().add(card);
        }
        //places marble buttons
        for (int i = 4; i < 8; i++){
            Button button = new Button();
            button.setPrefSize(83, 14);
            selectMarbleButtons.add(button);
            int finalI = i;
            button.setOnMouseClicked(event -> {
                button.setText("Please enter a key...");
                button.getStyleClass().add(".rebind-button.listening");

                button.requestFocus();
                button.setOnKeyPressed(keyEvent -> {
                    button.setText(keyEvent.getCode().toString());
                    keyBinds.bindKey(keyEvent, finalI);
                });
            });
            selectMarbleHBox.getChildren().add(button);
        }
        //places turn buttons
        for (int i = 8; i < 11; i++){
            Button button = new Button();
            button.setPrefSize(83, 14);
            turnButtons.add(button);
            int finalI = i;
            button.setOnMouseClicked(event -> {
                button.setText("Please enter a key...");
                button.getStyleClass().add(".rebind-button.listening");

                button.requestFocus();
                button.setOnKeyPressed(keyEvent -> {
                    button.setText(keyEvent.getCode().toString());
                    keyBinds.bindKey(keyEvent, finalI);
                });
            });
            turnHBox.getChildren().add(button);
        }

    }

    private void setBindNames() {
        int i = 0;
        for (Button button : selectButtons){
            button.setText(keyBinds.findKey(i++));
        }
        for (Button button : selectMarbleButtons){
            button.setText(keyBinds.findKey(i++));
        }
        for (Button button : turnButtons){
            button.setText(keyBinds.findKey(i++));
        }
    }

    public void saveKeyBinds(MouseEvent mouseEvent) {
        keyBinds.saveBinds(keyBinds);
        hideKeyBindsMenu(mouseEvent);
    }

    public void hideKeyBindsMenu(MouseEvent mouseEvent) {
            keyBindsMenu.setVisible(false);
    }
}
