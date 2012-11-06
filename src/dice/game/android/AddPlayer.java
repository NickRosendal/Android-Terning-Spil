package dice.game.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dice.game.android.R;

import dice.game.helpers.CropOption;
import dice.game.helpers.CropOptionAdapter;

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
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AddPlayer extends Activity
{
private Bitmap photo ;
	ImageButton gameArchiveButton;
	ImageButton phoneArchiveButton;
	ImageButton goToCamaraButton;
	ImageButton doneButton;
	ImageView playerImage;
	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_player);
		playerImage = (ImageView) findViewById(R.id.imageView1);
		goToCamaraButton = (ImageButton) findViewById(R.id.ib_camara);
		phoneArchiveButton = (ImageButton) findViewById(R.id.ib_phoneArchive);
		gameArchiveButton = (ImageButton) findViewById(R.id.ib_gameArchive);
		doneButton = (ImageButton) findViewById(R.id.ib_doneButton);
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// gets a silhouett
		// playerImage.setImageBitmap(getSilhouette());

		// get a list of previusly used images..
		// playerImage set as the chocen image
		// gameArchiveButton.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		// get a list of phone images
		// playerImage set as the chocen image
		// add the chosen image to the game archive data location..

		phoneArchiveButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

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
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				mImageCaptureUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), "tmp_avatar_"
						+ String.valueOf(System.currentTimeMillis()) + ".jpg"));

				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mImageCaptureUri);

				try
				{
					intent.putExtra("return-data", true);

					startActivityForResult(intent, PICK_FROM_CAMERA);
				} catch (ActivityNotFoundException e)
				{
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode)
		{
		case PICK_FROM_CAMERA:
			doCrop();
			System.out.println("calling do crop from camera");

			break;

		case PICK_FROM_FILE:
			mImageCaptureUri = data.getData();

			doCrop();

			break;

		case CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();

			if (extras != null)
			{
				 photo = extras.getParcelable("data");
				playerImage.setImageBitmap(photo);

				// mImageView.setImageBitmap(photo);
			}

			File f = new File(mImageCaptureUri.getPath());

			 if (f.exists()) f.delete();
		    File direct = new File(Environment.getExternalStorageDirectory() + "/MIAgamePhotos");
		    

		    if(!direct.exists())
		     {
		    	direct.mkdir();
		     }
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

			//you can create a new file name "test.jpg" in sdcard folder.
			File f2 = new File(Environment.getExternalStorageDirectory()
			                        + File.separator + "/MIAgamePhotos/Avatar"+String.valueOf(System.currentTimeMillis())+".jpg");
			try
			{
				f2.createNewFile();
				FileOutputStream fo = new FileOutputStream(f2);
				fo.write(bytes.toByteArray());
				fo.close();
				bytes.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//write the bytes in file
		
//			
//			
//			File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Images/" + "image_name" + ".jpg");
//			file.
//			Uri imageUri = Uri.fromFile(file);
//
//			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//			startActivityForResult(intent, 0);
						  
//			
//		    File direct = new File(Environment.getExternalStorageDirectory() + "/MIAgamePhotos");
//		    
//
//		    if(!direct.exists())
//		     {
//		         direct.mkdir();
//
//		     }
//		    
//			String FILENAME = ""+System.currentTimeMillis();
//			
//			FileOutputStream fos;
//			try
//			{
//				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//				photo.compress(Bitmap.CompressFormat.PNG, 90, fos);
//				fos.flush();
//				fos.close();
//			} catch (FileNotFoundException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			
			
		    
		    
		    
		   
			
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_add_player, menu);
		return true;
	}

	private Bitmap getSilhouette()
	{

		// should tjeck in a list what colors have been used, and asign the
		// player a random color, among the colors not used.
		return null;

	}

	private void doCrop()
	{
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();

		if (size == 0)
		{
			Toast.makeText(this, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();

			return;
		} else
		{
			intent.setData(mImageCaptureUri);

			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1)
			{
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, CROP_FROM_CAMERA);
			} else
			{
				for (ResolveInfo res : list)
				{
					final CropOption co = new CropOption();

					co.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(
						getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int item)
							{
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_FROM_CAMERA);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener()
				{
					public void onCancel(DialogInterface dialog)
					{

						if (mImageCaptureUri != null)
						{
							getContentResolver().delete(mImageCaptureUri, null,
									null);
							mImageCaptureUri = null;
						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}

}