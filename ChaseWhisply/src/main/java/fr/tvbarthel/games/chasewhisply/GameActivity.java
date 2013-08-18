package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.mechanics.GameEngine;
import fr.tvbarthel.games.chasewhisply.mechanics.SurvivalGameEngine;
import fr.tvbarthel.games.chasewhisply.mechanics.TimeLimitedGameEngine;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameInformationFactory;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.ui.AnimationLayer;
import fr.tvbarthel.games.chasewhisply.ui.fragments.GameScoreFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameView;

public class GameActivity extends ARActivity implements GameEngine.IGameEngine,
		View.OnClickListener {

	public static final String EXTRA_GAME_MODE = "ExtraGameModeFromChooser";
	private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
			, ViewGroup.LayoutParams.WRAP_CONTENT);
	private GameInformation mGameInformation;
	private GameEngine mGameEngine;
	private GameView mGameView;
	private GameMode mGameMode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		if (intent == null || !intent.hasExtra(EXTRA_GAME_MODE)) {
			finish();
		} else {
			mGameMode = intent.getParcelableExtra(EXTRA_GAME_MODE);
		}
	}


	@Override
	protected void onPause() {
		super.onPause();

		if (mGameEngine != null) {
			mGameEngine.pauseGame();
		}
	}


	@Override
	void onSmoothCoordinateChanged(float[] smoothCoordinate) {
		mGameEngine.changePosition((float) Math.toDegrees(smoothCoordinate[0]),
				(float) Math.toDegrees(smoothCoordinate[1]));
		mGameView.invalidate();
	}

	@Override
	void onCameraReady(float horizontal, float vertical) {
		if (mGameInformation == null) {
			mGameInformation = GameInformationFactory.createEmptyWorld(
					horizontal, vertical, mGameMode);
		}

		//instantiate GameView with GameModel
		mGameView = new GameView(GameActivity.this, mGameInformation);
		mGameView.setOnClickListener(GameActivity.this);
		addContentView(mGameView, mLayoutParams);

		AnimationLayer animationLayer = new AnimationLayer(GameActivity.this);
		addContentView(animationLayer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
				, ViewGroup.LayoutParams.MATCH_PARENT));

		mGameView.setAnimationLayer(animationLayer);

		//instantiate game engine
		switch (mGameMode.getType()) {
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
				mGameEngine = new TimeLimitedGameEngine(GameActivity.this, mGameInformation);
				mGameEngine.startGame();
				break;
			case GameModeFactory.GAME_TYPE_SURVIVAL:
				mGameEngine = new SurvivalGameEngine(GameActivity.this, mGameInformation, 1000);
				mGameEngine.startGame();
				break;
			default:
				finish();
				break;
		}

	}

	@Override
	public void onGameEngineStop() {
		final Intent scoreIntent = new Intent(this, HomeActivity.class);
		scoreIntent.putExtra(GameScoreFragment.EXTRA_GAME_INFORMATION, mGameInformation);
		setResult(RESULT_OK, scoreIntent);
		finish();
	}

	@Override
	public void onTargetKilled(TargetableItem targetKilled) {
		mGameView.animateDyingGhost(targetKilled);
	}

	@Override
	public void onClick(View view) {
		mGameEngine.fire();
		mGameView.invalidate();
	}

}
