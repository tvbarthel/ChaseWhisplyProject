package fr.tvbarthel.games.chasewhisply.mechanics.timer;

import android.os.Handler;

public abstract class BaseTimer extends Handler {

	protected long mTimerStep;
	protected Runnable mRunnable;
	protected long mCurrentTime;
	protected boolean mIsRunning;

	public BaseTimer() {
		mIsRunning = false;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (mIsRunning) tick();
			}
		};
	}

	protected abstract void tick();


	public boolean startTimer() {
		if (mIsRunning) {
			return false;
		}
		postDelayed(mRunnable, mTimerStep);
		mIsRunning = true;
		return true;
	}

	public long stopTimer() {
		if (mIsRunning) {
			removeCallbacks(mRunnable);
			mIsRunning = false;
		}
		return mCurrentTime;
	}

	/**
	 * add additional time
	 *
	 * @param t time in millisecond
	 */
	public void addTime(long t) {
		if (mIsRunning && t > 0) {
			mCurrentTime += t;
		}
	}

}
