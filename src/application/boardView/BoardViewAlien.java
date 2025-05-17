package application.boardView;

import exception.GameException;
import javafx.animation.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

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

        ParallelTransition teleport = new ParallelTransition(fill, flash, scale);
        teleport.setOnFinished(e -> marble.setEffect(null));
        teleport.play();
    }

}