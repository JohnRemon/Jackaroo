package engine.board;

import model.Colour;

import java.util.ArrayList;

public class SafeZone {
    private final Colour colour;
    private final ArrayList<Cell> cells;

    public SafeZone(Colour colour) {
        this.colour = colour;
        this.cells = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            cells.add(new Cell(CellType.SAFE));
        }
    }
}
