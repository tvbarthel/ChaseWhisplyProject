package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Handler;

public class GameTimer extends Handler {
	private final Runnable mRunnable;
	private long mTimeLimit;
	private final IGameTimer mIGameTimer;
	private boolean mIsRunning;
	private long mStartTime;

	public GameTimer(long timeLimit, IGameTimer iGameTimer) {
		mTimeLimit = timeLimit;
		mIGameTimer = iGameTimer;
		mIsRunning = false;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mIsRunning = false;
				mIGameTimer.timerEnd();
			}
		};
	}

	public boolean startTimer() {
		if (mIsRunning) {
			return false;
		}
		postDelayed(mRunnable, mTimeLimit);
		mIsRunning = true;
		mStartTime = System.currentTimeMillis();
		return true;
	}

	public long stopTimer() {
		long remainingTime = 0;
		if (mIsRunning) {
			removeCallbacks(mRunnable);
			mIsRunning = false;
			remainingTime = mTimeLimit - (System.currentTimeMillis() - mStartTime);
			mTimeLimit = remainingTime;
		}
		return remainingTime;
	}


	public interface IGameTimer {
		abstract void timerEnd();

		abstract int timerAbort();
	}
}
