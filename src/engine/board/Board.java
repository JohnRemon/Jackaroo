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



    private ArrayList<Cell> getSafeZone(Colour colour)
    {
        for (SafeZone safeZone : safeZones) {
            // we can use == in this case as we're comparing enums.
            if (safeZone.getColour() == (colour)) return safeZone.getCells();
        }
        return null;
    }

    private int getPositionInPath(ArrayList<Cell> path, Marble marble)
    {
        for (int i = 0; i < path.size(); i++)
            if (marble.equals(path.get(i).getMarble())) return i;

        return -1;
    }

    private int getBasePosition(Colour colour) {
        if (colour == null) {
            return -1;
        }
        switch (colour){
            case RED -> {return 0;}
            case BLUE -> {return 25;}
            case YELLOW -> {return 50;}
            case GREEN -> {return 75;}
            default -> {return -1;}
        }
    }

    private int getEntryPosition(Colour colour) {
        if (colour == null) {
            return -1;
        }
        switch (colour){
            case RED -> {return 98;}
            case BLUE -> {return 23;}
            case YELLOW -> {return 48;}
            case GREEN -> {return 73;}
            default -> {return -1;}
        }
    }

    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException{
        int pos = getPositionInPath(track, marble);
        ArrayList<Cell> safeZoneCopy = new ArrayList<>(Objects.requireNonNull(getSafeZone(marble.getColour())));
        boolean inSafeZone = false;

        for (Cell cell : safeZoneCopy)
            if (marble.equals(cell.getMarble())) {
                inSafeZone = true;
                break;
            }

        if (!inSafeZone && pos == -1) throw new IllegalMovementException("Marble cannot be moved!");

        if (!inSafeZone) // -> if marble is on track
            return getJourneyTrack(marble, steps, pos, safeZoneCopy);
        else
            return getJourneySafeZone(marble, steps, safeZoneCopy);
    }

    private void validatePath(Marble marble, ArrayList<Cell> path, boolean destroy) throws IllegalMovementException {
        int marblesEncountered = 0;

        for (Cell cell : path)
        {
            if (cell == path.getFirst()) continue;
            Marble currentMarble = cell.getMarble();

            if (currentMarble != null)
            {
                destroy = (cell.getCellType() == CellType.SAFE) ? false : destroy;
                if (currentMarble.getColour() == gameManager.getActivePlayerColour() && !destroy) throw new IllegalMovementException("Cannot move past own marbles!");
                if (++marblesEncountered > 1 && !destroy) throw new IllegalMovementException("Path blockage!");

                if (cell.getCellType() == CellType.ENTRY && !destroy && getPositionInPath(track, currentMarble) == getEntryPosition(currentMarble.getColour()))
                    throw new IllegalMovementException("SafeZone entry blocked!");
                if (cell.getCellType() == CellType.BASE && getPositionInPath(track, currentMarble) == getBasePosition(currentMarble.getColour()))
                    throw new IllegalMovementException("Base Cell blockage!");
            }



        }
    }

    private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy)throws IllegalDestroyException{

        int posTrack = getPositionInPath(track, marble), posSafe;

        //remove marble from current cell
        if (posTrack == -1)
        {
            posSafe = getPositionInPath(getSafeZone(marble.getColour()), marble);
            if (posSafe == -1)
                throw new IllegalDestroyException("Marble cannot be destroyed!");
            getSafeZone(marble.getColour()).get(posSafe).setMarble(null);
        } else
            track.get(posTrack).setMarble(null);

        //marble destroying
        for (Cell cell : fullPath) {
            if (cell.getMarble() != null && destroy) {
                validateDestroy(getPositionInPath(track, cell.getMarble()));
                cell.setMarble(null);
            }
        }

        //if the target cell is a trap, we need to destroy the marble
        if (fullPath.getLast().isTrap())
        {
            fullPath.getLast().setMarble(null);
            fullPath.getLast().setTrap(false);
            assignTrapCell();
        } else fullPath.getLast().setMarble(marble);
    }

    private void validateSwap(Marble marble_1, Marble marble_2) throws IllegalSwapException {
        // Ensure at least one marble is on the track
        if (getPositionInPath(track, marble_1) == -1 || getPositionInPath(track, marble_2) == -1) {
            throw new IllegalSwapException("One of the marbles is not on the track");
        }

        // Check if marble_1 is on the base
        int marble1Position = getPositionInPath(track, marble_1);
        if (!marble_1.getColour().equals(gameManager.getActivePlayerColour())
                && marble1Position == getBasePosition(marble_1.getColour())) {
            throw new IllegalSwapException("The Opponent's marble is on the base");
        }

        // Check if marble_2 is on the base
        int marble2Position = getPositionInPath(track, marble_2);
        if (!marble_2.getColour().equals(gameManager.getActivePlayerColour())
                && marble2Position == getBasePosition(marble_2.getColour())) {
            throw new IllegalSwapException("The Opponent's marble is on the base");
        }
    }

    private void validateDestroy(int positionInPath) throws IllegalDestroyException{
        if(positionInPath == -1)
            throw new IllegalDestroyException("The marble is not on the track");

        if(track.get(positionInPath).getCellType() == CellType.BASE)
            throw new IllegalDestroyException("The marble is on the base");
    }

    private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException{
        if(occupiedBaseCell.getMarble().getColour() == gameManager.getActivePlayerColour())
            throw new CannotFieldException("The base cell is occupied by the same colour");
    }

    private void validateSaving(int positionInSafeZone, int positionOnTrack) throws InvalidMarbleException{
        if(positionInSafeZone != -1 || positionOnTrack == -1)
            throw new InvalidMarbleException("The marble is on the safe zone or not on the track");
    }

    @Override
    public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {
        ArrayList<Cell> journey = validateSteps(marble, steps);
        validatePath(marble, journey, destroy);
        move(marble, journey, destroy);

    }

    @Override
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
        Cell baseTarget = track.get(getBasePosition(marble.getColour()));
        Marble occupyingMarble = baseTarget.getMarble();
        if (occupyingMarble != null)
        {
            validateFielding(baseTarget);
            int occupyingMarblePosition = getBasePosition(occupyingMarble.getColour());
            if (occupyingMarblePosition != -1) {
                validateDestroy(occupyingMarblePosition);
            }
            destroyMarble(occupyingMarble);
            track.get(getBasePosition(marble.getColour())).setMarble(marble);
        } else
            track.get(getBasePosition(marble.getColour())).setMarble(marble);
    }

    @Override
    public void sendToSafe(Marble marble) throws InvalidMarbleException {
        int posTrack = getPositionInPath(track, marble);
        ArrayList<Cell> safeZoneCopy = getSafeZone(marble.getColour());
        int posSafe = getPositionInPath(safeZoneCopy, marble);

        validateSaving(posSafe, posTrack);
        if (posTrack != -1) {
            track.get(posTrack).setMarble(null);
        }
        ArrayList<Cell> unoccupied = new ArrayList<>(4);
        for (Cell cell : getSafeZone(marble.getColour())) {
            if (cell.getMarble() == null)
                unoccupied.add(cell);
        }

        int randCell = (int) (Math.random() * unoccupied.size());
        unoccupied.get(randCell).setMarble(marble);
    }

    @Override
    public ArrayList<Marble> getActionableMarbles() {
        ArrayList<Marble> marbles = new ArrayList<>();
        for (Cell cell : track)
        {
            if (cell.getMarble() != null)
                marbles.add(cell.getMarble());
        }
        for (Cell cell : Objects.requireNonNull(getSafeZone(gameManager.getActivePlayerColour())))
        {
            if (cell.getMarble() != null)
                marbles.add(cell.getMarble());
        }
        return marbles;
    }



    // Helpers
    private ArrayList<Cell> getJourneyTrack(Marble marble, int steps, int pos, ArrayList<Cell> safeZoneCopy) throws IllegalMovementException {
        int entryPos = getEntryPosition(marble.getColour());
        int stepsTillEntryPos = (entryPos - pos + 100) %100; // add 100 then modulos 100 in case the track loops.

        if (steps > stepsTillEntryPos + 4) throw new IllegalMovementException("Rank too high!");

        ArrayList<Cell> journey = new ArrayList<>();
        if (steps > stepsTillEntryPos && steps != 5)
        {
            int count = 0;
            for (int i = 0; i <= steps; i++)
            {
                if (i <= stepsTillEntryPos)
                    journey.add(track.get((pos +i)%100));
                else
                    journey.add(safeZoneCopy.get(count++));
            }
            return journey;
        }

        for (int i = 0; i <= steps; i++)
            journey.add(track.get((pos +i)%100));

        return journey;
    }
    private ArrayList<Cell> getJourneySafeZone(Marble marble, int steps, ArrayList<Cell> safeZoneCopy) throws IllegalMovementException {

        int pos = getPositionInPath(safeZoneCopy, marble);
        if (steps < 0) throw new IllegalMovementException("Cannot move backwards in safe zone!");

        if (pos + steps > safeZoneCopy.size() - 1) throw new IllegalMovementException("Rank too high!");

        ArrayList<Cell> journey = new ArrayList<>();
        for (int i = 0; i <= steps; i++)
            journey.add(safeZoneCopy.get(pos+i));
        return journey;
    }
}