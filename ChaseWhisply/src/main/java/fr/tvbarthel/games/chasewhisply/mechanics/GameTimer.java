package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Handler;

public class GameTimer extends Handler {

	private static final long CHRONOMETER_STEP_IN_MILLI = 100;
	private long mTimerStep = 1000;
	private final Runnable mRunnable;
	private long mCurrentTime;
	private final IGameTimer mIGameTimer;
	private boolean mIsRunning;
	private long mStartTime;

	/**
	 * timer type chronometer
	 *
	 * @param iGameTimer
	 */
	public GameTimer(IGameTimer iGameTimer) {
		mIGameTimer = iGameTimer;
		mIsRunning = false;
		mStartTime = -1;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (mStartTime == -1) mStartTime = System.currentTimeMillis();
				if (mIsRunning) {
					mCurrentTime = System.currentTimeMillis() - mStartTime;
					mIGameTimer.timerTick(mCurrentTime);
					postDelayed(this, CHRONOMETER_STEP_IN_MILLI);
				}

			}
		};
	}

	/**
	 * time type egg timer
	 *
	 * @param remainingLimit
	 * @param iGameTimer
	 */
	public GameTimer(long remainingLimit, IGameEggTimer iGameTimer) {
		mCurrentTime = remainingLimit;
		mIGameTimer = iGameTimer;
		mIsRunning = false;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mCurrentTime -= mTimerStep;
				mCurrentTime = Math.max(mCurrentTime, 0);
				if (mIsRunning && mCurrentTime > 0) {
					mIGameTimer.timerTick(mCurrentTime);
					postDelayed(this, mTimerStep);
				} else if (mCurrentTime == 0) {
					stopTimer();
					((IGameEggTimer) mIGameTimer).timerEnd();
				}
			}
		};
	}

	public GameTimer(long remainingLimit, IGameEggTimer iGameTimer, long timerStepInMilli) {
		this(remainingLimit, iGameTimer);
		mTimerStep = timerStepInMilli;
	}

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


	public interface IGameTimer {
		abstract void timerTick(long currentTime);
	}

	public interface IGameEggTimer extends IGameTimer {
		abstract void timerEnd();
	}
}
