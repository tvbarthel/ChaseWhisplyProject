package fr.tvbarthel.games.chasewhisply.mechanics;

public class GameEngine implements ReloadingRoutine.IReloadingRoutine {
	private GameInformation mGameInformation;
	private ReloadingRoutine mReloadingRoutine;

	public GameEngine(GameInformation gameInformation) {
		mGameInformation = gameInformation;
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getReloadingTime(), this);
	}

	public void startGame() {
		mReloadingRoutine.startRoutine();
		//TODO
	}

	public void pauseGame() {
		mReloadingRoutine.stopRoutine();
		//TODO
	}

	public void resumeGame() {
		mReloadingRoutine.startRoutine();
		//TODO
	}

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
}
