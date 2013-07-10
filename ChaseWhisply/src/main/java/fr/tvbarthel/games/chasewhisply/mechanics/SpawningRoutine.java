package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Handler;

public class SpawningRoutine extends Handler {
	private final long mSpawningTime;
	private final Runnable mRunnable;
	private boolean mIsRunning;
	private final ISpawningRoutine mInterface;

	/**
	 * Create a spawningRoutine
	 *
	 * @param spawningTime     spawning time in millisecond
	 * @param iSpawningRoutine interface
	 */
	public SpawningRoutine(long spawningTime, ISpawningRoutine iSpawningRoutine) {
		mSpawningTime = spawningTime;
		mIsRunning = false;
		mInterface = iSpawningRoutine;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mInterface.spawn();
				if (mIsRunning) {
					postDelayed(this, mSpawningTime);
				}
			}
		};
	}

	public void startRoutine() {
		if (!mIsRunning) {
			mIsRunning = true;
			postDelayed(mRunnable, mSpawningTime);
		}
	}

	public void stopRoutine() {
		mIsRunning = false;
		removeCallbacks(mRunnable);
	}

	public interface ISpawningRoutine {
		abstract public void spawn();
	}

}