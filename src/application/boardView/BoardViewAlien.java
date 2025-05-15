package application.boardView;

import application.Main;
import application.UserSettings;
import engine.Game;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.card.Card;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static application.Main.getPrimaryStage;

public class BoardViewAlien extends BoardView {
    public static void setBoardPaneAlien(String username) throws IOException, GameException {
        setBoardPane("BoardViewAlien.fxml", username);
    }
        public static void playTeleportEffect(Circle marble, Pane root) {
            FillTransition fill = new FillTransition(Duration.millis(800), marble, (Color) marble.getFill(), Color.LIMEGREEN);
            fill.setAutoReverse(true);
            fill.setCycleCount(2);
            fill.setInterpolator(Interpolator.EASE_BOTH);

            DropShadow glow = new DropShadow(20, Color.LIME);
            glow.setSpread(0.5);
            marble.setEffect(glow);

            FadeTransition flash = new FadeTransition(Duration.millis(600), marble);
            flash.setFromValue(1.0);
            flash.setToValue(0.3);
            flash.setCycleCount(2);
            flash.setAutoReverse(true);

            ScaleTransition scale = new ScaleTransition(Duration.millis(700), marble);
            scale.setToX(1.4);
            scale.setToY(1.4);
            scale.setCycleCount(2);
            scale.setAutoReverse(true);

            Line streak1 = new Line(0, -20, 0, -60);
            Line streak2 = new Line(0, 20, 0, 60);
            for (Line line : List.of(streak1, streak2)) {
                line.setStroke(Color.LIMEGREEN);
                line.setStrokeWidth(2);
                line.setOpacity(0.7);
                line.setTranslateX(marble.getLayoutX());
                line.setTranslateY(marble.getLayoutY());
            }

            root.getChildren().addAll(streak1, streak2);

            TranslateTransition streakMove1 = new TranslateTransition(Duration.millis(700), streak1);
            streakMove1.setByY(-30);
            TranslateTransition streakMove2 = new TranslateTransition(Duration.millis(700), streak2);
            streakMove2.setByY(30);

            FadeTransition streakFade1 = new FadeTransition(Duration.millis(800), streak1);
            streakFade1.setFromValue(0.7);
            streakFade1.setToValue(0.0);

            FadeTransition streakFade2 = new FadeTransition(Duration.millis(800), streak2);
            streakFade2.setFromValue(0.7);
            streakFade2.setToValue(0.0);

            ParallelTransition teleport = new ParallelTransition(
                    fill, flash, scale,
                    new ParallelTransition(streakMove1, streakFade1),
                    new ParallelTransition(streakMove2, streakFade2)
            );

            teleport.setOnFinished(e -> {
                marble.setEffect(null);
                root.getChildren().removeAll(streak1, streak2);
            });

            teleport.play();
        }
    }