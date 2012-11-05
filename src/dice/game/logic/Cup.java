package dice.game.logic;

import java.util.Random;
public class Cup
{
	int lastRoll[] = new int[2];
	int currentRoll[] = new int[2];
	  Random randomGenerator = new Random();
	public void roll()
	{
System.arraycopy(currentRoll, 0, lastRoll, 0, currentRoll.length);
		currentRoll[0] = 1 + randomGenerator.nextInt(6);
		currentRoll[1] = 1 + randomGenerator.nextInt(6);
	}

	public int[] getLastRoll()
	{
		return lastRoll;
	}

	public int[] getCurrentRoll()
	{
		return currentRoll;
	}
	public String whatDidIRoll(int roll[]){
		
		int Dice1 = roll[0];
		int Dice2 = roll[1];
		
		//set dice 1 to allways being the bigger
		if(Dice1 < Dice2){
			int temp = Dice1;
			Dice1 = Dice2;
			Dice2 = temp;
		}
		
		
		// a Meyer!
		if(Dice1==2 && Dice2 ==1){
			
		}
		// Lille Meyer
		else if(Dice1== 3 && Dice2 == 1){
			
		}
		//Pair
		else if(Dice1==Dice2){
			
		}
		//et tal
		else{
			
		}
		
		
		
		return null;
		
	}

}