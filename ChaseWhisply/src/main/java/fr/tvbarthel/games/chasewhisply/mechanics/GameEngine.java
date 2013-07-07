package fr.tvbarthel.games.chasewhisply.mechanics;

abstract public class GameEngine implements ReloadingRoutine.IReloadingRoutine {
	protected GameInformation mGameInformation;
	protected ReloadingRoutine mReloadingRoutine;
	protected float mCurrentPosX;
	protected float mCurrentPosY;


	public GameEngine(GameInformation gameInformation) {
		mGameInformation = gameInformation;
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getWeapon().getReloadingTime(), this);
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

	public int fire() {
		return mGameInformation.getWeapon().fire();
	}

	public void changePosition(float posX, float posY) {
		setCurrentPos(posX, posY);
		mGameInformation.updateItemVisibility(posX, posY);
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

	public void setCurrentPos(float posX, float posY) {
		mCurrentPosX = posX;
		mCurrentPosY = posY;
	}
}
