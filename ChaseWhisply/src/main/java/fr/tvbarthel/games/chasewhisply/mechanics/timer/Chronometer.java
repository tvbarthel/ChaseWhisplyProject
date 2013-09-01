package fr.tvbarthel.games.chasewhisply.mechanics.timer;

public class Chronometer extends BaseTimer {

	private static final long DEFAULT_STEP_IN_MILLI = 100;
	private IChronometer mIChronometer;
	private long mStartTime;

	/**
	 * timer type chronometer
	 *
	 * @param iChronometer
	 */
	public Chronometer(IChronometer iChronometer) {
		super();
		mTimerStep = DEFAULT_STEP_IN_MILLI;
		mIChronometer = iChronometer;
		mStartTime = -1;
	}

	@Override
	protected void tick() {
		if (mStartTime == -1) mStartTime = System.currentTimeMillis();
		mCurrentTime = System.currentTimeMillis() - mStartTime;
		mIChronometer.timerTick(mCurrentTime);
		postDelayed(mRunnable, mTimerStep);
	}

	public void setStartTime(long startTime) {
		if (startTime > 0) mStartTime = startTime;
	}

	public long getStartTime() {
		return mStartTime;
	}

	public interface IChronometer {
		abstract void timerTick(long currentTime);
	}
}
