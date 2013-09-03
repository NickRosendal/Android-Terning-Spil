package dice.game.android;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import dice.game.helpers.LockScreenRotation;
import dice.game.storage.OldPlayer;


public class GameSetup extends Activity {
    private LinkedList<OldPlayer> thePlayerList = new LinkedList<OldPlayer>();
    private TableLayout ourPlayerLayerout;
    public static int count; //skal bruges for at få unik silhouet

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        new LockScreenRotation(this);
        ourPlayerLayerout = (TableLayout) findViewById(R.id.tablelayout);
        ImageButton addNewPlayerButton = (ImageButton) findViewById(R.id.gotoAddPlayerIntentButton);
        ImageButton startButton = (ImageButton) findViewById(R.id.buttonStartGame);

        addNewPlayerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GameSetup.this, AddPlayer.class), 1);

            }
        });

        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //start game, but we need atleast 2 players!
                if (thePlayerList.size() >= 2) {
                    Intent startTheGame = new Intent(v.getContext(), dice.game.android.TheGame.class);
                    Bundle b = new Bundle();
                    b.putSerializable("PlayerList", thePlayerList);
                    startTheGame.putExtras(b);
                    System.out.println("we are sending " + thePlayerList.size() + " players over");
                    startActivity(startTheGame);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game_setup, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            OldPlayer thisGamePlayer = (OldPlayer) data.getExtras().get("thisGamePlayer");
            thePlayerList.add(thisGamePlayer);

            addPlayerToTable(thisGamePlayer);
            count++;

            // update vores list view ting kimmmm
        }

    }

    private void addPlayerToTable(final OldPlayer player) {
        final TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        //Bitmap myBitmap = BitmapFactory.decodeFile(imageLocation);

        final ImageView playerImage = new ImageView(this);



        playerImage.setImageBitmap(player.getPictureBitmap());

        int playerwidth = ourPlayerLayerout.getWidth();
        int femtedel = playerwidth / 5;
        int tofemtedele = femtedel*2;

        //størrelsen på vores billede.
        playerImage.setLayoutParams(new TableRow.LayoutParams(tofemtedele, ((tofemtedele/16)*9)));


        //størrelsen på knappern, 100 bred 100høj
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(femtedel, femtedel);




        final ImageButton deletePlayerButton = new ImageButton(this);
        deletePlayerButton.setImageResource(R.drawable.ic_menu_delete);
        deletePlayerButton.setBackgroundColor(0000);
        deletePlayerButton.setLayoutParams(layoutParams);

        final ImageButton movePlayerUpButton = new ImageButton(this);
        movePlayerUpButton.setImageResource(R.drawable.ic_menu_move_up);
        movePlayerUpButton.setBackgroundColor(0000);
        movePlayerUpButton.setLayoutParams(layoutParams);

        final ImageButton movePlayerDownButton = new ImageButton(this);
        movePlayerDownButton.setImageResource(R.drawable.ic_menu_move_down);
        movePlayerDownButton.setBackgroundColor(0000);
        movePlayerDownButton.setLayoutParams(layoutParams);

        tableRow.addView(deletePlayerButton);

        tableRow.addView(playerImage);

        tableRow.addView(movePlayerDownButton);
        tableRow.addView(movePlayerUpButton);


        deletePlayerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//              //remove from list
                thePlayerList.remove(player);
                //remove from tablelayout
                ourPlayerLayerout.removeView(tableRow);
            }
        });
        movePlayerUpButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int playerIndex = -1;

                for (int i = 0; i < thePlayerList.size(); i++) {

                    if (thePlayerList.get(i).equals(player)) {
                        playerIndex = i;

                        i = 200; //just setting i to more than size, to end the loop.
                    }

                }
                if (playerIndex == 0) {
                    //PLAYER IS FIRST IN THE LIST MOVE HIM TO THE END

                    OldPlayer temp = thePlayerList.pop();
                    thePlayerList.add(temp);

                    ourPlayerLayerout.removeView(tableRow);

                    ourPlayerLayerout.addView(tableRow);
                } else {
                    OldPlayer tempGoingUp = thePlayerList.get(playerIndex);

                    //we are moving the player above down.
                    thePlayerList.set(playerIndex, thePlayerList.get(playerIndex - 1));

                    //we are putting temp in on the space above
                    thePlayerList.set(playerIndex - 1, tempGoingUp);

                    //we need move the tablerows now! :o


                    View temp = ourPlayerLayerout.getChildAt(playerIndex);

                    ourPlayerLayerout.removeView(ourPlayerLayerout.getChildAt(playerIndex));

                    ourPlayerLayerout.addView(temp, playerIndex - 1);


                }

            }
        });
        movePlayerDownButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int playerIndex = -1;

                for (int i = 0; i < thePlayerList.size(); i++) {

                    if (thePlayerList.get(i).equals(player)) {
                        playerIndex = i;

                        i = 200; //just setting i to more than size, to end the loop.
                    }


                }
                if (playerIndex == thePlayerList.size() - 1) {
                    //should be last in list?
                    OldPlayer temp = thePlayerList.get(thePlayerList.size() - 1);
                    // O M F G
                    // need to move all!?!?!
                    thePlayerList.remove(playerIndex);
                    thePlayerList.addFirst(temp);

                    View tempV = ourPlayerLayerout.getChildAt(playerIndex);
//
                    ourPlayerLayerout.removeView(ourPlayerLayerout.getChildAt(playerIndex));
//
                    ourPlayerLayerout.addView(tempV, 0);

                } else {

                    OldPlayer tempGoingDown = thePlayerList.get(playerIndex);
//
//                        //we are moving the player above down.
                    thePlayerList.set(playerIndex, thePlayerList.get(playerIndex + 1));
//
//                        //we are putting temp in on the space above
                    thePlayerList.set(playerIndex + 1, tempGoingDown);
//
//                        //we need move the tablerows now! :o
//
//
                    View temp = ourPlayerLayerout.getChildAt(playerIndex);
//
                    ourPlayerLayerout.removeView(ourPlayerLayerout.getChildAt(playerIndex));
//
                    ourPlayerLayerout.addView(temp, playerIndex + 1);

                }
            }
        });

        ourPlayerLayerout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    }


}

