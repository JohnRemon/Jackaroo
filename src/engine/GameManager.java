package engine;

import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import model.Colour;
import model.player.Marble;
import model.player.Player;

import java.util.ArrayList;

public interface GameManager {
	public void sendHome(Marble marble);
    public void fieldMarble() throws CannotFieldException, IllegalDestroyException;
    public void discardCard(Colour colour) throws CannotDiscardException;
    public void discardCard() throws CannotDiscardException;
    public Colour getActivePlayerColour();
    public Colour getNextPlayerColour();
    public int getCurrentPlayerIndex();
    public ArrayList<Player> getPlayers();
}
