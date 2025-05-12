package model.player;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.Colour;

public class Marble {

    private final Colour colour;
    
    public Marble(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return this.colour;
    }

    public Paint getColourFX()
    {
        switch(colour) {
            case RED -> {return Color.RED;}
            case BLUE -> {return Color.BLUE;}
            case GREEN -> {return Color.GREEN;}
            case YELLOW -> {return Color.DARKGOLDENROD;}
            default -> {return Color.BLACK;}
        }

    }
}
