package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.MathUtils;

public class TimeLimitedGameEngine extends GameEngine implements GameTimer.IGameTimer {
	protected GameTimer mGameTimer;

	private int mXRange;
	private int mYRange;

	@Override
	protected void onKill() {

	}

	@Override
	public void spawn() {
		super.spawn();
		if (mGameInformation.getCurrentTargetsNumber() < mGameInformation.getMaxTargetOnTheField()) {
			final int randomDraw = MathUtils.randomize(0,100);
			final float[] pos = mGameInformation.getCurrentPosition();
			if(randomDraw < 80) {
				mGameInformation.addTargetableItem(DisplayableItemFactory.createEasyGhost(
						(int) pos[0] - mXRange,
						(int) pos[0] + mXRange,
						(int) pos[1] - mYRange,
						(int) pos[1] + mYRange));
			}else {
				mGameInformation.addTargetableItem(DisplayableItemFactory.createBabyGhost(
						(int) pos[0] - mXRange,
						(int) pos[0] + mXRange,
						(int) pos[1] - mYRange,
						(int) pos[1] + mYRange));
			}
		}
	}

	public TimeLimitedGameEngine(IGameEngine iGameEngine, GameInformation gameInformation) {
		super(iGameEngine, gameInformation);
		mGameTimer = new GameTimer(gameInformation.getRemainingTime(), this);
	}

	@Override
	public void startGame() {
		super.startGame();
		mGameTimer.startTimer();
		mXRange = mGameInformation.getSceneWidth() / 2 + mGameInformation.getSceneWidth() / 10;
		mYRange = mGameInformation.getSceneHeight() / 2 + mGameInformation.getSceneHeight() / 10;
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
