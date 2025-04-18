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

    //getter and setter for the marble
    public Marble getMarble() {
        return marble;
    }
    public void setMarble(Marble marble) {
        this.marble = marble;
    }

    //getter and setter for the cellType
    public CellType getCellType() {
        return cellType;
    }
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    //getter and setter for the trap
    public boolean isTrap() {
        return trap;
    }
    public void setTrap(boolean trap) {
        this.trap = trap;
    }
}
