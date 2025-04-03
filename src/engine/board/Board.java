package engine.board;

import engine.GameManager;
import exception.*;
import model.Colour;
import model.player.Marble;

import java.util.ArrayList;
import java.util.Objects;

public class Board implements BoardManager{
    private final GameManager gameManager;
    private final ArrayList<Cell> track;
    private final ArrayList<SafeZone> safeZones;
    private int splitDistance;

    public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
        this.gameManager = gameManager;
        this.track = new ArrayList<>(100);
        this.safeZones = new ArrayList<>();
        this.splitDistance = 3;
        //create track
        for(int i = 0; i < 100; i++){
            if(i % 25 == 0)
                track.add(new Cell(CellType.BASE));
            else if((i + 2) % 25 == 0)
                track.add(new Cell(CellType.ENTRY));
            else
                track.add(new Cell(CellType.NORMAL));
        }
        //change 8 random Normal track cells to be flagged as a trap
        for(int i = 0; i < 8; i++){
            assignTrapCell();
        }
        //create safe zones
        for(Colour colour : colourOrder){
            SafeZone safeZone = new SafeZone(colour);
            safeZones.add(safeZone);
        }

    }
    //getter and setter for splitDistance
    public int getSplitDistance(){
        return splitDistance;
    }
    public void setSplitDistance(int splitDistance){
        this.splitDistance = splitDistance;
    }
    //getter for safeZones
    public ArrayList<SafeZone> getSafeZones() {
        return safeZones;
    }
    //getter for track
    public ArrayList<Cell> getTrack() {
        return track;
    }

    private void assignTrapCell(){
        int randomCell;
        do {
            randomCell = (int) (Math.random() * track.size());
        }while(track.get(randomCell).getCellType() != CellType.NORMAL || track.get(randomCell).isTrap());
        track.get(randomCell).setTrap(true);
    }

    /*Returns an ArrayList of cells of a
    certain Safe Zone from the list of safeZones given the target colour, defaulting to null if Safe
    Zone color is not found.*/
    private ArrayList<Cell> getSafeZone(Colour colour){
        ArrayList<Cell> safeCells = new ArrayList<>();

        for(SafeZone safeZone : safeZones){
            if(safeZone.getColour() == colour){
                safeCells.addAll(safeZone.getCells());
                return safeCells;
            }
        }
        return null;
    }


    /*Return the
    index of a marble’s position on a given path of cells, which could be the track or a Safe Zone,
    defaulting to -1 if the marble is not found on the given path.*/
    private int getPositionInPath(ArrayList<Cell> path, Marble marble){
        for(int i = 0; i < path.size(); i++){
            if(path.get(i).getMarble() == marble){
                return i;
            }
        }
        return -1;
    }

    /*Returns the index of the Base cell position on
    track for a given colour, defaulting to -1 if the Base is not found due to an invalid colour.*/
    private int getBasePosition(Colour colour){
        for(int i = 0; i < track.size(); i++){
            if(track.get(i).getCellType() == CellType.BASE && track.get(i).getMarble().getColour() == colour){
                return i;
            }
        }
        return -1;
    }
    /*Returns the index of the Entry cell position
    on track for a given colour, defaulting to -1 if the Entry is not found due to an invalid colour*/
    private int getEntryPosition(Colour colour){
        for(int i = 0; i < track.size(); i++){
            if(track.get(i).getCellType() == CellType.ENTRY && track.get(i).getMarble().getColour() == colour){
                return i;
            }
        }
        return -1;
    }

 /* Helper method for validate steps
 *  checks if the marble is in the safe zone and returns a boolean variable
  */
    private boolean isInSafeZone( Marble marble){
        Colour activePlayerColour = gameManager.getActivePlayerColour();
        for( SafeZone safeZone : safeZones){
            if (safeZone.getColour() == activePlayerColour){
                for ( Cell cell : safeZone.getCells()){
                    if( cell.getMarble() == marble){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*Validates the number of steps a marble can move and returns the full path of cells
    that the marble will take in order, from current to target keeping in mind the special cards:
     */
    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
        ArrayList<Cell> path = new ArrayList<>();
        Colour marbleColour = marble.getColour();
        boolean isActivePlayer = marbleColour.equals(gameManager.getActivePlayerColour());


        boolean isInSafeZone = isInSafeZone(marble);
        int currentTrackPos = getPositionInPath(track, marble);

        if (!isInSafeZone && currentTrackPos == -1) {
            throw new IllegalMovementException("Marble is not found on the board");
        }

        if (steps < 0) {
            if (isInSafeZone) {
                throw new IllegalMovementException("Cannot move backwards in safe zone");
            }
            return validateBackwardSteps(marble, steps, currentTrackPos);
        }

        if (isInSafeZone) {
            ArrayList<Cell> safeZoneCells = getSafeZone(marbleColour);
            int currentPos = getPositionInPath(safeZoneCells, marble);

            path.add(safeZoneCells.get(currentPos));

            if (currentPos + steps >= safeZoneCells.size()) {
                throw new IllegalMovementException(steps + " steps exceeds safe zone capacity");
            }

            for (int i = 1; i <= steps; i++) {
                path.add(safeZoneCells.get(currentPos + i));
            }
        }
        // track movement
        else {
            path.add(track.get(currentTrackPos));

            int entryPos = getEntryPosition(marbleColour);
            int stepsToEntry = (entryPos - currentTrackPos + 100) % 100;
            boolean isFiveCard = steps == 5 && !isActivePlayer;

            //  Five card special rules
            if (isFiveCard) {

                if (steps > stepsToEntry) {
                    throw new IllegalMovementException("Five card entry to safe zone is not allowed");
                }
            }

            if (steps > stepsToEntry + getSafeZone(marbleColour).size() && !isFiveCard) {
                throw new IllegalMovementException(steps + " steps exceeds maximum movement");
            }

            if (steps > stepsToEntry && !isFiveCard) {

                for (int i = 1; i <= stepsToEntry; i++) {
                    path.add(track.get((currentTrackPos + i) % 100));
                }

                ArrayList<Cell> safeZoneCells = getSafeZone(marbleColour);
                int safeZoneSteps = steps - stepsToEntry;
                for (int i = 0; i < safeZoneSteps; i++) {
                    path.add(safeZoneCells.get(i));
                }
            }

            else {
                for (int i = 1; i <= steps; i++) {
                    path.add(track.get((currentTrackPos + i) % 100));
                }
            }
        }

        return path;
    }

    // validate steps helper
    private ArrayList<Cell> validateBackwardSteps(Marble marble, int steps, int currentPos) throws IllegalMovementException {
        ArrayList<Cell> path = new ArrayList<>();
        path.add(track.get(currentPos));

        for (int i = 1; i <= -steps; i++) {
            int nextPos = (currentPos - i + 100) % 100;
            Cell nextCell = track.get(nextPos);

            // Backward movement rules
            if (nextCell.getMarble() != null) {
                if (nextCell.getMarble().getColour() == marble.getColour()) {
                    throw new IllegalMovementException("Cannot move backward past own marble at cell " + nextPos);
                }
                if (nextCell.getCellType() == CellType.BASE) {
                    throw new IllegalMovementException("Cannot move backward into Base cell at " + nextPos);
                }
            }
            path.add(nextCell);
        }
        return path;
    }

    private void validatePath(Marble marble, ArrayList<Cell> path, boolean destroy) throws IllegalMovementException {

        int blockingCount = 0;
        Colour marbleColour = marble.getColour();

        for (int i = 0; i < path.size(); i++) {
            Cell cell = path.get(i);
            Marble otherMarble = cell.getMarble();

            // King can't affect Safe Zone marbles
            destroy = (otherMarble != null && isInSafeZone(otherMarble)) ? false : destroy;

            if (otherMarble != null) {
                // Can't pass own marbles unless destroying
                if (otherMarble.getColour() == marbleColour && !destroy) {
                    throw new IllegalMovementException("Own marble blockage");
                }

                if (blockingCount++ > 0 && !destroy) {
                    throw new IllegalMovementException("Path blocked");
                }


                if (cell.getCellType() == CellType.BASE && otherMarble.getColour() != marbleColour) {
                    throw new IllegalMovementException("Base cell blocked");
                }
                if (cell.getCellType() == CellType.ENTRY && otherMarble.getColour() == marbleColour) {
                    throw new IllegalMovementException("Entry blocked");
                }
            }
        }
    }

    private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
        for (Cell cell : track)
            if (cell.getMarble() == marble) cell.setMarble(null);
        for (SafeZone zone : safeZones)
            for (Cell cell : zone.getCells())
                if (cell.getMarble() == marble) cell.setMarble(null);

        if (destroy) {
            for (Cell cell : fullPath) {
                Marble other = cell.getMarble();
                if (other != null) destroyMarble(other); // Let destroyMarble() handle validation
            }
        }

        Cell target = fullPath.get(fullPath.size()-1);
        target.setMarble(marble);

        if (target.isTrap()) {
            destroyMarble(marble);
            target.setTrap(false);
            assignTrapCell();
        }
    }

    private void validateSwap(Marble marble_1, Marble marble_2) throws IllegalSwapException{
        if(getPositionInPath(track, marble_1) == -1 && getPositionInPath(track, marble_2) == -1){
            throw new IllegalSwapException("Both marbles are not on the track");
        }
        if(!marble_1.getColour().equals(gameManager.getActivePlayerColour()) && getBasePosition(marble_1.getColour()) != -1){
            throw new IllegalSwapException("The Opponent's marble is on the base");
        }
        if(!marble_2.getColour().equals(gameManager.getActivePlayerColour()) && getBasePosition(marble_2.getColour()) != -1){
            throw new IllegalSwapException("The Opponent's marble is on the base");
        }
    }

    private void validateDestroy(int positionInPath) throws IllegalDestroyException{
        if(positionInPath == -1){
            throw new IllegalDestroyException("The marble is not on the track");
        }
        if(track.get(positionInPath).getCellType() == CellType.BASE){
            throw new IllegalDestroyException("The marble is on the base");
        }
    }

    private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException{
        if(occupiedBaseCell.getMarble().getColour() == gameManager.getActivePlayerColour()){
            throw new CannotFieldException("The base cell is occupied by the same colour");
        }
    }

    private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException{
        if(positionInSafeZone != -1 || positionOnTrack == -1){
            throw new InvalidMarbleException("The marble is in the safe zone or not on the track");
        }

    }

    @Override
    public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {
        ArrayList<Cell> path = validateSteps(marble, steps);

        validatePath(marble, path, destroy);

        move(marble, path, destroy);
    }
    public void swap(Marble marble_1, Marble marble_2) throws IllegalSwapException {

        validateSwap(marble_1, marble_2);

        int position_1 = getPositionInPath(track, marble_1);
        int position_2 = getPositionInPath(track, marble_2);

        if (position_1 == -1 || position_2 == -1){
        throw new IllegalSwapException("Both marbles must be on the track");
        }

        Cell cell_1 = track.get(position_1);
        Cell cell_2 = track.get(position_2);

        Marble temp = cell_1.getMarble();
        cell_1.setMarble(cell_2.getMarble());
        cell_2.setMarble(temp);
    }

    @Override
    public void destroyMarble(Marble marble) throws IllegalDestroyException {
        if (!marble.getColour().equals(gameManager.getActivePlayerColour())) {
            int pos = getPositionInPath(track, marble);
            validateDestroy(pos);
        }

        for (Cell cell : track) {
            if (cell.getMarble() == marble) {
                cell.setMarble(null);
                break;
            }
        }
        try {
            sendToBase(marble);
        } catch (CannotFieldException e) {
            throw new IllegalDestroyException("Failed to return marble home");
        }
    }

    @Override
    public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
        //get the base position of the marble
        int basePosition = getBasePosition(marble.getColour());

        //check if there is a marble
        if(track.get(basePosition).getMarble() != null){

            //validate the fielding
            validateFielding(track.get(basePosition));

            //destroy opponent's marble
            if(track.get(basePosition).getMarble().getColour() != marble.getColour()){
                validateDestroy(basePosition);
                destroyMarble(track.get(basePosition).getMarble());
            }
        }
        track.get(basePosition).setMarble(marble);
    }

    @Override
    public void sendToSafe(Marble marble) throws InvalidMarbleException {
        //get the position of the marble on the track
        int positionOnTrack = getPositionInPath(track, marble);

        //get the position of the marble in the safe zone
        int positionInSafeZone = getPositionInPath(getSafeZone(marble.getColour()), marble);

        //Validate the marble Saving
        validateSaving(positionInSafeZone, positionOnTrack);

        //get the unoccupied cells
        ArrayList<Cell> unoccupiedCells = new ArrayList<>();
        for(SafeZone safeZone : safeZones){
            if(safeZone.getColour() == marble.getColour()){
                for(Cell cell : safeZone.getCells()){
                    if(cell.getMarble() == null){
                        unoccupiedCells.add(cell);
                    }
                }
            }
        }

        //get a random cell from the unoccupied cells
        int random = (int) (Math.random() * unoccupiedCells.size());

        //set the marble to the cell
        unoccupiedCells.get(random).setMarble(marble);

    }

    @Override
    public ArrayList<Marble> getActionableMarbles() {
        ArrayList<Marble> actionableMarbles= new ArrayList<>();
        //check all the cells in the track
        for(Cell cell : track){
            //if the cell is not empty and the marble is of the same colour as the active player
            if(cell.getMarble() != null && cell.getMarble().getColour() == gameManager.getActivePlayerColour()){
                //add the marble to the list of actionable marbles
                actionableMarbles.add(cell.getMarble());
            }
        }
        //check all the cells in the safe zones
        for(SafeZone safeZone : safeZones){
            //if it is the active player's safe zone
            if(safeZone.getColour() == gameManager.getActivePlayerColour()){
                for(Cell cell : safeZone.getCells()){
                    //if the cell is not empty
                    if(cell.getMarble() != null){
                        //add the marble to the list of actionable marbles
                        actionableMarbles.add(cell.getMarble());
                    }
                }
            }
        }
        return actionableMarbles;
    }
}
