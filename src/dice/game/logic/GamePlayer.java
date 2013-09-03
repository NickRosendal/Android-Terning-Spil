package dice.game.logic;

import java.io.Serializable;

//import android.graphics.drawable.Drawable;
import dice.game.storage.OldPlayer;

public class GamePlayer implements Serializable

{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int turnNr;
	private int lives;
	private OldPlayer myOldPlayer;

	public GamePlayer(OldPlayer inOldPlayer)
	{
		myOldPlayer = inOldPlayer;
		lives = 6;
	}

	public int getTurnNr()
	{
		return turnNr;
	}

	public void setTurnNr(int turnNr)
	{
		this.turnNr = turnNr;
	}

	public int getLives()
	{
		return lives;
	}

	public OldPlayer getOldPlayer()
	{
		return myOldPlayer;
	}

	public void setLives(int lives)
	{
		this.lives = lives;
	}

	public void loseALife()
	{
		lives--;
	}

	// public Drawable getPicture() {
	// return myOldPlayer.getPicture();
	// }

	public String getName()
	{
		return myOldPlayer.getName();
	}
}
