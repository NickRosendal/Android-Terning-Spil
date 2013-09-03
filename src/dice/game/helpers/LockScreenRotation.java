package dice.game.helpers;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;

public class LockScreenRotation {
	
	public LockScreenRotation(Activity inActivity){

    	
    	DisplayMetrics metrics = inActivity.getResources().getDisplayMetrics();
    	int width = metrics.widthPixels;
    	int height = metrics.heightPixels;
    	System.out.println(width + " x " + height);
    	    	
    	if(width > height){
    		 System.out.println("det er en tablet");
    		 inActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	}
    	else{
    		System.out.println("det er ikke en tablet ");
    		inActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	}
    }
  

}
