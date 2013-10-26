package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;

public class GameBehaviorFactory {
	public static final int DEFAULT_MAX_TARGET = 10;

	public static GameBehaviorTimeDecreasing createSprintOrMarathon() {
		return new GameBehaviorTimeDecreasing() {
			@Override
			public void spawn(int xRange, int yRange) {
				allSpawnBehavior(xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
				mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
			}
		};
	}

	public static GameBehaviorTutorial createTutorial() {
		return new GameBehaviorTutorial();
	}

	public static GameBehaviorTimeDecreasing createSurvival() {
		return new GameBehaviorTimeDecreasing() {
			@Override
			public void spawn(int xRange, int yRange) {
				if (mGameInformation.getCurrentTargetsNumber() < DEFAULT_MAX_TARGET) {
					allSpawnBehavior(xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
					mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
				}
			}

			@Override
			protected void killTarget(TargetableItem currentTarget) {
				super.killTarget(currentTarget);
				mGameInformation.setCurrentTime(mGameInformation.getCurrentTime() + 1000);
			}
		};
	}

	public static GameBehaviorTimeIncreasing createDeathToTheKing() {
		return new GameBehaviorTimeIncreasing() {
			@Override
			public void spawn(int xRange, int yRange) {
			}

			@Override
			protected void killTarget(TargetableItem currentTarget) {
				super.killTarget(currentTarget);
				if (currentTarget.getType() == DisplayableItemFactory.TYPE_KING_GHOST) {
					mIGameBehavior.stop();
				}
			}
		};
	}

	public static GameBehaviorTwentyInARow createTwentyInARow() {
		return new GameBehaviorTwentyInARow();
	}
}
