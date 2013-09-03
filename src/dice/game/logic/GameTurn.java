package dice.game.logic;

import java.util.ArrayList;

import dice.game.designpatterns.Observer;
import dice.game.logic.Cup;

public class GameTurn implements dice.game.designpatterns.Subject

{
	private ArrayList<Object> observers = new ArrayList<Object>();
	private int PlayerId;
	private Integer PreviusPlayerId;
	private Cup theCup;
	private int shakes = 0;
	private int directionToGo = 0;
	private int lifts = 0;
	private boolean wasMiaCalled = false;

	public void setWasMiaCalled(boolean wasMiaCalled)
	{
		this.wasMiaCalled = wasMiaCalled;
	}

	public GameTurn(int inPlayer, Integer inPassedFromPlayer, Cup inCup)
	{

		this.PlayerId = inPlayer;
		this.PreviusPlayerId = inPassedFromPlayer;
		this.theCup = inCup;
//		this.shakes = 0;
	//	this.lifts = 0;

	}

	// phase 1

	public void shakeCup()
	{
		shakes++;
		theCup.roll();
	}

	// public int getPlayerId(){
	// return PlayerId;
	// }

	public void liftCup()
	{

		if (shakes < 1)
		{
			notifyObservers("askPlayerIfHeWasRight");
		}
		if (lifts < 1)
		{
			lifts++;
			notifyObservers("showTheCub");
		}
	}

	public int[] whatDidIRoll()
	{
		return theCup.getCurrentRoll();
	}

	public void passToNextPlayer()
	{
		if (shakes > 0)
		{
			notifyObservers("passToRight");
		}
	}

	public void wasThePlayerRight(boolean inTrueOrFalse)
	{
		if (wasMiaCalled)
		{
			if (inTrueOrFalse)
			{
				notifyObservers("currentMeyer");
			} else
			{
				notifyObservers("previusMeyer");
			}
		} else
		{
			if (inTrueOrFalse)
			{
				notifyObservers("currentWin");
			} else
			{
				notifyObservers("previusWin");
			}
		}

	}

	public int getDirectionToGo()
	{
		return directionToGo;
	}

	// en game turn skal f�rst ha en gameplayer.

	public void registerObserver(Observer o)
	{

		observers.add(o);
	}

	public void removeObserver(Observer o)
	{
		observers.remove(o);
	}

	public void notifyObservers(String event)
	{

		for (int i = 0; i < observers.size(); i++)
		{
			((Observer) observers.get(i)).update(event);

		}

	}
}