package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

public abstract class GameBehaviorTimeIncreasing extends GameBehaviorTime {

    @Override
    public void tick(long tickingTime) {
        mGameInformation.setCurrentTime(mGameInformation.getCurrentTime() + tickingTime);
    }

}
