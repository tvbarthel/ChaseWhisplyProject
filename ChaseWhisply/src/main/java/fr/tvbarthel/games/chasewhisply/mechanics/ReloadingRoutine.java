package fr.tvbarthel.games.chasewhisply.mechanics;


import fr.tvbarthel.games.chasewhisply.model.TimerRoutine;

public class ReloadingRoutine extends TimerRoutine {
	private final IReloadingRoutine mInterface;

	/**
	 * Create a ReloadingRoutine
	 *
	 * @param reloadingTime     in millisecond
	 * @param iTimeBasedRoutine interface
	 */
	public ReloadingRoutine(long reloadingTime, IReloadingRoutine iTimeBasedRoutine) {
		super(reloadingTime);
		mInterface = iTimeBasedRoutine;
	}

	@Override
	protected void doOnTick() {
		mInterface.reload();
	}

	public interface IReloadingRoutine {
		abstract public void reload();
	}

}
