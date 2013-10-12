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
		int step = gameInformationTutorial.getCurrentStep();
		switch (step) {
			case GameInformationTutorial.STEP_WELCOME:
				gameInformationTutorial.setCurrentStep(GameInformationTutorial.STEP_UI_WELCOME);
				break;
			case GameInformationTutorial.STEP_UI_WELCOME:
				gameInformationTutorial.setCurrentStep(GameInformationTutorial.STEP_CROSSHAIR);
				break;
			case GameInformationTutorial.STEP_CROSSHAIR:
				gameInformationTutorial.setCurrentStep(GameInformationTutorial.STEP_AMMO);
				break;
			case GameInformationTutorial.STEP_AMMO:
				gameInformationTutorial.setCurrentStep(GameInformationTutorial.STEP_COMBO);
				break;
			case GameInformationTutorial.STEP_COMBO:
				gameInformationTutorial.setCurrentStep(GameInformationTutorial.STEP_SERIOUS_THINGS);
				break;
			case GameInformationTutorial.STEP_SERIOUS_THINGS:
				gameInformationTutorial.setCurrentStep(GameInformationTutorial.STEP_END);
				break;
			case GameInformationTutorial.STEP_END:
				stopGame();
				break;
		}
		mGameSoundManager.playGhostLaugh();
	}
}
