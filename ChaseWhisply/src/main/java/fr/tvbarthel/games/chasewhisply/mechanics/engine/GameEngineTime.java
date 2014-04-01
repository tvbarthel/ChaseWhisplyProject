package fr.tvbarthel.games.chasewhisply.mechanics.engine;


import android.content.Context;

import fr.tvbarthel.games.chasewhisply.mechanics.behaviors.GameBehaviorTime;

public abstract class GameEngineTime extends GameEngineStandard {
    protected GameBehaviorTime mGameBehavior;

    public GameEngineTime(Context context, IGameEngine iGameEngine, GameBehaviorTime gameBehavior) {
        super(context, iGameEngine, gameBehavior);
        mGameBehavior = gameBehavior;
    }

    public long getCurrentTime() {
        return mGameBehavior.getCurrentTime();
    }

    @Override
    public void start() {
        mGameBehavior.setStartingTime();
        super.start();
    }

    @Override
    public void stop() {
        mGameBehavior.setEndingTime();
        super.stop();
    }
}
