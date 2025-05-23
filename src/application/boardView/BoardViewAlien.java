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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.Colour;

import java.io.IOException;

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


//    public void assignCardsAnimation(Game game) {
//        playerCardsRow.setOnMouseMoved(event -> {
//            double mouseX = event.getX();
//            for (int i = 0; i < playerCardsImages.size(); i++) {
//                ImageView card = playerCardsImages.get(i);
//                double cardCenterX = card.getLayoutX() + (card.getBoundsInParent().getWidth() / 2);
//                double distance = Math.abs(mouseX - cardCenterX);
//
//                // Map distance to scale (closer = bigger)
//                double scale = closerBigger(1.0 + (150 - distance) / 300, 1.0, 1.2);
//                double rotate = closerBigger((mouseX - cardCenterX) / 30, -10, 10);
//
//                card.setScaleX(scale);
//                card.setScaleY(scale);
//                card.setRotate(rotate);
//            }
//        });
//
//        playerCardsRow.setOnMouseExited(event -> {
//            for (ImageView card : playerCardsImages) {
//                card.setScaleX(1.0);
//                card.setScaleY(1.0);
//                card.setRotate(0);
//            }
//        });
//    }

    public static Effect getDefaultMarbleShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.6));
        shadow.setOffsetX(1);
        shadow.setOffsetY(1);
        shadow.setSpread(0.3);
        return shadow;
    }

    private double closerBigger(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

}