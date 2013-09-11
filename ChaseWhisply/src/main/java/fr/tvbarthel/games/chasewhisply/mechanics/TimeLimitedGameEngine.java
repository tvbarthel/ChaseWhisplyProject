package fr.tvbarthel.games.chasewhisply.mechanics;

import fr.tvbarthel.games.chasewhisply.mechanics.timer.EggTimer;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.MathUtils;

public class TimeLimitedGameEngine extends GameEngine implements EggTimer.IEggTimer {
	protected EggTimer mEggTimer;

	private int mXRange;
	private int mYRange;

	@Override
	protected void onKill() {

	}

	@Override
	public void spawn() {
		super.spawn();
		if (mGameInformation.getCurrentTargetsNumber() < mGameInformation.getMaxTargetOnTheField()) {
			final int randomDraw = MathUtils.randomize(0, 100);
			final float[] pos = mGameInformation.getCurrentPosition();
			int ghostType;
			if (randomDraw < 60) {
				ghostType = DisplayableItemFactory.TYPE_EASY_GHOST;
			} else if (randomDraw < 75) {
				ghostType = DisplayableItemFactory.TYPE_HIDDEN_GHOST;
			} else if (randomDraw < 90) {
				ghostType = DisplayableItemFactory.TYPE_BABY_GHOST;
			} else if (randomDraw < 99) {
				ghostType = DisplayableItemFactory.TYPE_GHOST_WITH_HELMET;
			} else {
				ghostType = DisplayableItemFactory.TYPE_KING_GHOST;
			}
			mGameInformation.addTargetableItem(DisplayableItemFactory.createGhostWithRandomCoordinates(
					ghostType,
					(int) pos[0] - mXRange,
					(int) pos[0] + mXRange,
					(int) pos[1] - mYRange,
					(int) pos[1] + mYRange
			));
		}
	}

	public TimeLimitedGameEngine(IGameEngine iGameEngine, GameInformation gameInformation) {
		super(iGameEngine, gameInformation);
		mEggTimer = new EggTimer(gameInformation.getTime(), this);
	}

	@Override
	public void startGame() {
		super.startGame();
		mEggTimer.startTimer();
		mXRange = mGameInformation.getSceneWidth() / 2 + mGameInformation.getSceneWidth() / 10;
		mYRange = mGameInformation.getSceneHeight() / 2 + mGameInformation.getSceneHeight() / 10;
	}

	@Override
	public void pauseGame() {
		super.pauseGame();
		mGameInformation.setTime(mEggTimer.stopTimer());
	}

	@Override
	public void resumeGame() {
		super.resumeGame();
		mEggTimer.startTimer();
	}

	@Override
	public void stopGame() {
		super.stopGame();
		mGameInformation.setTime(0);
		mInterface.onGameEngineStop();
	}

	@Override
	public void timerEnd() {
		stopGame();
	}

	@Override
	public void timerTick(long remainingTime) {
		mGameInformation.setTime(remainingTime);
	}

}
