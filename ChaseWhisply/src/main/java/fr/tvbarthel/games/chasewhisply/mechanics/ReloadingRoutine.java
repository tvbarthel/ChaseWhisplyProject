package fr.tvbarthel.games.chasewhisply.mechanics;


import android.os.Handler;

public class ReloadingRoutine extends Handler {
	private final long mReloadingTime;
	private final Runnable mRunnable;
	private boolean mIsRunning;
	private final IReloadingRoutine mInterface;

	/**
	 * Create a ReloadingRoutine
	 *
	 * @param reloadingTime     in millisecond
	 * @param iTimeBasedRoutine interface
	 */
	public ReloadingRoutine(long reloadingTime, IReloadingRoutine iTimeBasedRoutine) {
		mReloadingTime = reloadingTime;
		mIsRunning = false;
		mInterface = iTimeBasedRoutine;
		mRunnable = new Runnable() {
			@Override
			public void run() {
				mInterface.reload();
				if (mIsRunning) {
					postDelayed(this, mReloadingTime);
				}
			}
		};
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
	}

	public interface IReloadingRoutine {
		abstract public void reload();
	}

}
