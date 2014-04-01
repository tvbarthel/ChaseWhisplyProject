package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationSurvival;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;

public class GameBehaviorSurvival extends GameBehaviorTimeDecreasing {
    private static final int MAX_TARGET_EASY = 10;
    private static final int MAX_TARGET_HARD = 6;
    private static final int TIME_GAIN_EASY = 1000;
    private static final int TIME_GAIN_HARD = 800;
    private GameInformationSurvival mGameInformation;

    @Override
    public void setGameInformation(GameInformation gameInformation) {
        super.setGameInformation(gameInformation);
        mGameInformation = (GameInformationSurvival) gameInformation;
    }

    @Override
    public void spawn(int xRange, int yRange) {
        final int currentTargetNumber = mGameInformation.getCurrentTargetsNumber();
        final int difficulty = mGameInformation.getDifficulty();

        if (difficulty == GameInformationSurvival.DIFFICULTY_EASY && currentTargetNumber < MAX_TARGET_EASY) {
            spawnStandardBehavior(xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
            mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
        } else if (difficulty == GameInformationSurvival.DIFFICULTY_HARD && currentTargetNumber < MAX_TARGET_HARD) {
            spawnHardBehavior(xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
            mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
        } else if (difficulty == GameInformationSurvival.DIFFICULTY_HARDER && currentTargetNumber < MAX_TARGET_HARD) {
            spawnHarderBehavior(xRange / 2 + 2 * xRange / 10, yRange / 2 + 2 * yRange / 10);
            mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
        } else if (currentTargetNumber < MAX_TARGET_HARD) {
            spawnHardestBehavior(xRange / 2 + 2 * xRange / 10, yRange / 2 + 2 * yRange / 10);
            mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
        }
    }

    @Override
    protected void killTarget(TargetableItem currentTarget) {
        super.killTarget(currentTarget);
        final int difficulty = mGameInformation.getDifficulty();
        int bonusTime = TIME_GAIN_EASY;

        if (difficulty != GameInformationSurvival.DIFFICULTY_EASY) {
            bonusTime = TIME_GAIN_HARD;
        }

        mGameInformation.setCurrentTime(mGameInformation.getCurrentTime() + bonusTime);
    }
}
