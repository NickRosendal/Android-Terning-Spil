package dice.game.helpers;


import dice.game.android.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager
{
	private SoundPool MySoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
	private int diceSound;

	public SoundManager(Context appContext){
		loadsounds (appContext);
	}
	private void loadsounds(Context appContext)
	{
		// this class neeeds to be loaded when the app launches, so that sounds
		// are ready to shoooot
		diceSound = MySoundPool.load(appContext, R.raw.dicesound, 1);

	}

	public void playDiceSound()
	{
		MySoundPool.play(diceSound, 100, 100, 1, 0, 1f);

	}
}
