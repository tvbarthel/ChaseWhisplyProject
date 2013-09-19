package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.mechanics.GameEngine;
import fr.tvbarthel.games.chasewhisply.model.GameBehavior;
import fr.tvbarthel.games.chasewhisply.model.GameBehaviorFactory;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.ui.AnimationLayer;
import fr.tvbarthel.games.chasewhisply.ui.GameView;
import fr.tvbarthel.games.chasewhisply.ui.fragments.GameScoreFragment;

public class GameActivity extends ARActivity implements GameEngine.IGameEngine, View.OnClickListener {

	private static final String BUNDLE_GAME_BEHAVIOR = "GameActivity.Bundle.GameBehavior";
	public static final String EXTRA_GAME_MODE = "ExtraGameModeFromChooser";
	private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
			, ViewGroup.LayoutParams.WRAP_CONTENT);
	private GameBehavior mGameBehavior;
	private GameInformation mGameInformation;
	private GameEngine mGameEngine;
	private GameView mGameView;
	private GameMode mGameMode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_GAME_BEHAVIOR)) {
			mGameBehavior = savedInstanceState.getParcelable(BUNDLE_GAME_BEHAVIOR);
			mGameInformation = mGameBehavior.getGameInformation();
			mGameMode = mGameBehavior.getGameInformation().getGameMode();
		} else if (intent != null && intent.hasExtra(EXTRA_GAME_MODE)) {
			mGameMode = intent.getParcelableExtra(EXTRA_GAME_MODE);
		} else {
			finish();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BUNDLE_GAME_BEHAVIOR, mGameBehavior);
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
		//if no gameBehavior choose the one corresponding to the right gameMode
		boolean firstLaunch = false;
		if (mGameBehavior == null) {
			firstLaunch = true;
			switch (mGameMode.getType()) {
				case GameModeFactory.GAME_TYPE_REMAINING_TIME:
					mGameBehavior = GameBehaviorFactory.createRemainingTimeAllMobsGame(
							mGameMode.getLevel(), horizontal, vertical, mGameMode);
					break;
				case GameModeFactory.GAME_TYPE_SURVIVAL:
					mGameBehavior = GameBehaviorFactory.createSurvivalGame(
							horizontal, vertical, mGameMode);
					break;
				case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
					mGameBehavior = GameBehaviorFactory.createKillTheKingGame(
							horizontal, vertical, mGameMode);
					break;
				default:
					finish();
					break;
			}
			mGameInformation = mGameBehavior.getGameInformation();
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
		mGameEngine = new GameEngine(GameActivity.this, mGameBehavior);
		if (firstLaunch) {
			mGameEngine.startGame();
		} else {
			mGameEngine.resumeGame();
		}

	}

	@Override
	public void onGameEngineStop() {
		final Intent scoreIntent = new Intent(this, HomeActivity.class);
		scoreIntent.putExtra(GameScoreFragment.EXTRA_GAME_INFORMATION, mGameBehavior.getGameInformation());
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
