package fr.tvbarthel.games.chasewhisply.mechanics.engine;


import android.content.Context;

import fr.tvbarthel.games.chasewhisply.mechanics.behaviors.GameBehaviorTutorial;
import fr.tvbarthel.games.chasewhisply.ui.gameviews.GameViewTutorial;

public abstract class GameEngineTutorial extends GameEngineStandard implements GameBehaviorTutorial.IGameBehaviorTutorial {
    protected GameBehaviorTutorial mGameBehavior;

    public GameEngineTutorial(Context context, IGameEngine iGameEngine, GameBehaviorTutorial gameBehavior) {
        super(context, iGameEngine, gameBehavior);
        mGameBehavior = gameBehavior;
    }

    public int getCurrentStep() {
        return mGameBehavior.getCurrentStep();
    }

    @Override
    public void onNextStep() {
        ((GameViewTutorial) mGameView).updateStepMessage();
    }
}
