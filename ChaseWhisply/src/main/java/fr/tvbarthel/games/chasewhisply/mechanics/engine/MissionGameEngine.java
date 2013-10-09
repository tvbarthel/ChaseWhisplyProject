package fr.tvbarthel.games.chasewhisply.mechanics.engine;

import android.content.Context;

import fr.tvbarthel.games.chasewhisply.model.GameBehavior;

/**
 * Created by tbarthel on 09/10/13.
 */
public class MissionGameEngine extends StandardGameEngine {
	/**
	 * Mission GameEngine
	 * fire on touch
	 * sounds
	 * works with timer
	 * works with a specific goal
	 *
	 * @param context
	 * @param iGameEngine
	 * @param gameBehavior
	 */
	public MissionGameEngine(Context context, IGameEngine iGameEngine, GameBehavior gameBehavior) {
		super(context, iGameEngine, gameBehavior);
	}

	@Override
	public void onScreenTouch() {
		super.onScreenTouch();
		checkIfCompleted();
	}

	/**
	 * Check is the mission is completed
	 * if completed, stop the game
	 */
	private void checkIfCompleted() {
		if (mGameBehavior.isCompleted()) {
			stopGame();
		}
	}
}
