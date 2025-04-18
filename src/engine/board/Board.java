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
        switch (colour){
            case RED -> {return 0;}
            case BLUE -> {return 25;}
            case YELLOW -> {return 50;}
            case GREEN -> {return 75;}
            default -> {return -1;}
        }
    }

    private int getEntryPosition(Colour colour) {
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

        for (Cell cell : path)
        {
            if (cell == path.getFirst()) continue;
            Marble currentMarble = cell.getMarble();

            if (currentMarble != null)
            {
                destroy = (cell.getCellType() == CellType.SAFE) ? false : destroy;
                if (currentMarble.getColour() == marble.getColour() && !destroy) throw new IllegalMovementException("Cannot move past own marbles!");
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

    private void validateSwap(Marble marble_1, Marble marble_2) throws IllegalSwapException{
        if(getPositionInPath(track, marble_1) == -1 || getPositionInPath(track, marble_2) == -1)    //this needs to be OR since you can't swap with 2 marbles if one is in a safe zone
            throw new IllegalSwapException("One of the marbles is not on the track");

        if(!marble_1.getColour().equals(gameManager.getActivePlayerColour()) && getBasePosition(marble_1.getColour()) != -1)
            throw new IllegalSwapException("The Opponent's marble is on the base");

        if(!marble_2.getColour().equals(gameManager.getActivePlayerColour()) && getBasePosition(marble_2.getColour()) != -1)
            throw new IllegalSwapException("The Opponent's marble is on the base");

        if (marble_1.getColour() == marble_2.getColour())
            throw new IllegalSwapException("Cannot swap owned marbles!");
        /*
        Same Player Ownership: Marbles owned by the same player are ineligible for swapping.
        Technically a completely useless check since for the player no difference was made, but you never know with the test cases.
        */
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
        Cell target = track.get(getBasePosition(marble.getColour()));

        Marble occupyingMarble = target.getMarble();
        if (occupyingMarble != null)
        {
            validateFielding(target);
            validateDestroy(getPositionInPath(track, occupyingMarble));
            sendToBase(occupyingMarble); //potentially bad recursion but should be safe
            target.setMarble(marble);
        } else target.setMarble(marble);
    }

    @Override
    public void sendToSafe(Marble marble) throws InvalidMarbleException {
        int posTrack = getPositionInPath(track, marble);
        ArrayList<Cell> safeZoneCopy = getSafeZone(marble.getColour());
        int posSafe = getPositionInPath(safeZoneCopy, marble);

        validateSaving(posSafe, posTrack);

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

        int currentPosition = getPositionInPath(track, marble);
        boolean onTrack = true;
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

            if (steps == 5) //using 5 card to control a random marble
            {
                for (int i = 0; i < steps+1; i++)
                {
                    Marble occupyingMarble = track.get(getPositionInPath(track, marble)).getMarble();
                    if (occupyingMarble != null)
                    {
                        if (occupyingMarble.getColour() == gameManager.getActivePlayerColour())
                            throw new IllegalMovementException("Cannot bypass your own marbles using the 5 card");
                    }
                    journey.add(track.get((currentPosition + i) %100));
                }
                return journey;
            }

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
        if ((steps - (stepsTillEntry)+1) > safeZoneOfPlayer.size())
            throw new IllegalMovementException("Rank too high");

        if (stepsTillEntry < 0) //this is the case where marble moves normally on track, no safezone entry
        {
            for (int i = 0; i < steps+1; i++)
            {
                journey.add(track.get((i+ currentPosition)%100));
            }
            return journey;
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
        if (counter >= 4)
           throw new IllegalMovementException("Rank played was too high");
        return journey;
    }
}