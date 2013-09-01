package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.model.GameInformation;

public class SurvivalGameEngine extends TimeLimitedGameEngine {

	protected long mAdditionalTimeOnKill = 0;

	/**
	 * engine for survival game
	 *
	 * @param iGameEngine
	 * @param gameInformation from factory
	 * @param t               additional time in milliseconds when target killed
	 */
	public SurvivalGameEngine(IGameEngine iGameEngine, GameInformation gameInformation, long t) {
		super(iGameEngine, gameInformation);
		if (t > 0) {
			mAdditionalTimeOnKill = t;
		}
	}

	@Override
	protected void onKill() {
		super.onKill();
		mEggTimer.addTime(mAdditionalTimeOnKill);
	}
}
