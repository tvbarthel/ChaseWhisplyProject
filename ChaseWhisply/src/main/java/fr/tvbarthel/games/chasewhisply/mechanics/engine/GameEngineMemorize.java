package fr.tvbarthel.games.chasewhisply.mechanics.engine;

import android.content.Context;

import fr.tvbarthel.games.chasewhisply.mechanics.behaviors.GameBehaviorMemorize;
import fr.tvbarthel.games.chasewhisply.mechanics.behaviors.GameBehaviorStandard;
import fr.tvbarthel.games.chasewhisply.mechanics.routine.Routine;
import fr.tvbarthel.games.chasewhisply.ui.gameviews.GameView;
import fr.tvbarthel.games.chasewhisply.ui.gameviews.GameViewMemorize;

public class GameEngineMemorize extends GameEngineStandard {
    private GameBehaviorMemorize mGameBehavior;
    private GameViewMemorize mGameView;

    public GameEngineMemorize(Context context, IGameEngine iGameEngine, GameBehaviorStandard gameBehavior) {
        super(context, iGameEngine, gameBehavior);
        mGameBehavior = (GameBehaviorMemorize) gameBehavior;
    }

    @Override
    protected void setGameView(GameView gameView) {
        super.setGameView(gameView);
        mGameView = (GameViewMemorize) gameView;
    }

    @Override
    public void setCameraAngle(float horizontal, float vertical) {
        super.setCameraAngle(horizontal, vertical);
        mGameBehavior.setWorldWindowSizeInDegress(horizontal, vertical);
    }

    @Override
    public void onRun(int routineType, Object obj) {
        switch (routineType) {
            case Routine.TYPE_RELOADER:
                mGameBehavior.reload();
                break;
            case Routine.TYPE_TICKER:
                mGameBehavior.nextMemorization();
                mGameView.showInstruction(true);
                break;

        }
    }

    public boolean isPlayerMemorizing() {
        return mGameBehavior.isPlayerMemorizing();
    }

    public String getCurrentMemorizationProgress() {
        return String.valueOf(mGameBehavior.getCurrentMemorizationStep() + 1) + "/" +
                String.valueOf(mGameBehavior.getMemorizationSteps());
    }

    public int getCurrentWave() {
        return mGameBehavior.getCurrentWave();
    }

    public int getCurrentGhostToMemorize() {
        return mGameBehavior.getCurrentGhostToMemorize();
    }

}
