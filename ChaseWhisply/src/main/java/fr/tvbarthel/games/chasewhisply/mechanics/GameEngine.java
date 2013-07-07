package fr.tvbarthel.games.chasewhisply.mechanics;

abstract public class GameEngine implements ReloadingRoutine.IReloadingRoutine {
	protected GameInformation mGameInformation;
	protected ReloadingRoutine mReloadingRoutine;
	protected int mSceneWidthInPx;
	protected int mSceneWidth;
	protected int mSceneHeightInPx;
	protected int mSceneHeight;
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


	/**
	 * Getters & Setters
	 */
	public void setSceneWidthInPx(int sceneWidthInPx) {
		mSceneWidthInPx = sceneWidthInPx;
	}

	public void setSceneHeightInPx(int sceneHeightInPx) {
		mSceneHeightInPx = sceneHeightInPx;
	}

	public void setSceneWidth(int sceneWidth) {
		mSceneWidth = sceneWidth;
	}

	public void setSceneheight(int sceneHeight) {
		mSceneHeight = sceneHeight;
	}

	public void setCurrentPos(float posX, float posY) {
		mCurrentPosX = posX;
		mCurrentPosY = posY;
	}
}
