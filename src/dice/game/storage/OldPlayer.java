package dice.game.storage;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;


/*
 * This clase contains "old" players, mening players who have allready played the game
 * the pourpose is to get easy acces to those players in future games.
 */
public class OldPlayer {
	private Drawable Picture;
	private String name;
	private int oldPlayerhashCode;
	private ArrayList<Double> GameScore = new ArrayList<Double>();

	public OldPlayer() 
	{
		oldPlayerhashCode = (int) (System.currentTimeMillis() * 31);
	}
	public void addGameScore(int gameFinishNr, int numberOfPlayers) {
		Double gameScore = (double) (numberOfPlayers / gameFinishNr);
		GameScore.add(gameScore);
	}

	public int getGamesPlayed() {
		return GameScore.size();
	}

	public Double getGameScore() {
		Double avarage = 0.0;
		for (Double score : GameScore) {
			avarage += score;
		}
		return (avarage / GameScore.size());
	}

	public Drawable getPicture() {
		return Picture;
	}

	public String getName() {
		return name;
	}

	 public void setPicture(Drawable picture) {
	 Picture = picture;
	 }

	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {

		return oldPlayerhashCode;

	}
}
