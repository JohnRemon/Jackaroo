package engine.board;

import engine.GameManager;
import exception.*;
import model.Colour;
import model.player.Marble;
import model.player.Player;

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

    //validateSteps start

    private void validatePath(Marble marble, ArrayList<Cell> path, boolean destroy) throws IllegalMovementException {
        int marblesEncountered = 0;
        for (int i = 1; i < path.size(); i++)
        {
            Cell cell = path.get(i);
            boolean enterSafeZone = false;
            Marble currentMarble = cell.getMarble();
            if (i != path.size()-1)
            {
                if (path.get(i+1).getCellType() == CellType.SAFE)
                    enterSafeZone = true;
            }
            if (currentMarble != null)
            {
                destroy = cell.getCellType() != CellType.SAFE && destroy;
                if (currentMarble.getColour() == gameManager.getActivePlayerColour() && !destroy)
                    throw new IllegalMovementException("Cannot move past own marbles!");
                if (marblesEncountered++ > 1 && !destroy)
                    throw new IllegalMovementException("Path blockage!");
                if (cell.getCellType() == CellType.ENTRY && !destroy && enterSafeZone)
                {
                    throw new IllegalMovementException("SafeZone entry blocked!");
                }
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
                throw new IllegalDestroyException("Marble cannot be moved!");
            getSafeZone(marble.getColour()).get(posSafe).setMarble(null);
        } else
            track.get(posTrack).setMarble(null);

        //marble destroying
        for (Cell cell : fullPath) {
            if (cell.getMarble() != null && destroy) {
                int position = getPositionInPath(track, cell.getMarble());
                if (position != -1)
                {
                    validateDestroy(position);
                    track.get(position).setMarble(null);
                } else throw new IllegalDestroyException("Marble cannot be destroyed!");
            }
        }

        //if the target cell is a trap, we need to destroy the marble
        if (fullPath.getLast().isTrap())
        {
            destroyMarble(fullPath.getLast().getMarble());
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
        // if in safezone or not on board
        if (positionInPath < 0)
            throw new IllegalDestroyException("The marble is not on the track");
        Cell cell = track.get(positionInPath);
        if (cell.getMarble() != null && cell.getCellType() == CellType.BASE && cell.getMarble().getColour() == gameManager.getActivePlayerColour())
            throw new IllegalDestroyException("Cannot destroy a marble on its base cell!");
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
    int position = getPositionInPath(track, marble);
    Cell cell = track.get(position);
    validateDestroy(position);

    if (cell.getCellType() == CellType.BASE)
       throw new IllegalDestroyException("Cannot destroy a marble on its base cell!");

    track.get(position).setMarble(null);
    gameManager.sendHome(marble);
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



    private ArrayList<Cell> validateSteps (Marble marble, int steps) throws IllegalMovementException
    {
        //marble shouldn't be null anyways
        if (marble == null) throw new IllegalMovementException("No marble to move!");


        int currentPosition = getPositionInPath(track, marble);
        boolean onTrack = true, ownMarble = gameManager.getActivePlayerColour() == marble.getColour();
        ArrayList<Cell> journey = new ArrayList<>();
        ArrayList<Cell> safeZoneOfPlayer = getSafeZone(gameManager.getActivePlayerColour());
        if (currentPosition == -1)
        {
            currentPosition = getPositionInPath(safeZoneOfPlayer, marble);
            if (currentPosition == -1) throw new IllegalMovementException("Marble is not on board");
            onTrack = false;
        }
        if (onTrack)
        {
            int stepsTillEntry = getEntryPosition(marble.getColour())-currentPosition;

            if (!ownMarble)
                return getJourneyNoSafeZone(steps, stepsTillEntry, journey, currentPosition, safeZoneOfPlayer);

            if (steps != -4)
            {
                return getForwardJourney(steps, stepsTillEntry, journey, currentPosition, safeZoneOfPlayer);
            } else //using -4 card
            {
                for (int i = 1; i <= 4; i++) {
                    journey.add(track.get((currentPosition - i + 100) % 100));
                } //move backwards, with wrapping
                return journey;
            }
        }
        else //safezone movement
        {
            if (steps == -4 || steps + (currentPosition+1) > 4)
                throw new IllegalMovementException("Invalid movement in safezone");

            for (int i = 0; i < steps+1; i++)
            {
                journey.add(safeZoneOfPlayer.get(i+currentPosition));
            }
            return journey;
        }
    }

    private ArrayList<Cell> getForwardJourney(int steps, int stepsTillEntry, ArrayList<Cell> journey, int currentPosition, ArrayList<Cell> safeZoneOfPlayer) throws IllegalMovementException {
        if ((steps  - (stepsTillEntry)) > safeZoneOfPlayer.size()) {
            throw new IllegalMovementException("Rank too high");
        }

        if (stepsTillEntry < 0) //this is the case where marble moves normally on track, no safezone entry
        {
            return getJourneyNoSafeZone(steps, stepsTillEntry, journey, currentPosition, safeZoneOfPlayer);
        }

        int counter = 0; //used to count how many safezone cells passed if it enters safe zone
        for (int i = 0; i < steps+1; i++) //case where marble enters a safezone from track
        {
           if (i <= stepsTillEntry)
                journey.add(track.get((i+ currentPosition)%100));
           else //entered safezone
           {
               journey.add(safeZoneOfPlayer.get(counter++));
           }
        }
        if (counter > 4) {
            throw new IllegalMovementException("Rank played was too high");
        }
        return journey;
    }

    private ArrayList<Cell> getJourneyNoSafeZone(int steps, int stepsTillEntry, ArrayList<Cell> journey, int currentPosition, ArrayList<Cell> safeZoneOfPlayer)
    {
        for (int i = 0; i <= steps; i++)
        {
            journey.add(track.get((currentPosition+i+100)%100));
        }
        return journey;
    }
}