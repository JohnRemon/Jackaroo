package application.boardView;

import engine.Game;
import exception.GameException;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.Colour;
import model.card.Card;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BoardViewAlien extends BoardView {
    public static void setBoardPaneAlien(String username) throws IOException, GameException {
        setBoardPane("BoardViewAlien.fxml", username);
    }
    public static void playTeleportEffect(Circle marble, Pane root) {
        Paint originalColour = marble.getFill();

        marble.setStyle("-fx-effect: dropshadow(gaussian, #00ff00, 10, 0.5, 0, 0);\n");
        marble.setStyle("-fx-effect: dropshadow(gaussian, #00ff00, 10, 0.7, 0, 0);\n");

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

        ParallelTransition teleport = new ParallelTransition(flash, scale);
        teleport.setOnFinished(e -> {
            marble.setEffect(null);
            marble.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 5, 0.3, 1, 1);\n");
        });

        teleport.play();
    }
    public static void playTeleportEffectOld(Circle marble, Pane root) {
        if (marble == null) return;

        Paint originalFill = marble.getFill();
        if (!(originalFill instanceof Color)) {
            originalFill = Color.WHITE; // fallback if somehow not a Color
        }

        // Guard against node not in scene graph yet
        if (marble.getScene() == null) return;

        // Fill transition
        FillTransition fill = new FillTransition(Duration.millis(800), marble);
        fill.setFromValue((Color) originalFill);
        fill.setToValue(Color.LIMEGREEN);
        fill.setAutoReverse(true);
        fill.setCycleCount(2);
        fill.setInterpolator(Interpolator.EASE_BOTH);

        // Glow effect
        DropShadow glow = new DropShadow(20, Color.LIME);
        glow.setSpread(0.5);
        marble.setEffect(glow);

        // Fade transition
        FadeTransition flash = new FadeTransition(Duration.millis(600), marble);
        flash.setFromValue(1.0);
        flash.setToValue(0.3);
        flash.setCycleCount(2);
        flash.setAutoReverse(true);

        // Scale transition
        ScaleTransition scale = new ScaleTransition(Duration.millis(700), marble);
        scale.setToX(1.4);
        scale.setToY(1.4);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);

        // Bundle transitions
        ParallelTransition teleport = new ParallelTransition(fill, flash, scale);
        Paint finalOriginalFill = originalFill;
        teleport.setOnFinished(e -> {
            marble.setEffect(null);

            // Only reset if we're still using that marble in the scene
            if (marble.getScene() != null) {
                marble.setFill(finalOriginalFill);
            }
        });

        teleport.play();
    }

    private double closerBigger(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

}