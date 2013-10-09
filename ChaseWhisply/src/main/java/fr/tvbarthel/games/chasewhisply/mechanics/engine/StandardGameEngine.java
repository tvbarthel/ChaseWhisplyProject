package fr.tvbarthel.games.chasewhisply.mechanics.engine;

import android.content.Context;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameBehavior;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

public class StandardGameEngine extends GameEngine {

	/**
	 * Standard GameEngine
	 * fire on touch
	 * sounds
	 * works with timer
	 *
	 * @param context
	 * @param iGameEngine
	 * @param gameBehavior
	 */
	public StandardGameEngine(Context context, IGameEngine iGameEngine, GameBehavior gameBehavior) {
		super(context, iGameEngine, gameBehavior);
	}

	@Override
	public void onScreenTouch() {
		fire();
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
					mGameInformation.increaseScore(10 * currentTarget.getBasePoint() + 10 * mGameInformation.getCurrentCombo());
					mGameInformation.earnExp(currentTarget.getExpPoint());
					mInterface.onTargetKilled(currentTarget);
					mGameBehavior.targetKilled();
					mGameSoundManager.playGhostDeath();
				}
			}
		} else {
			mGameSoundManager.playDryGunShot();
		}
	}
}
