package dice.game.android;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import dice.game.designpatterns.Observer;
import dice.game.helpers.GestureListener;
import dice.game.helpers.LockScreenRotation;
import dice.game.logic.Game;
import dice.game.logic.GamePlayer;
import dice.game.logic.GameTurn;
import dice.game.storage.OldPlayer;

public class TheGame extends Activity implements dice.game.designpatterns.Observer
{
	private ImageView player;

	private Animation LIFTCUB;
	private Animation DOWNCUB;
	private Animation COMEFROMTHELEFT;
	private Animation GOTOTHERIGHT;
	private RelativeLayout layout;
	private Animation SHAKECUB;
	private ImageView player2;
	private ImageView iVcub;
	private Game theGame = new Game(this);
	private SensorManager mSensorManager;
	private SensorEventListener mySensorListener;
	private dice.game.helpers.GestureListener swipeListener;
	private TextView TestTextView;
	private ImageView diceimage1;
	private dice.game.helpers.SoundManager ourSounds;
	private ImageView diceimage2;
	private Handler handler;
	GameTurn localTurn;
	Button nextPlayerButton;

	// ImageButton wasThePlayerRightYes;
	// ImageButton WasThePlayerRightNo;
	// CheckBox wasMIACalled;
	// TextView askThePlayer;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// for some reason the title bar, and notification bar has to be removed
		// before we call this..
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_game);
		new LockScreenRotation(this);
		LIFTCUB = AnimationUtils.loadAnimation(this, R.drawable.lift);
		DOWNCUB = AnimationUtils.loadAnimation(this, R.drawable.down);
		SHAKECUB = AnimationUtils.loadAnimation(this, R.drawable.shake);

		
		ourSounds = new dice.game.helpers.SoundManager(this);
		
		swipeListener = new GestureListener();
		swipeListener.registerObserver(this);

		layout = (RelativeLayout) findViewById(R.id.gamelayout);
		diceimage1 = (ImageView) findViewById(R.id.imageDice1);
		diceimage2 = (ImageView) findViewById(R.id.imageDice2);

		player2 = (ImageView) findViewById(R.id.player2);
		player = (ImageView) findViewById(R.id.player);
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int height = (metrics.widthPixels / 16) * 9;
		player2.setMinimumHeight(height);
		player.setMinimumHeight(height);

		iVcub = (ImageView) findViewById(R.id.iv_cub);
		iVcub.setImageResource(R.drawable.cub);

		// Creates the was the player right dialong
		// wasThePlayerRightYes = (ImageButton)
		// findViewById(R.id.button_was_current_player_right_yes);
		// WasThePlayerRightNo = (ImageButton)
		// findViewById(R.id.button_was_current_player_right_no);
		// wasMIACalled = (CheckBox) findViewById(R.id.checkbox_mia_was_called);
		// askThePlayer = (TextView)
		// findViewById(R.id.textView_was_the_current_player_right);
		// WasThePlayerRightNo.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// localTurn.wasThePlayerRight(false);
		// showWasThePlayerRightDialog(false);
		// }
		// });
		// wasThePlayerRightYes.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// localTurn.wasThePlayerRight(true);
		// showWasThePlayerRightDialog(false);
		//
		// }
		// });
		// wasMIACalled.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener()
		// {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked)
		// {
		// if (isChecked)
		// {
		// localTurn.setWasMiaCalled(true);
		// } else
		// {
		// localTurn.setWasMiaCalled(false);
		// }
		//
		// }
		// });

		Bundle b = this.getIntent().getExtras();

		/*
		when using a Bundle Linklist gets converted to a Arraylist (thanks a
		lot Android)
		*/
        ArrayList<OldPlayer> inOldPlayers = (ArrayList<OldPlayer>) b.getSerializable("PlayerList");

		for (OldPlayer thisoldplayer : inOldPlayers)
		{
			theGame.addPlayer(new GamePlayer(thisoldplayer));
		}
		iVcub.setOnTouchListener(swipeListener);
		player.setImageBitmap(theGame.getCurrentPlayer().getOldPlayer().getPictureBitmap());

		theGame.playGame();
		localTurn = theGame.getGameTurn();
		// localTurn.registerObserver(this);
		createPlayerScoreBoard();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// hvis vi har haft pauset ska vi lige lytte igen
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mySensorListener = new dice.game.helpers.sensorManager(mSensorManager, this).mSensorListener;
		mSensorManager.registerListener(mySensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		// vi vil ikke have at vi bliver ved med at lytte efter rysten hvis
		// denne activity ik er åben

		mSensorManager.unregisterListener(mySensorListener);
	}

	private void createWhoWonMenu()
	{
		/*
		 * 
		 * jeg tænker vi kunne bruge det tablelayour vi har scoren i normal?
		 */

		TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout_players);
		tableLayout.removeAllViews();

		TableRow tableRow2 = new TableRow(getApplicationContext());
		tableRow2.setGravity(Gravity.CENTER);
		ImageButton prevWonB = new ImageButton(this);
		ImageButton currentWonB = new ImageButton(this);
		final ToggleButton wasMIACalled = new ToggleButton(this);


		DisplayMetrics metrics = this.getResources().getDisplayMetrics();


		int playerWidth = metrics.widthPixels / 3;
		int PlayerHeight = (playerWidth / 16) * 9;

		prevWonB.setLayoutParams(new TableRow.LayoutParams(playerWidth, PlayerHeight));

		prevWonB.setImageBitmap(theGame.getPassedFrom().getOldPlayer().getPictureBitmap());
        prevWonB.setScaleType(ImageView.ScaleType.FIT_XY);   //scales picture
        prevWonB.setBackgroundColor(0000);                  //hides button

        currentWonB.setLayoutParams(new TableRow.LayoutParams(playerWidth, PlayerHeight));
		currentWonB.setImageBitmap(theGame.getCurrentPlayer().getOldPlayer().getPictureBitmap());
        currentWonB.setScaleType(ImageView.ScaleType.FIT_XY);
        currentWonB.setBackgroundColor(0000);

		prevWonB.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				localTurn.wasThePlayerRight(false);
				
				showDices(false);
				iVcub.startAnimation(DOWNCUB);
				// ingen update

			}
		});
		currentWonB.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				localTurn.wasThePlayerRight(true);
				
				showDices(false);
				iVcub.startAnimation(DOWNCUB);

			}
		});
		wasMIACalled.setText("Blev der kaldt MIA");

		wasMIACalled.setTextOn("Ja");
		wasMIACalled.setTextOff("Nej");

		wasMIACalled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					localTurn.setWasMiaCalled(true);

				} else
				{
					localTurn.setWasMiaCalled(false);

				}

			}
		});

		// TextView whoWonText = new TextView(this);
		// whoWonText.setText("Who won?, remember to check if MIA was called!");
		// tableRow1.addView(whoWonText);
		// tableLayout.addView(tableRow1, new
		// TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));

		System.out.println("adding views");
		tableRow2.addView(prevWonB);
		tableRow2.addView(wasMIACalled);
		tableRow2.addView(currentWonB);


		System.out.println("views added");
		tableLayout.addView(tableRow2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

	}

	private void createPlayerScoreBoard()
	{
		TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout_players);
		tableLayout.removeAllViews();
		TableRow tableRow;

		// System.out.println("its my turn"+localTurn.getPlayerId());


        ArrayList<GamePlayer> scoreboardplayers = theGame.getScoreBoardPlayers(theGame.getCurrentPlayerInt());

		int amountOfPlayersToCreate = scoreboardplayers.size();
		// if (theGame.getPlayerCount() < 5)
		// amountOfPlayersToCreate = theGame.getPlayerCount();

		System.out.println("did i go this far?");
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int PADDING = 4;
		int workingWidth = metrics.widthPixels - (PADDING * amountOfPlayersToCreate);
		int playerWidth = workingWidth / 5;
		int PlayerHeight = (playerWidth / 16) * 9;
		tableRow = new TableRow(getApplicationContext());
		tableRow.setGravity(Gravity.CENTER);

		/*
		 * J = currentplayerint
		 */

		for (int j = 0; j <= scoreboardplayers.size() - 1; j++)
		{
			// System.out.println(scoreboardplayers.get(j));
			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(scoreboardplayers.get(j).getOldPlayer().getPictureBitmap());
			// imageView.setImageBitmap(theGame.getPlayer(j).getOldPlayer().getPictureBitmap());
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			imageView.setLayoutParams(new TableRow.LayoutParams(playerWidth, PlayerHeight));

			imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
			tableRow.addView(imageView);
		}
		tableLayout.addView(tableRow);

		tableRow = new TableRow(getApplicationContext());
		tableRow.setGravity(Gravity.CENTER);
		for (int j = 0; j <= scoreboardplayers.size() - 1; j++)
		{
			ImageView imageView = new ImageView(this);
			switch (scoreboardplayers.get(j).getLives())
			{
			case 1:
				imageView.setImageResource(R.drawable.dice1);
				break;
			case 2:
				imageView.setImageResource(R.drawable.dice2);
				break;
			case 3:
				imageView.setImageResource(R.drawable.dice3);
				break;
			case 4:
				imageView.setImageResource(R.drawable.dice4);
				break;
			case 5:
				imageView.setImageResource(R.drawable.dice5);
				break;
			case 6:
				imageView.setImageResource(R.drawable.dice6);
				break;
			}

			imageView.setLayoutParams(new TableRow.LayoutParams(playerWidth, playerWidth));

			imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
			tableRow.addView(imageView);
		}
		tableLayout.addView(tableRow);

	}

	private void changeImageToPreviousPlayer(Bitmap fromPlayer, Bitmap toPlayer)
	{
		COMEFROMTHELEFT = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
		GOTOTHERIGHT = AnimationUtils.loadAnimation(this, R.anim.push_right_out);

		player.setImageBitmap(fromPlayer);

		player.startAnimation(GOTOTHERIGHT);
		player.setVisibility(ImageView.INVISIBLE);

		player2.startAnimation(COMEFROMTHELEFT);

		player2.setImageBitmap(toPlayer);

		player2.setVisibility(ImageView.VISIBLE);
	}

	private void changeImageToNextPlayer(Bitmap fromPlayer, Bitmap toPlayer)
	{
		COMEFROMTHELEFT = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
		GOTOTHERIGHT = AnimationUtils.loadAnimation(this, R.anim.push_left_out);

		player.setImageBitmap(fromPlayer);

		player.startAnimation(GOTOTHERIGHT);
		player.setVisibility(ImageView.INVISIBLE);

		player2.startAnimation(COMEFROMTHELEFT);

		player2.setImageBitmap(toPlayer);

		player2.setVisibility(ImageView.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_the_game, menu);
		return true;
	}

	@Override
	public void update(String event)
	{

		System.out.println("NOTIFIED :D");
		// GameTurn localTurn = theGame.getGameTurn();
		// localTurn.registerObserver(this);
		if (event.equals("SwipeRight"))
		{
			System.out.println("swipe right");

			Bitmap tmpBitmap = theGame.getCurrentPlayer().getOldPlayer().getPictureBitmap();
			localTurn.passToNextPlayer();

		} else if (event.equals("SwipeLeft"))
		{
		} else if (event.equals("GoLeft"))
			
		{
			
			Bitmap tmpBitmap = theGame.getCurrentPlayer().getOldPlayer().getPictureBitmap();
			// System.out.println(theGame.getGameTurn().getPlayerId()+"   it was my turn");
			theGame.passToPreviousPlayer();

			changeImageToPreviousPlayer(tmpBitmap, theGame.getCurrentPlayer().getOldPlayer().getPictureBitmap());
			localTurn = theGame.getGameTurn();
			// System.out.println(theGame.getGameTurn().getPlayerId()+"   ive just changede the local turn is this good?");
			// // <--- wrong
																										// right
			createPlayerScoreBoard();
		} else if (event.equals("passToRight"))
		{
			showDices(false);
            iVcub.clearAnimation();

			changeImageToNextPlayer(theGame.getPassedFrom().getOldPlayer().getPictureBitmap(), theGame.getCurrentPlayer().getOldPlayer().getPictureBitmap());
			localTurn = theGame.getGameTurn();
            /*

            this is what trigers the update of the scoreboard, the scorebord will make a nullpoint exception, when there is only 1 player left!


             */

			createPlayerScoreBoard();

		} else if (event.equals("SwipeDown"))
		{
			showDices(false);
			iVcub.startAnimation(DOWNCUB);
			System.out.println("swipe down");
		} else if (event.equals("SwipeUp"))
		{

			localTurn.liftCup(); // checks if dialog should be shown
			
		
		
		} else if (event.equals("showTheCub"))
		{
			int[] myCubValues = localTurn.whatDidIRoll();
			System.out.println("swipe up");
			setDiceImage(myCubValues);
			
			LIFTCUB.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation anim)
                {
                	showDices(true);
                };
                public void onAnimationRepeat(Animation anim)
                {
                };
                public void onAnimationEnd(Animation anim)
                {
                	showDices(false);
                };
            });     
			iVcub.startAnimation(LIFTCUB);
		} else if (event.equals("Shake"))
		{
			localTurn.shakeCup();
			iVcub.startAnimation(SHAKECUB);
			System.out.println("shake it baby");
			final Observer tempThis = this;
			SHAKECUB.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation anim)
                {
                	mSensorManager.unregisterListener(mySensorListener);
                	ourSounds.playDiceSound();

                };
                public void onAnimationRepeat(Animation anim)
                {
                };
                public void onAnimationEnd(Animation anim)
                {
                	mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            		mySensorListener = new dice.game.helpers.sensorManager(mSensorManager,tempThis).mSensorListener;
            		mSensorManager.registerListener(mySensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            	
                };
            });     


		}  else if(event.equals("gameOver")) {
            //Toast.makeText(this, "Vi har en vinder", Toast.LENGTH_LONG).show();

            this.finish();




            Intent launchWinScreen = new Intent(getBaseContext(), winScreen.class);


            Bundle b = new Bundle();

            b.putSerializable("Winner", theGame.getPlayer(0));

            launchWinScreen.putExtras(b);

            startActivity(launchWinScreen);




            /*

            ide, start ny activity
            finish game activity,

            det vil betyde at retun knappen vil sende en til gamesetup skærmen og man kan starte et nyt spil.


             */





        }
        else if (event.equals("askPlayerIfHeWasRight"))
		{
			createWhoWonMenu();

		} else if (event.equals("updatescoreboard"))
		{
			createPlayerScoreBoard();
			localTurn = theGame.getGameTurn();

		}

	}


	private void showDices(boolean xxx)
	{
		if (xxx)
		{
			System.out.println("showing dices");
			diceimage1.setVisibility(View.VISIBLE);
			diceimage2.setVisibility(View.VISIBLE);
		} else
		{
			System.out.println("hiding dices");
			diceimage1.setVisibility(View.INVISIBLE);
			diceimage2.setVisibility(View.INVISIBLE);
		}
	}

	private void setDiceImage(int[] dices)
	{
		int dice1 = dices[0];
		int dice2 = dices[1];
		/*
		 * mit hovede kan simpelthen ik tænke dynamisk:D
		 */
		switch (dice1)
		{
		case (1):
			diceimage1.setImageResource(R.drawable.dice1);
			break;
		case (2):
			diceimage1.setImageResource(R.drawable.dice2);
			break;
		case (3):
			diceimage1.setImageResource(R.drawable.dice3);
			break;
		case (4):
			diceimage1.setImageResource(R.drawable.dice4);
			break;
		case (5):
			diceimage1.setImageResource(R.drawable.dice5);
			break;
		case (6):
			diceimage1.setImageResource(R.drawable.dice6);
			break;
		}

		switch (dice2)
		{
		case (1):
			diceimage2.setImageResource(R.drawable.dice1);
			break;
		case (2):
			diceimage2.setImageResource(R.drawable.dice2);
			break;
		case (3):
			diceimage2.setImageResource(R.drawable.dice3);
			break;
		case (4):
			diceimage2.setImageResource(R.drawable.dice4);
			break;
		case (5):
			diceimage2.setImageResource(R.drawable.dice5);
			break;
		case (6):
			diceimage2.setImageResource(R.drawable.dice6);
			break;
		}
	}

}
