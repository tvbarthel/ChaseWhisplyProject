package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngine;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineFactory;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.ui.fragments.GameScoreFragment;

public class GameActivity extends ARActivity implements GameEngine.IGameEngine {

    private static final String BUNDLE_GAME_INFORMATION = "GameActivity.Bundle.GameInformation";
    public static final String EXTRA_GAME_MODE = "ExtraGameModeFromChooser";
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);

    private GameEngine mGameEngine;
    private GameInformation mLastGameInformationSaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_GAME_INFORMATION)) {
            mLastGameInformationSaved = savedInstanceState.getParcelable(BUNDLE_GAME_INFORMATION);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mGameEngine != null) {
            outState.putParcelable(BUNDLE_GAME_INFORMATION, mGameEngine.getGameInformation());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGameEngine != null) {
            mGameEngine.pause();
        }
    }

    @Override
    void onSmoothCoordinateChanged(float[] smoothCoordinate) {
        mGameEngine.changePosition((float) Math.toDegrees(smoothCoordinate[0]),
                (float) Math.toDegrees(smoothCoordinate[2]), (float) Math.toDegrees(smoothCoordinate[1]));
    }

    @Override
    void onCameraReady(float horizontal, float vertical) {
        //if no gameBehavior choose the one corresponding to the right gameMode
        final Intent intent = getIntent();
        if (mGameEngine != null) {
            configureGameEngine(horizontal, vertical);
            mGameEngine.resume();
        } else if (mLastGameInformationSaved != null) {
            mGameEngine = GameEngineFactory.restore(this, this, mLastGameInformationSaved);
            configureGameEngine(horizontal, vertical);
            mGameEngine.resume();
        } else if (intent != null && intent.hasExtra(EXTRA_GAME_MODE)) {
            mGameEngine = GameEngineFactory.create(this, this, (GameMode) intent.getParcelableExtra(EXTRA_GAME_MODE));
            configureGameEngine(horizontal, vertical);
            mGameEngine.start();
        } else {
            finish();
        }

    }

    private void configureGameEngine(float horizontal, float vertical) {
        mGameEngine.setCameraAngle(horizontal, vertical);
        addContentView(mGameEngine.getGameView(), mLayoutParams);
        addContentView(mGameEngine.getAnimationLayer(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onGameEngineStop() {
        final Intent scoreIntent = new Intent(this, HomeActivity.class);
        scoreIntent.putExtra(GameScoreFragment.EXTRA_GAME_INFORMATION, mGameEngine.getGameInformation());
        setResult(RESULT_OK, scoreIntent);
        finish();
    }

}
