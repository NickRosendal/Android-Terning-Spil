package dice.game.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import dice.game.helpers.LockScreenRotation;
import dice.game.logic.GamePlayer;
import dice.game.storage.OldPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: nickrosendal
 * Date: 2/9/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class winScreen extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_screen);
        new LockScreenRotation(this);
//

        Bundle b = this.getIntent().getExtras();
        GamePlayer theWinner = (GamePlayer)b.getSerializable("Winner");
//
        ImageView theWinnerImage = (ImageView) findViewById(R.id.winnerImage);
//
        theWinnerImage.setImageBitmap(theWinner.getOldPlayer().getPictureBitmap());
//
        Button theEndButton = (Button) findViewById(R.id.backToGameSetupButton);
        theEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endThis();
            }
        });
    }
    private void endThis(){
        this.finish();
    }
}