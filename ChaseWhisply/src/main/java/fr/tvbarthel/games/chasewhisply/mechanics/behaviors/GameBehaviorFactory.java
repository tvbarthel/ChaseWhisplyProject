package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;

public class GameBehaviorFactory {
    public static final int DEFAULT_MAX_TARGET = 10;

    public static GameBehaviorTimeDecreasing createSprint() {
        return new GameBehaviorTimeDecreasing() {
            @Override
            public void spawn(int xRange, int yRange) {
                if (mGameInformation.getCurrentTargetsNumber() < GameBehaviorFactory.DEFAULT_MAX_TARGET) {
                    final int ghostType = TargetableItem.randomGhostTypeEasy();
                    spawnGhost(ghostType, xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
                    mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
                }
            }
        };
    }

    public static GameBehaviorTimeDecreasing createMarathon() {
        return new GameBehaviorTimeDecreasing() {
            @Override
            public void spawn(int xRange, int yRange) {
                if (mGameInformation.getCurrentTargetsNumber() < GameBehaviorFactory.DEFAULT_MAX_TARGET) {
                    final int ghostType = TargetableItem.randomGhostType();
                    spawnGhost(ghostType, xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
                    mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
                }
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

    public static GameBehaviorMemorize createMemorize() {
        return new GameBehaviorMemorize();
    }
}
