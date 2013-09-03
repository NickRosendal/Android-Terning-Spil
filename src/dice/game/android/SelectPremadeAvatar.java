package dice.game.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import dice.game.helpers.LockScreenRotation;
public class SelectPremadeAvatar extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_premade_avatar);
        new LockScreenRotation(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_premade_avatar, menu);
        return true;
    }
}
