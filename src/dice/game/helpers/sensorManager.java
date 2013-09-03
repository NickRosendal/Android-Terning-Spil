package dice.game.helpers;

import java.util.ArrayList;

import dice.game.designpatterns.Observer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class sensorManager implements dice.game.designpatterns.Subject
{
	// private SensorManager mSensorManager;
	private boolean lock = false;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private ArrayList<Object> observers = new ArrayList<Object>();
    private int shakeCount =  0;

    public int numOfShakesNeeded = 2;
    public int sensorSensitivity = 10;

	public sensorManager(SensorManager mSensorManager, dice.game.designpatterns.Observer listener)
	{
		super();
System.out.println("shake listener added");
		this.mAccel = 0.00f;
		this.mAccelCurrent = mSensorManager.GRAVITY_EARTH;
		this.mAccelLast = mSensorManager.GRAVITY_EARTH;
		registerObserver(listener);

	}

	public final SensorEventListener mSensorListener = new SensorEventListener()
	{

		public void onSensorChanged(SensorEvent se)
		{

//			float x = se.values[0];
//			float y = se.values[1];
//			float z = se.values[2];
//			mAccelLast = mAccelCurrent;
//			mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
//			float delta = mAccelCurrent - mAccelLast;
//			mAccel = mAccel * 0.9f + delta; // perform low-cut filter
//
//			if (mAccel > sensorSensitivity && lock == false)
//			{
//				lock = true;
//				notifyObservers("Shake" + mAccel);
//
//				new Thread(new Runnable()
//				{
//					public void run()
//					{
//						try
//						{
//							Thread.sleep(10);
//						} catch (InterruptedException e)
//						{
//						}
//						lock = false;
//					}
//				}).start();
//			}
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter



            if (y > sensorSensitivity)
            {
                System.out.println("y: "+y+" x: "+x+" z: "+z);



               if(shakeCount == 0){
                   shakeCount++;
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(1000);
                            shakeCount = 0;
                        } catch (InterruptedException e)
                        {
                        }

                    }
                }).start();
            }else{
                   shakeCount++;
                   if(shakeCount >= numOfShakesNeeded){
                       shakeCount =0;
                       notifyObservers("Shake" + mAccel);

                   }
               }
            }
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}

	};

	public void registerObserver(dice.game.designpatterns.Observer o)
	{

		observers.add(o);
	}

	public void removeObserver(dice.game.designpatterns.Observer o)
	{
		observers.remove(o);
	}

	public void notifyObservers(String event)
	{

		for (int i = 0; i < observers.size(); i++)
		{
			((dice.game.designpatterns.Observer) observers.get(i)).update("Shake");

		}

	}

}
