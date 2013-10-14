package fr.tvbarthel.games.chasewhisply.mechanics.engine;

import android.content.Context;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameBehavior;
import fr.tvbarthel.games.chasewhisply.model.GameInformationTutorial;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;


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

		final int currentStep = gameInformationTutorial.getCurrentStep();

		if (currentStep == GameInformationTutorial.STEP_KILL || currentStep == GameInformationTutorial.STEP_KILL_2) {
			fire();
		} else {
			final int nextStep = gameInformationTutorial.nextStep();

			if (nextStep == GameInformationTutorial.STEP_END) {
				mGameInformation.earnExp(8);
				stopGame();
			}

			if (nextStep == GameInformationTutorial.STEP_AMMO_2) {
				mGameInformation.getWeapon().setCurrentAmmunition(0);
			}

			if (nextStep == GameInformationTutorial.STEP_TARGET || nextStep == GameInformationTutorial.STEP_TARGET_2) {
				mGameBehavior.onTouchScreen();
			}
		}
	}

	/**
	 * call when use touch screen
	 * create bullet hole or hit current target
	 */
	public void fire() {
		final int dmg = mGameInformation.getWeapon().fire();
		final TargetableItem currentTarget = mGameInformation.getCurrentTarget();
		if (dmg != 0) {
			mGameInformation.bulletFired();
			mGameSoundManager.playGunShot();
			if (currentTarget == null) {
				//player miss
				mGameInformation.bulletMissed();
				DisplayableItem hole = DisplayableItemFactory.createBulletHole();
				final float[] currentPosition = mGameInformation.getCurrentPosition();
				hole.setX((int) currentPosition[0]);
				hole.setY((int) currentPosition[1]);
				mGameInformation.addDisplayableItem(hole);
			} else {
				currentTarget.hit(dmg);
				if (!currentTarget.isAlive()) {
					//traget killed
					mGameInformation.targetKilled();
					//increase combo
					mGameInformation.stackCombo();
					//add score
					mGameInformation.increaseScore(10 * currentTarget.getBasePoint()
							+ 10 * mGameInformation.getCurrentCombo());
					mGameInformation.earnExp(currentTarget.getExpPoint());
					mInterface.onTargetKilled(currentTarget);
					mGameBehavior.targetKilled();
					mGameSoundManager.playGhostDeath();
					((GameInformationTutorial) mGameInformation).nextStep();
				}
			}
		} else {
			mGameSoundManager.playDryGunShot();
		}
	}
}
