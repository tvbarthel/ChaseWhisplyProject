package fr.tvbarthel.games.chasewhisply.mechanics.timer;

public class EggTimer extends BaseTimer {

	private static final long DEFAULT_STEP_IN_MILLI = 1000;
	private IEggTimer mIEggTimer;

	public EggTimer(long remainingLimit, IEggTimer iEggTimer) {
		super();
		mTimerStep = DEFAULT_STEP_IN_MILLI;
		mCurrentTime = remainingLimit;
		mIEggTimer = iEggTimer;

	}

	@Override
	protected void tick() {
		mCurrentTime -= mTimerStep;
		mCurrentTime = Math.max(mCurrentTime, 0);
		if (mCurrentTime > 0) {
			mIEggTimer.timerTick(mCurrentTime);
			postDelayed(mRunnable, mTimerStep);
		} else {
			stopTimer();
			mIEggTimer.timerEnd();
		}
	}

	public interface IEggTimer {

		abstract void timerTick(long currentTimer);

		abstract void timerEnd();
	}
}
