package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Handler;

public class GameTimer extends Handler {
	private final Runnable mRunnable;
	private long mRemainingTime;
	private final IGameTimer mIGameTimer;
	private boolean mIsRunning;
	private long mStartTime;

	public GameTimer(long remainingLimit, IGameTimer iGameTimer) {
		mRemainingTime = remainingLimit;
		mIGameTimer = iGameTimer;
		mIsRunning = false;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mRemainingTime -= 1000;
				mRemainingTime = Math.max(mRemainingTime, 0);
				if (mIsRunning && mRemainingTime > 0) {
					mIGameTimer.timerTick(mRemainingTime);
					postDelayed(this, 1000);
				} else if (mRemainingTime == 0) {
					stopTimer();
					mIGameTimer.timerEnd();
				}
			}
		};
	}

	public boolean startTimer() {
		if (mIsRunning) {
			return false;
		}
		postDelayed(mRunnable, 1000);
		mIsRunning = true;
		mStartTime = System.currentTimeMillis();
		return true;
	}

	public long stopTimer() {
		if (mIsRunning) {
			removeCallbacks(mRunnable);
			mIsRunning = false;
		}
		return mRemainingTime;
	}

	/**
	 * add additional time
	 *
	 * @param t time in millisecond
	 */
	public void addTime(long t) {
		if (mIsRunning && t > 0) {
			mRemainingTime += t;
		}
	}


	public interface IGameTimer {
		abstract void timerEnd();

		abstract void timerTick(long remainingTime);
	}
}
