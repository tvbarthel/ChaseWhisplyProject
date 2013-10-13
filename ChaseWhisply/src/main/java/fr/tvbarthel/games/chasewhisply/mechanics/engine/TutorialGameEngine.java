package fr.tvbarthel.games.chasewhisply.mechanics.engine;

import android.content.Context;

import fr.tvbarthel.games.chasewhisply.model.GameBehavior;
import fr.tvbarthel.games.chasewhisply.model.GameInformationTutorial;


public class TutorialGameEngine extends GameEngine {

	/**
	 * Create GameEngine for the tutorial
	 *
	 * @param context
	 * @param iGameEngine
	 * @param gameBehavior
	 */
	public TutorialGameEngine(Context context, IGameEngine iGameEngine, GameBehavior gameBehavior) {
		super(context, iGameEngine, gameBehavior);
	}

	@Override
	public void onScreenTouch() {
		final GameInformationTutorial gameInformationTutorial =
				(GameInformationTutorial) mGameInformation;
		int currentStep = gameInformationTutorial.nextStep();
		if(currentStep == GameInformationTutorial.STEP_END) {
			stopGame();
		}
		if(currentStep == GameInformationTutorial.STEP_AMMO_2) {
			mGameInformation.getWeapon().setCurrentAmmunition(0);
		}
		mGameSoundManager.playGhostLaugh();
	}
}
