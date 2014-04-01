package fr.tvbarthel.games.chasewhisply.mechanics.routine;

import android.os.Handler;

public class Routine extends Handler {
    public static final int TYPE_TICKER = 0x00000001;
    public static final int TYPE_RELOADER = 0x00000002;
    public static final int TYPE_SPAWNER = 0x00000003;

    final protected long mTickingTime;
    final protected Runnable mRunnable;
    final protected int mType;
    protected boolean mIsRunning;
    protected IRoutine mIRoutine;


    /**
     * Create a Routine
     *
     * @param tickingTime ticking time in millisecond
     */
    public Routine(int type, long tickingTime) {
        mType = type;
        mTickingTime = tickingTime;
        mIsRunning = false;
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Routine.this.run();
                if (mIsRunning) {
                    postDelayed(this, mTickingTime);
                }
            }
        };
    }

    /**
     * Create a Routine
     *
     * @param tickingTime ticking time in millisecond
     */
    public Routine(int type, long tickingTime, IRoutine iRoutine) {
        this(type, tickingTime);
        mIRoutine = iRoutine;
    }

    public void setIRoutine(IRoutine iRoutine) {
        mIRoutine = iRoutine;
    }

    /**
     * Start the Routine
     */
    public void startRoutine() {
        if (!mIsRunning) {
            mIsRunning = true;
            postDelayed(mRunnable, mTickingTime);
        }
    }

    /**
     * Stop the Routine
     */
    public void stopRoutine() {
        mIsRunning = false;
        removeCallbacks(mRunnable);
    }

    public int getType() {
        return mType;
    }


    protected void run() {
        if (mIRoutine != null) {
            mIRoutine.onRun(getType(), null);
        }
    }

    public interface IRoutine {
        public void onRun(int routineType, Object obj);
    }
}
