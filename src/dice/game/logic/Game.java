package dice.game.logic;

import java.util.ArrayList;
import java.util.LinkedList;

import android.widget.Toast;
import dice.game.android.TheGame;
import dice.game.designpatterns.Observer;
import dice.game.logic.Cup;

public class Game implements dice.game.designpatterns.Observer, dice.game.designpatterns.Subject {
    private ArrayList<Object> observers = new ArrayList<Object>();
    private LinkedList<GamePlayer> gamePlayerList = new LinkedList<GamePlayer>();
    private LinkedList<GamePlayer> deadPlayerList = new LinkedList<GamePlayer>();
    private int currentplayerInt = 0;
    private boolean TestForDeadPlayer = false;

    private int PassedFromPlayer = -1;
    private Cup gameCup = new Cup();
    private GameTurn myGameTurn = null;
    private Observer myObserver;

    public Game(Observer inObserver) {
        myObserver = inObserver;
        registerObserver(inObserver);
    }

    public void setGameTurn(GameTurn inGameTurn) {
        myGameTurn = inGameTurn;
    }

    public GameTurn getGameTurn() {
        return myGameTurn;
    }

    public void playGame() {
        // while we have more than 1 player
        if (gamePlayerList.size() > 1) {
            myGameTurn = new GameTurn(currentplayerInt, PassedFromPlayer, gameCup);
            myGameTurn.registerObserver(this);
            myGameTurn.registerObserver(myObserver);
        } else {
            System.out.println("-- The game is done --");
        }

    }
    private boolean checkIfGameIsOver(){


        if(gamePlayerList.size()==1){


            /*

            do somthing awsome too
             */

            return true;
        }else{
            return false;
        }

    }
    private void endTurnCheck(String result) {
//		int pass;
//		int previusWin;
//		int currentWin;
//		int previusMeyer;
//		int currentMeyer;
//
//		int resultInt;
        GamePlayer tmpPlayer = gamePlayerList.get(currentplayerInt);

        if (result.equals("passToRight")) {
            PassedFromPlayer = currentplayerInt;
            System.out.println("PassedFromPlayer " + PassedFromPlayer);
            passToNextPlayer();
            TestForDeadPlayer = false;



        } else if (result.equals("previusWin")) {

            tmpPlayer.loseALife();
            gamePlayerList.set(currentplayerInt, tmpPlayer);
            PassedFromPlayer = -1; // current player starts
            // round since he/she lost
            TestForDeadPlayer = true;

            checkIfGameIsDone();

            notifyObservers("updatescoreboard");
        } else if (result.equals("currentWin")) {

            PassedFromPlayer = currentplayerInt;

            // int count;
            // currentplayerInt
            int count = currentplayerInt - 1;

            /*
            previus player gets the turn, but he is not awardd a new gameturn FIX it..

             */

            if (count < 0) {
                count = gamePlayerList.size() - 1;
            }
            gamePlayerList.get(count).loseALife();

            // gamePlayerList.set(count, tmpPlayer);
            // TestForDeadPlayer = true;
            if (gamePlayerList.get(count).getLives() < 1) {
                deadPlayerList.add(gamePlayerList.get(count));
                gamePlayerList.remove(count);
            }

            checkIfGameIsDone();

            notifyObservers("GoLeft");

            //notifyObservers("updatescoreboard");




        } else if (result.equals("previusMeyer")) {

            // passToPreviousPlayer();
            tmpPlayer.loseALife();
            tmpPlayer.loseALife();
            gamePlayerList.set(currentplayerInt, tmpPlayer);
            PassedFromPlayer = -1; // current player starts
            // round since he/she lost
            TestForDeadPlayer = true;

            checkIfGameIsDone();


            notifyObservers("updatescoreboard");


        } else if (result.equals("currentMeyer")) {
            PassedFromPlayer = currentplayerInt;
            int count1 = currentplayerInt - 1;
            if (count1 < 0) {
                count1 = gamePlayerList.size() - 1;
            }
            gamePlayerList.get(count1).loseALife();
            gamePlayerList.get(count1).loseALife();
            // gamePlayerList.set(count, tmpPlayer);
            // TestForDeadPlayer = true;
            if (gamePlayerList.get(count1).getLives() < 1) {
                deadPlayerList.add(gamePlayerList.get(count1));
                gamePlayerList.remove(count1);
            }
             //we might have a problem, if u have 0 lifes and loose 1.

            checkIfGameIsDone();

            notifyObservers("GoLeft");

        } else {
            // u broke the rules!!!!
        }

        if (TestForDeadPlayer == true) {
            tmpPlayer = gamePlayerList.get(currentplayerInt);
            if (tmpPlayer.getLives() < 1) {
                deadPlayerList.add(tmpPlayer);
                gamePlayerList.remove(currentplayerInt);
                // passToPreviousPlayer();
                notifyObservers("GoLeft");
            }
        }

        playGame();


    }
    private boolean checkIfGameIsDone(){
                     if(gamePlayerList.size()==1){
                         /*

                         do somthing awsome
                          */
                         notifyObservers("gameOver");

                         return true;
                     }               else{
                         return false;
                     }
    }
    public void addPlayer(GamePlayer inGamePlayer) {
        gamePlayerList.add(inGamePlayer);
    }

    public GamePlayer getCurrentPlayer() {
        return gamePlayerList.get(currentplayerInt);
    }

    public GamePlayer getPlayer(int playerToGet) {
        return gamePlayerList.get(playerToGet);
    }

    public GamePlayer getPassedFrom() {
        return gamePlayerList.get(PassedFromPlayer);
    }

    public int getCurrentPlayerInt() {
        return currentplayerInt;
    }

    public void setCurrentPlayer(int inPlayer) {
        currentplayerInt = inPlayer;
    }

    public void passToNextPlayer() {
        if (currentplayerInt < (gamePlayerList.size() - 1)) {
            currentplayerInt++;
        } else {
            currentplayerInt = 0;
        }
        System.out.println("currentplayerInt" + currentplayerInt);
    }

    public void passToPreviousPlayer() {
        System.out.println("PASSING TO PREVIUS PLAYER");

        if (currentplayerInt == 0) {
            currentplayerInt = (gamePlayerList.size() - 1);
        } else {
            currentplayerInt--;
        }
    }

    public void switchPlayerPositions(int inPosition1, int inPostion2) {

        GamePlayer tmpGamePlayer = gamePlayerList.get(inPosition1);
        gamePlayerList.set(inPosition1, gamePlayerList.get(inPostion2));
        gamePlayerList.set(inPostion2, tmpGamePlayer);
    }

    public int getPlayerCount() {
        return gamePlayerList.size();
    }

    @Override
    public void update(String event) {
        System.out.println("Observer says " + event);
        endTurnCheck(event);
    }

    public void registerObserver(Observer o) {

        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String event) {

        for (int i = 0; i < observers.size(); i++) {
            ((Observer) observers.get(i)).update(event);

        }

    }

    public ArrayList<GamePlayer> getScoreBoardPlayers(int currentplayerpos) {

        ArrayList<GamePlayer> scoreboardList = new ArrayList<GamePlayer>();


        currentplayerpos -= 2;

        System.out.println("currentplauyerpos aftern -2" + currentplayerpos);



        if (currentplayerpos < 0 && gamePlayerList.size() >= 2) {
            System.out.println("AM LESS THAN ZERO");

            currentplayerpos = (gamePlayerList.size() + currentplayerpos);

            System.out.println("less than zero - 2 is now" + currentplayerpos);

        }
        else if(gamePlayerList.size() == 1){
            currentplayerpos = 0;
        }

        boolean allreadyseen = false;

        while (scoreboardList.size() < 5 && allreadyseen == false) {

            while (currentplayerpos < gamePlayerList.size() && scoreboardList.size() < 5 && allreadyseen == false) // mig er mindre end st�rrelsen p� gameplayerlist ,
            {

                System.out.println("adding player" + currentplayerpos);
                if (!scoreboardList.contains(gamePlayerList.get(currentplayerpos))) {

                    scoreboardList.add(gamePlayerList.get(currentplayerpos));
                    currentplayerpos++;
                } else allreadyseen = true;


            }
            currentplayerpos = 0;
        }
        if (scoreboardList.size() == 3) {
            ArrayList<GamePlayer> temp = new ArrayList<GamePlayer>();
            temp.add(scoreboardList.get(1));
            temp.add(scoreboardList.get(2));
            temp.add(scoreboardList.get(0));
            scoreboardList = temp;
        }
        return scoreboardList;
//		}

    }

}