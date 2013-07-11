package fr.tvbarthel.games.chasewhisply.mechanics;

public class TimeLimitedGameEngine extends GameEngine implements GameTimer.IGameTimer{
	private GameTimer mGameTimer;

	public TimeLimitedGameEngine(IGameEngine iGameEngine, GameInformation gameInformation) {
		super(iGameEngine, gameInformation);
		mGameTimer = new GameTimer(gameInformation.getRemainingTime(), this);
	}

	@Override
	public void startGame() {
		super.startGame();
		mGameTimer.startTimer();
	}

	@Override
	public void pauseGame() {
		super.pauseGame();
		mGameInformation.setRemainingTime(mGameTimer.stopTimer());
	}

	@Override
	public void resumeGame() {
		super.resumeGame();
		mGameTimer.startTimer();
	}

	@Override
	public void stopGame() {
		super.stopGame();
		mGameInformation.setRemainingTime(0);
		mInterface.onGameEngineStop();
	}

	@Override
	public void timerEnd() {
		stopGame();
	}

	@Override
	public void timerTick(long remainingTime) {
		mGameInformation.setRemainingTime(remainingTime);
	}
}
