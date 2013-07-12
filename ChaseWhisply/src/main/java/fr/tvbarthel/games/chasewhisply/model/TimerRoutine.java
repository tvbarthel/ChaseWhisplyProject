package fr.tvbarthel.games.chasewhisply.model;

import android.os.Handler;

abstract public class TimerRoutine extends Handler {
	protected Runnable mRunnable;
	protected boolean mIsRunning;
	protected long mTickingTime;

	/**
	 * Create a TimerRoutine
	 *
	 * @param tickingTime ticking time in millisecond
	 */
	protected TimerRoutine(long tickingTime) {
		mTickingTime = tickingTime;
		mIsRunning = false;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				doOnTick();
				if (mIsRunning) {
					postDelayed(this, mTickingTime);
				}
			}
		};
	}

	/**
	 * Start the TimerRoutine
	 */
	public void startRoutine() {
		if (!mIsRunning) {
			mIsRunning = true;
			postDelayed(mRunnable, mTickingTime);
		}
	}

	/**
	 * Stop the TimerRoutine
	 */
	public void stopRoutine() {
		mIsRunning = false;
		removeCallbacks(mRunnable);
	}

	/**
	 * If the TimerRoutine is running
	 * doOnTick is called every mTickingTime milliseconds
	 */
	abstract protected void doOnTick();
}
