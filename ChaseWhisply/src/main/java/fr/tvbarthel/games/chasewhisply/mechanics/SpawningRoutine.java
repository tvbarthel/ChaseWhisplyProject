package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.model.TimerRoutine;

public class SpawningRoutine extends TimerRoutine {
	private final ISpawningRoutine mInterface;

	/**
	 * Create a spawningRoutine
	 *
	 * @param spawningTime     spawning time in millisecond
	 * @param iSpawningRoutine interface
	 */
	public SpawningRoutine(long spawningTime, ISpawningRoutine iSpawningRoutine) {
		super(spawningTime);
		mInterface = iSpawningRoutine;
	}

	@Override
	protected void doOnTick() {
		mInterface.spawn();
	}

	public interface ISpawningRoutine {
		abstract public void spawn();
	}

}