package engine.board;

import model.player.Marble;

public class Cell {
    private Marble marble;
    private CellType cellType;
    private boolean trap;

    public Cell(CellType cellType) {
        this.marble = null;
        this.cellType = cellType;
        this.trap = false;
    }
}
