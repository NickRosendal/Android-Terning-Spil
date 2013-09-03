package dice.game.storage;


import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/*
 * This clase contains "old" players, mening players who have allready played the game
 * the pourpose is to get easy acces to those players in future games.
 */
public class OldPlayer implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String PictureLocation;
	private String name;
	private int oldPlayerhashCode;
	private ArrayList<Double> GameScore = new ArrayList<Double>();
	
	public OldPlayer()
	{
		oldPlayerhashCode = (int) (System.currentTimeMillis() * 31);
	}

	public void addGameScore(int gameFinishNr, int numberOfPlayers)
	{
		Double gameScore = (double) (numberOfPlayers / gameFinishNr);
		GameScore.add(gameScore);
	}

	public int getGamesPlayed()
	{
		return GameScore.size();
	}

	public Double getGameScore()
	{
		Double avarage = 0.0;
		for (Double score : GameScore)
		{
			avarage += score;
		}
		return (avarage / GameScore.size());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int hashCode()
	{

		return oldPlayerhashCode;

	}

	public void setPictureLocation(String photoName)
	{
		PictureLocation = photoName;
	}

	public Bitmap getPictureBitmap()
	{
		
		Bitmap bitmap = BitmapFactory.decodeFile(PictureLocation);
		return bitmap;
	}

	public String getPictureLocation()
	{
		return PictureLocation;
	}
}
