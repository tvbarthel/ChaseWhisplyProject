package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.mechanics.routine.ReloadingRoutine;
import fr.tvbarthel.games.chasewhisply.mechanics.routine.SpawningRoutine;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

abstract public class GameEngine implements ReloadingRoutine.IReloadingRoutine, SpawningRoutine.ISpawningRoutine {
	public static final int STATE_STOP = 0x00000001;
	public static final int STATE_RUNNING = 0x00000002;
	public static final int STATE_PAUSED = 0x00000003;

	protected IGameEngine mInterface;
	protected GameInformation mGameInformation;
	protected ReloadingRoutine mReloadingRoutine;
	protected SpawningRoutine mSpawningRoutine;
	protected int mCurrentState;

	protected abstract void onKill();


	public GameEngine(IGameEngine iGameEngine, GameInformation gameInformation) {
		mInterface = iGameEngine;
		mGameInformation = gameInformation;
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getWeapon().getReloadingTime(), this);
		mSpawningRoutine = new SpawningRoutine(mGameInformation.getSpawningTime(), this);
		mCurrentState = STATE_STOP;
	}

	/**
	 * start the game, should be called only at the beginning once.
	 */
	public void startGame() {
		mReloadingRoutine.startRoutine();
		mSpawningRoutine.startRoutine();
		mCurrentState = STATE_RUNNING;
		//TODO
	}

	/**
	 * pause the game.
	 */
	public void pauseGame() {
		mReloadingRoutine.stopRoutine();
		mSpawningRoutine.stopRoutine();
		mCurrentState = STATE_PAUSED;
		//TODO
	}

	/**
	 * resume the game
	 */
	public void resumeGame() {
		mReloadingRoutine.startRoutine();
		mSpawningRoutine.startRoutine();
		mCurrentState = STATE_RUNNING;
		//TODO
	}

	/**
	 * stop the game, should be called only once at the end.
	 */
	public void stopGame() {
		mReloadingRoutine.stopRoutine();
		mSpawningRoutine.stopRoutine();
		mCurrentState = STATE_STOP;
		//TODO
	}

	@Override
	public void reload() {
		mGameInformation.getWeapon().reload();
	}

	@Override
	public void spawn() {
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
					onKill();
				}
			}
		}
	}

	public void changePosition(float posX, float posY) {
		mGameInformation.setCurrentPosition(posX, posY);
	}

	/**
	 * Getters & Setters
	 */
	public void setSceneWidth(int sceneWidth) {
		mGameInformation.setSceneWidth(sceneWidth);
	}

	public void setSceneHeight(int sceneHeight) {
		mGameInformation.setSceneHeight(sceneHeight);
	}

	public GameInformation getGameInformation() {
		return mGameInformation;
	}

	public int getCurrentState() {
		return mCurrentState;
	}

	public interface IGameEngine {
		abstract void onGameEngineStop();
	}

}
