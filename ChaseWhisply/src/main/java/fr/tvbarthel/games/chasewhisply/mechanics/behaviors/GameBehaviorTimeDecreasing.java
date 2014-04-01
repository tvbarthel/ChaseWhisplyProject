package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

public abstract class GameBehaviorTimeDecreasing extends GameBehaviorTime {

    @Override
    public void tick(long tickingTime) {
        //decrease time at each tick
        final long currentTime = mGameInformation.getCurrentTime();
        final long timeAfterTick = currentTime - tickingTime;
        if (timeAfterTick > 0) {
            mGameInformation.setCurrentTime(timeAfterTick);
        } else {
            mGameInformation.setCurrentTime(0);
            mIGameBehavior.stop();
        }
    }

}
