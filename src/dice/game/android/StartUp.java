package dice.game.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Dialog;
import android.os.AsyncTask;
import dice.game.helpers.LockScreenRotation;
import dice.game.logic.GamePlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

@SuppressLint(
        {"NewApi"})
public class StartUp extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);



    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        runBootScreenAnimation();
    }


    private void bootUpProcess() {

        new LockScreenRotation(this);
        File direct = new File(Environment.getExternalStorageDirectory() + "/MIAgamePhotos/Premade");
        if (!direct.exists()) {
            direct.mkdir();
        }
    }

    private void runBootScreenAnimation() {

        //text fade in
        TextView bottomText = (TextView) findViewById(R.id.introText);
        //bottomText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        bottomText.setTextSize(25);
        Animation FADEIN = AnimationUtils.loadAnimation(this, R.drawable.fadein);
        FADEIN.setDuration(2000);
        bottomText.startAnimation(FADEIN);

        //cub shake
        Animation SHAKECUB = AnimationUtils.loadAnimation(this, R.drawable.shake);
        ImageView cub = (ImageView) findViewById(R.id.image_start_up_cup);
        SHAKECUB.setDuration(600);
        SHAKECUB.setStartOffset(3000);
        SHAKECUB.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation anim) {
                bootUpProcess();
            }

            public void onAnimationRepeat(Animation anim) {
            }

            public void onAnimationEnd(Animation anim) {
                startNextActivity();
            }
        });
        cub.startAnimation(SHAKECUB);
    }

    private void startNextActivity() {
        finish();
        Intent intent = new Intent(StartUp.this, GameSetup.class);
        StartUp.this.startActivity(intent);
    }
}
