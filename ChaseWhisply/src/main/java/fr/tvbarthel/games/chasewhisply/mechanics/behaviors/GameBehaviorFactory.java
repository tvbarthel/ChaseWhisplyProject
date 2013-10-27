package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

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

	public static GameBehaviorSurvival createSurvival() {
		return new GameBehaviorSurvival();
	}

	public static GameBehaviorDeathToTheKing createDeathToTheKing() {
		return new GameBehaviorDeathToTheKing();
	}

	public static GameBehaviorTwentyInARow createTwentyInARow() {
		return new GameBehaviorTwentyInARow();
	}
}
