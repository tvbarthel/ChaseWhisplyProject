package fr.tvbarthel.games.chasewhisply.mechanics.routine;


public class RoutineTicker extends Routine {

    /**
     * Create a Routine
     *
     * @param tickingTime ticking time in millisecond
     */
    public RoutineTicker(long tickingTime) {
        super(Routine.TYPE_TICKER, tickingTime);
    }

    public RoutineTicker(long tickingTime, IRoutine iRoutine) {
        super(Routine.TYPE_TICKER, tickingTime, iRoutine);
    }

    @Override
    protected void run() {
        mIRoutine.onRun(getType(), mTickingTime);
    }

}
