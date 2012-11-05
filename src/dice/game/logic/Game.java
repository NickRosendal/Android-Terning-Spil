package dice.game.logic;

import java.util.LinkedList;
import dice.game.logic.Cup;

public class Game implements dice.game.designpatterns.Observer
{
	private LinkedList<GamePlayer> gamePlayerList = new LinkedList<GamePlayer>();
	private LinkedList<GamePlayer> deadPlayerList = new LinkedList<GamePlayer>();
	private int currentplayerInt = 0;
	private boolean TestForDeadPlayer = false;

	private int PassedFromPlayer = -1;
	private Cup gameCup = new Cup();
	private GameTurn myGameTurn = null;

	public void setGameTurn(GameTurn inGameTurn)
	{
		myGameTurn = inGameTurn;
	}

	public GameTurn getGameTurn()
	{
		return myGameTurn;
	}

	public void playGame()
	{
		// while we have more than 1 player
		if (gamePlayerList.size() > 1)
		{
			myGameTurn = new GameTurn(currentplayerInt, PassedFromPlayer, gameCup);
			myGameTurn.registerObserver(this);

		} else
		{
			System.out.println("-- The game is done --");
		}

	}

	private void endTurnCheck(String result)
	{
		int pass;
		int previusWin;
		int currentWin;
		int previusMeyer;
		int currentMeyer;

		int resultInt;
		GamePlayer tmpPlayer = gamePlayerList.get(currentplayerInt);

		if (result.equals("pass"))
		{
			passToNextPlayer();
			TestForDeadPlayer = false;
		} else if (result.equals("previusWin"))
		{
			tmpPlayer.loseALife();
			gamePlayerList.set(currentplayerInt, tmpPlayer);
			PassedFromPlayer = -1; // current player starts
									// round since he/she lost
			TestForDeadPlayer = true;
		} else if (result.equals("currentWin"))
		{
			passToPreviousPlayer();
			tmpPlayer = gamePlayerList.get(currentplayerInt);
			tmpPlayer.loseALife();
			gamePlayerList.set(currentplayerInt, tmpPlayer);
			TestForDeadPlayer = true;
		} else if (result.equals("previusMeyer"))
		{
			tmpPlayer.loseALife();
			tmpPlayer.loseALife();
			gamePlayerList.set(currentplayerInt, tmpPlayer);
			PassedFromPlayer = -1; // current player starts
									// round since he/she lost
			TestForDeadPlayer = true;
		} else if (result.equals("currentMeyer"))
		{
			passToPreviousPlayer();
			tmpPlayer = gamePlayerList.get(currentplayerInt);
			tmpPlayer.loseALife();
			tmpPlayer.loseALife();
			gamePlayerList.set(currentplayerInt, tmpPlayer);
			TestForDeadPlayer = true;
		} else
		{
			// u broke the rules!!!!
		}

		if (TestForDeadPlayer == true)
		{
			tmpPlayer = gamePlayerList.get(currentplayerInt);
			if (tmpPlayer.getLives() < 1)
			{
				deadPlayerList.add(tmpPlayer);
				gamePlayerList.remove(currentplayerInt);
				// passToPreviousPlayer();
			}
		}

		playGame();

		// FIX JAVA 7 TAK?!

		// switch (result)
		// {
		// case pass:
		// passToNextPlayer();
		// TestForDeadPlayer = false;
		// break;
		// case previusWin:
		//
		// tmpPlayer.loseALife();
		// gamePlayerList.set(currentplayerInt, tmpPlayer);
		// PassedFromPlayer = -1; // current player starts
		// // round since he/she lost
		// TestForDeadPlayer = true;
		// break;
		// case currentWin:
		//
		// passToPreviousPlayer();
		// tmpPlayer = gamePlayerList.get(currentplayerInt);
		// tmpPlayer.loseALife();
		// gamePlayerList.set(currentplayerInt, tmpPlayer);
		// TestForDeadPlayer = true;
		// break;
		// case previusMeyer:
		// tmpPlayer.loseALife();
		// tmpPlayer.loseALife();
		// gamePlayerList.set(currentplayerInt, tmpPlayer);
		// PassedFromPlayer = -1; // current player starts
		// // round since he/she lost
		// TestForDeadPlayer = true;
		// break;
		// case currentMeyer:
		// passToPreviousPlayer();
		// tmpPlayer = gamePlayerList.get(currentplayerInt);
		// tmpPlayer.loseALife();
		// tmpPlayer.loseALife();
		// gamePlayerList.set(currentplayerInt, tmpPlayer);
		// TestForDeadPlayer = true;
		// break;
		// }

	}

	public void addPlayer(GamePlayer inGamePlayer)
	{
		gamePlayerList.add(inGamePlayer);
	}

	public GamePlayer getCurrentPlayer()
	{
		return gamePlayerList.get(currentplayerInt);
	}

	public int getCurrentPlayerInt()
	{
		return currentplayerInt;
	}

	public void setCurrentPlayer(int inPlayer)
	{
		currentplayerInt = inPlayer;
	}

	public void passToNextPlayer()
	{
		if (currentplayerInt < (gamePlayerList.size() - 1))
		{
			currentplayerInt++;
		} else
		{
			currentplayerInt = 0;
		}
	}

	public void passToPreviousPlayer()
	{
		if (currentplayerInt == 0)
		{
			currentplayerInt = (gamePlayerList.size() - 1);
		} else
		{
			currentplayerInt--;
		}
	}

	public void switchPlayerPositions(int inPosition1, int inPostion2)
	{

		GamePlayer tmpGamePlayer = gamePlayerList.get(inPosition1);
		gamePlayerList.set(inPosition1, gamePlayerList.get(inPostion2));
		gamePlayerList.set(inPostion2, tmpGamePlayer);
	}

	@Override
	public void update(String event)
	{
		System.out.println("Observer says " + event);
		endTurnCheck(event);
	}

}