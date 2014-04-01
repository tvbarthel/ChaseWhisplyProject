package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationMemorize;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;

public class GameBehaviorMemorize extends GameBehaviorStandard {
    private GameInformationMemorize mGameInformation;
    private float mWorldWindowWidthInDegree;
    private float mWorldWindowHeightInDegree;

    @Override
    public void setGameInformation(GameInformation gameInformation) {
        super.setGameInformation(gameInformation);
        mGameInformation = (GameInformationMemorize) gameInformation;
    }

    public void setWorldWindowSizeInDegress(float horizontalLimitInDegree, float verticalLimitInDegree) {
        mWorldWindowWidthInDegree = horizontalLimitInDegree;
        mWorldWindowHeightInDegree = verticalLimitInDegree;
    }

    @Override
    public void onClick() {
        if (mGameInformation.isPlayerKilling()) {
            super.onClick();
        }
    }

    @Override
    public void spawn(int xRange, int yRange) {
    }

    @Override
    protected void killTarget(TargetableItem currentTarget) {
        super.killTarget(currentTarget);
        if (currentTarget.getType() != mGameInformation.getCurrentGhostType()) {
            mIGameBehavior.stop();
        } else {
            nextTarget();
        }
    }

    private void nextTarget() {
        final int currentCursor = mGameInformation.increaseCursor();
        if (currentCursor == mGameInformation.getWaveSize()) {
            mGameInformation.setState(GameInformationMemorize.STATE_MEMORIZING);
            mGameInformation.generateNextWave();
        }
    }

    public void nextMemorization() {
        if (mGameInformation.isPlayerMemorizing()) {
            final int memorizationSteps = mGameInformation.getWaveSize();
            final int currentStep = mGameInformation.getCursor();
            if (currentStep == memorizationSteps - 1) {
                mGameInformation.setState(GameInformationMemorize.STATE_KILLING);
                mGameInformation.resetCursor();
                summonCurrentWave();
            } else {
                mGameInformation.increaseCursor();
            }
        }
    }

    public boolean isPlayerMemorizing() {
        return mGameInformation.isPlayerMemorizing();
    }

    public int getCurrentMemorizationStep() {
        return mGameInformation.getCursor();
    }

    public int getMemorizationSteps() {
        return mGameInformation.getWaveSize();
    }

    public int getCurrentWave() {
        return mGameInformation.getCurrentWaveNumber();
    }

    public int getCurrentGhostToMemorize() {
        return mGameInformation.getCurrentGhostType();
    }

    private void summonCurrentWave() {
        for (Integer ghostType : mGameInformation.getCurrentWave()) {
            spawnGhost(ghostType, (int) mWorldWindowWidthInDegree / 2, (int) mWorldWindowHeightInDegree / 2);
            mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
        }
    }

}
