package fr.tvbarthel.games.chasewhisply.model;


import android.os.Handler;

public class TimeBasedRoutine extends Handler {
	private final long mReloadingTime;
	private final long mGameDuration;
	private long mTimeCounter;
	private final Runnable mRunnable;
	private boolean mIsRunning;
	private final ITimeBasedRoutine mInterface;

	public TimeBasedRoutine(long reloadingTime, long gameDuration, ITimeBasedRoutine iTimeBasedRoutine) {
		mReloadingTime = reloadingTime;
		mGameDuration = gameDuration;
		mTimeCounter = 0;
		mIsRunning = false;
		mInterface = iTimeBasedRoutine;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mTimeCounter += mReloadingTime;
				mInterface.reload();
				if (mTimeCounter < mGameDuration && mIsRunning) {
					postDelayed(this, mReloadingTime);
				} else if (mTimeCounter >= mGameDuration) {
					stopRoutine();
				}
			}
		};
	}

	public void setTimeCounter(long timeCounter) {
		mTimeCounter = timeCounter;
	}

	public void startRoutine() {
		if (!mIsRunning) {
			mIsRunning = true;
			postDelayed(mRunnable, mReloadingTime);
		}
	}

	public void stopRoutine() {
		mIsRunning = false;
		removeCallbacks(mRunnable);
		mInterface.stop();
	}

	public interface ITimeBasedRoutine {
		abstract public void reload();

		abstract public void stop();
	}

}
