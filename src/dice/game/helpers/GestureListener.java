package dice.game.helpers;

import java.util.ArrayList;

import dice.game.designpatterns.Observer;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GestureListener implements OnTouchListener, dice.game.designpatterns.Subject
{

	private final GestureDetector gestureDetector = new GestureDetector(new GestureListenerINNERCLASS());
	private ArrayList<Object> observers = new ArrayList<Object>();

	public boolean onTouch(final View v, final MotionEvent event)
	{
		// super.onTouch(view, motionEvent);
		return gestureDetector.onTouchEvent(event);
	}

	public void registerObserver(dice.game.designpatterns.Observer o)
	{

		observers.add(o);
		System.out.println("i registred and observer!!!!!, gesturelistener");
	}

	public void removeObserver(dice.game.designpatterns.Observer o)
	{
		observers.remove(o);
	}

	public void notifyObservers(String event)
	{
		System.out.println("am gonna notifi the hell out of em, gesturelistener " + observers.size());

		for (int i = 0; i < observers.size(); i++)
		{
			((dice.game.designpatterns.Observer) observers.get(i)).update(event);

		}

	}

	private final class GestureListenerINNERCLASS extends SimpleOnGestureListener
	{

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e)
		{
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			System.out.println("on fling, gesturelistener");
			boolean result = false;
			try
			{
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY))
				{
					if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
					{
						if (diffX > 0)
						{
							notifyObservers("SwipeRight");

						} else
						{

							notifyObservers("SwipeLeft");
						}
					}
				} else
				{
					if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
					{
						if (diffY > 0)
						{

							notifyObservers("SwipeDown");
						} else
						{

							notifyObservers("SwipeUp");
						}
					}
				}
			} catch (Exception exception)
			{
				exception.printStackTrace();
			}
			return result;
		}

	}
}
