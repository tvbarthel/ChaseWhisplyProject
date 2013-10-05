package fr.tvbarthel.games.chasewhisply.mechanics;


import android.content.Context;

import fr.tvbarthel.games.chasewhisply.mechanics.routine.ReloadingRoutine;
import fr.tvbarthel.games.chasewhisply.mechanics.routine.SpawningRoutine;
import fr.tvbarthel.games.chasewhisply.mechanics.routine.TickerRoutine;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameBehavior;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;

public class GameEngine implements ReloadingRoutine.IReloadingRoutine, SpawningRoutine.ISpawningRoutine, TickerRoutine.ITickerRoutine {
	public static final int STATE_STOP = 0x00000001;
	public static final int STATE_RUNNING = 0x00000002;
	public static final int STATE_PAUSED = 0x00000003;

	protected IGameEngine mInterface;
	protected GameInformation mGameInformation;
	protected GameBehavior mGameBehavior;
	protected ReloadingRoutine mReloadingRoutine;
	protected SpawningRoutine mSpawningRoutine;
	protected TickerRoutine mTickerRoutine;
	protected int mCurrentState;
	private int mXRange;
	private int mYRange;
	private GameSoundManager mGameSoundManager;


	public GameEngine(final Context context, IGameEngine iGameEngine, GameBehavior gameBehavior) {
		mInterface = iGameEngine;
		mGameBehavior = gameBehavior;
		mGameInformation = mGameBehavior.getGameInformation();
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getWeapon().getReloadingTime(), this);
		mSpawningRoutine = new SpawningRoutine(mGameInformation.getSpawningTime(), this);
		mTickerRoutine = new TickerRoutine(mGameInformation.getTickingTime(), this);
		mCurrentState = STATE_STOP;
		mGameSoundManager = new GameSoundManager(context);
	}


	/**
	 * start the game, should be called only at the beginning once.
	 */
	public void startGame() {
		mGameBehavior.init();
		mReloadingRoutine.startRoutine();
		mSpawningRoutine.startRoutine();
		mTickerRoutine.startRoutine();
		mCurrentState = STATE_RUNNING;
		mXRange = mGameInformation.getSceneWidth() / 2 + mGameInformation.getSceneWidth() / 10;
		mYRange = mGameInformation.getSceneHeight() / 2 + mGameInformation.getSceneHeight() / 10;
	}

	/**
	 * pause the game.
	 */
	public void pauseGame() {
		mGameSoundManager.stopAllSounds();
		mReloadingRoutine.stopRoutine();
		mSpawningRoutine.stopRoutine();
		mTickerRoutine.stopRoutine();
		mCurrentState = STATE_PAUSED;
	}

	/**
	 * resume the game
	 */
	public void resumeGame() {
		mReloadingRoutine.startRoutine();
		mSpawningRoutine.startRoutine();
		mTickerRoutine.startRoutine();
		mCurrentState = STATE_RUNNING;
		mXRange = mGameInformation.getSceneWidth() / 2 + mGameInformation.getSceneWidth() / 10;
		mYRange = mGameInformation.getSceneHeight() / 2 + mGameInformation.getSceneHeight() / 10;
	}

	/**
	 * stop the game, should be called only once at the end.
	 */
	public void stopGame() {
		mGameSoundManager.stopAllSounds();
		mReloadingRoutine.stopRoutine();
		mSpawningRoutine.stopRoutine();
		mTickerRoutine.stopRoutine();
		mCurrentState = STATE_STOP;
		mInterface.onGameEngineStop();
	}

	@Override
	public void reload() {
		mGameInformation.getWeapon().reload();
	}

	@Override
	public void spawn() {
		mGameBehavior.spawn(mXRange, mYRange);
		mGameSoundManager.playGhostLaugh();
	}

	@Override
	public void onTick(long tickingTime) {
		mGameBehavior.tick(tickingTime);
		if (mGameBehavior.isCompleted()) {
			stopGame();
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
		abstract public void onGameEngineStop();

		abstract public void onTargetKilled(TargetableItem targetKilled);
	}

}
