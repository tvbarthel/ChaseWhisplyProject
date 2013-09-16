package fr.tvbarthel.games.chasewhisply.mechanics.routine;


public class TickerRoutine extends TimerRoutine {

	private ITickerRoutine mInterface;

	public TickerRoutine(long tickingTime, ITickerRoutine i) {
		super(tickingTime);
		mInterface = i;
	}

	@Override
	protected void doOnTick() {
		mInterface.onTick(mTickingTime);
	}

	public interface ITickerRoutine {
		public void onTick(long tickingTime);
	}
}
