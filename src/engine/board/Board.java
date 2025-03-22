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

    @Override
    public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {

    }

    @Override
    public void swap(Marble marble_1, Marble marble_2) throws IllegalSwapException {

    }

    @Override
    public void destroyMarble(Marble marble) throws IllegalDestroyException {

    }

    @Override
    public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {

    }

    @Override
    public void sendToSafe(Marble marble) throws InvalidMarbleException {

    }

    @Override
    public ArrayList<Marble> getActionableMarbles() {
        return null;
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

    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException{
        ArrayList<Cell> validCells = new ArrayList<>();
        //First Get the Marble's Current Position
        if(getPositionInPath(track, marble) != -1){
            //The Marble is on the track

            //Check if the steps are valid
            if(steps > track.size() - getPositionInPath(track, marble) - 1){
                throw new IllegalMovementException("The rank of the card played is too high.");
            }


        }
        else if(getPositionInPath(Objects.requireNonNull(getSafeZone(marble.getColour())), marble) != -1){
            //The Marble is on the Safe Zone
        }
        else{
            throw new IllegalMovementException("Marble is not on the track or safe zone");
        }
        return validCells;
    }
}
