package fr.tvbarthel.games.chasewhisply.mechanics;

public class GameEngine implements ReloadingRoutine.IReloadingRoutine {
	private GameInformation mGameInformation;
	private ReloadingRoutine mReloadingRoutine;

	public GameEngine(GameInformation gameInformation) {
		mGameInformation = gameInformation;
		mReloadingRoutine = new ReloadingRoutine(mGameInformation.getReloadingTime(), this);
	}

	public void startGame() {
		//TODO
	}

	public void pauseGame() {
		//TODO
	}

	public void resumeGame() {
		//TODO
	}

	public void stopGame() {
		//TODO
	}

	@Override
	public void reload() {

	}
}
