package application.boardView;

import javafx.scene.shape.Circle;
import model.player.Marble;

public class MarbleMapping {

    private final Marble marble;
    private final Circle circle;

    public MarbleMapping(Marble marble, Circle circle) {
        this.marble = marble;
        this.circle = circle;
    }

    public Marble getMarble() {
        return marble;
    }
    public Circle getCircle() {
        return circle;
    }
}

