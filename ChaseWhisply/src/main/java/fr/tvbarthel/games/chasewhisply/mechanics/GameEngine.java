package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

abstract public class GameEngine implements ReloadingRoutine.IReloadingRoutine {
	protected GameInformation mGameInformation;
	protected ReloadingRoutine mReloadingRoutine;
	protected TargetableItem mCurrentTarget;


	public GameEngine(GameInformation gameInformation) {
		mGameInformation = gameInformation;
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getWeapon().getReloadingTime(), this);
		mCurrentTarget = null;
	}

	/**
	 * start the game, should be called only at the beginning once.
	 */
	public void startGame() {
		mReloadingRoutine.startRoutine();
		//TODO
	}

	/**
	 * pause the game.
	 */
	public void pauseGame() {
		mReloadingRoutine.stopRoutine();
		//TODO
	}

	/**
	 * resume the game
	 */
	public void resumeGame() {
		mReloadingRoutine.startRoutine();
		//TODO
	}

	/**
	 * stop the game, should be called only once at the end.
	 */
	public void stopGame() {
		mReloadingRoutine.stopRoutine();
		//TODO
	}

	@Override
	public void reload() {
		mGameInformation.getWeapon().reload();
	}

	/**
	 * call when use touch screen
	 * create bullet hole or hit current target
	 */
	public void fire() {
		final int dmg = mGameInformation.getWeapon().fire();
		if (mCurrentTarget == null) {
			//TODO create a bullet hole
		} else {
			//TODO hit currentTarget
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

}
