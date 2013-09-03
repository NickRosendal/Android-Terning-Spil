package dice.game.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dice.game.helpers.LockScreenRotation;

import dice.game.android.R;

import dice.game.helpers.CropOption;
import dice.game.helpers.CropOptionAdapter;
import dice.game.logic.GamePlayer;
import dice.game.storage.OldPlayer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AddPlayer extends Activity
{
	private Bitmap photo;
	ImageButton gameArchiveButton;
	ImageButton phoneArchiveButton;
	ImageButton goToCamaraButton;
	ImageButton doneButton;
	ImageView playerImage;

	private HashMap<Integer, Boolean> playerImageTemplets;
	private ImageView mImageView;
	private dice.game.storage.OldPlayer thisPlayer;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int CROP_DONE = 4;
	private static final int SELECT_PICTURE = 5;
	private String photoLocation;
	Uri tempFilePath;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_player);
		new LockScreenRotation(this);
		playerImage = (ImageView) findViewById(R.id.imageView1);
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		playerImage.setMinimumHeight((width/16)* 9);
		goToCamaraButton = (ImageButton) findViewById(R.id.ib_camara);
		phoneArchiveButton = (ImageButton) findViewById(R.id.ib_phoneArchive);
		doneButton = (ImageButton) findViewById(R.id.ib_doneButton);

		// int width = 400;
		// playerImage.setLayoutParams(new LayoutParams(width,width));
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// gets a silhouett
		playerImage.setImageBitmap(getSilhouette());

		// get a list of previusly used images..
		// playerImage set as the chocen image


		// get a list of phone images
		// playerImage set as the chocen image
		// add the chosen image to the game archive data location..
		doneButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				thisPlayer = new dice.game.storage.OldPlayer();
				// substing to remove file://
				thisPlayer.setPictureLocation(photoLocation);

				// dice.game.logic.GamePlayer thisGamePlayer = new GamePlayer(
				// thisPlayer);

				// Intent i = new Intent(this, StartUp.class);
				// Bundle b = new Bundle();
				// b.putParcelable("aGamePlayer", thisGamePlayer);
				// i.putExtras(b);
				// //i.setClass(this, StartUp.class);
				// startActivity(i);

				Intent returnIntent = new Intent();
				returnIntent.putExtra("thisGamePlayer", thisPlayer);
				setResult(RESULT_OK, returnIntent);
				finish();

			}
		});
		phoneArchiveButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

			}
		});

		// should launch camara intent
		// and return the picture
		// set playerImage to the picture
		// the picture, should be stored in game archive data location..

		goToCamaraButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + "jens.jpg");
				tempFilePath = Uri.fromFile(f2);

				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFilePath);

				startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK)
		{
			System.out.println("resultCode" + resultCode);
			return;
		}
		switch (requestCode)
		{
		case PICK_FROM_CAMERA:
			if (resultCode == RESULT_OK)
			{

				System.out.println("calling do crop from camera");

				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setType("image/*");

				List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
				// opret billed fil her

				intent.setData(tempFilePath);

				// billede her
				intent.putExtra("outputX", 400);
				intent.putExtra("outputY", 225);
				intent.putExtra("aspectX", 16);
				intent.putExtra("aspectY", 9);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);

				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
				startActivityForResult(i, CROP_DONE);
			}
			break;
		case CROP_DONE:
			photo = (Bitmap) data.getExtras().get("data");
			playerImage.setImageBitmap(photo);
			photoLocation = createFileOnDisk(photo).toString().substring(7);
			System.out.println("photoLocation: " + photoLocation);
			break;

		case SELECT_PICTURE:
			Uri selectedImageUri = data.getData();
			String  selectedImagePath = getPath(selectedImageUri);
			Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			
			if (height/9 == width/16 ){
			playerImage.setImageBitmap(bitmap);
			photoLocation = selectedImagePath;
			}
			
				else { //pick from file
					System.out.println("calling do crop from camera");

					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setType("image/*");

					List<ResolveInfo> list = getPackageManager()
							.queryIntentActivities(intent, 0);
					// opret billed fil her

					intent.setData(selectedImageUri);

					// billede her
					intent.putExtra("outputX", 400);
					intent.putExtra("outputY", 225);
					intent.putExtra("aspectX", 16);
					intent.putExtra("aspectY", 9);
					intent.putExtra("scale", true);
					intent.putExtra("return-data", true);

					Intent i = new Intent(intent);
					ResolveInfo res = list.get(0);

					i.setComponent(new ComponentName(res.activityInfo.packageName,
							res.activityInfo.name));
					startActivityForResult(i, CROP_DONE);
				       }
			break;
		}

	}

	// Gets the path from the Uri given by data.getdata()
	// copied from:
	// http://stackoverflow.com/questions/2169649/open-an-image-in-androids-built-in-gallery-app-programmatically
	public String getPath(Uri uri)
	{
		String[] projection =
		{ MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private Uri createFileOnDisk(Bitmap inBitmap)
	{

		// just check ifwe have a folder on the sdcard if not it creates!
		File direct = new File(Environment.getExternalStorageDirectory() + "/MIAgamePhotos");

		if (!direct.exists())
		{
			direct.mkdir();
		}
		// starting the process ofsaving the file
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

		// you can create a new file name "test.jpg" in sdcard folder.

		String pathOfPhoto = "/MIAgamePhotos/Avatar" + String.valueOf(System.currentTimeMillis()) + ".jpg";

		File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + pathOfPhoto);
		try
		{
			f2.createNewFile();
			FileOutputStream fo = new FileOutputStream(f2);
			fo.write(bytes.toByteArray());
			fo.close();
			bytes.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return Uri.fromFile(f2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_add_player, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{

	}

	private Bitmap getSilhouette()
	{

		int mycase = GameSetup.count;
		if (mycase > 6)
		{
			mycase = mycase - 7;
		}
		Bitmap sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_pink);

		switch (mycase)
		{
		case 0:
			//pic remains pink
			break;
		case 1:
			sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_blue);
			break;
		case 2:
			sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_brown);
			break;
		case 3:
			sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_darkblue);
			break;
		case 4:
			sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_green);
			break;
		case 5:
			sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_red);
			break;
		case 6:
			sil = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_yellow);
			break;
		}

		photoLocation = createFileOnDisk(sil).toString().substring(7);

		return sil;

	}

}