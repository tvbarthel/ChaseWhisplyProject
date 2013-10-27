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
				spawnStandardBehavior(xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
				mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
			}
		};
	}

	public static GameBehaviorTutorial createTutorial() {
		return new GameBehaviorTutorial();
	}

	public static GameBehaviorTimeDecreasing createSurvival() {
		return new GameBehaviorSurvival();
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
