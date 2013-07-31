package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.games.GamesClient;

import fr.tvbarthel.games.chasewhisply.google.BaseGameActivity;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.ui.AboutFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameHomeFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameModeChooserFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameModeView;
import fr.tvbarthel.games.chasewhisply.ui.GameScoreFragment;

public class HomeActivity extends BaseGameActivity implements GameHomeFragment.Listener, GameScoreFragment.Listener, GameModeChooserFragment.Listener {
	//Request code
	private static final int REQUEST_ACHIEVEMENT = 0x00000000;
	private static final int REQUEST_LEADERBOARD = 0x00000001;
	//Fragments
	private GameHomeFragment mGameHomeFragment;
	private GameScoreFragment mGameScoreFragment;
	private GameModeChooserFragment mGameModeChooserFragment;
	private AboutFragment mAboutFragment;
	//sign in
	private boolean mSignedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_home);

		mSignedIn = false;

		if (getIntent().hasExtra(GameScoreFragment.EXTRA_GAME_INFORMATION)) {
			mGameScoreFragment = GameScoreFragment.newInstance((GameInformation)
					getIntent().getParcelableExtra(GameScoreFragment.EXTRA_GAME_INFORMATION));
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					mGameScoreFragment).commit();
			getIntent().removeExtra(GameScoreFragment.EXTRA_GAME_INFORMATION);
		} else if (savedInstanceState == null) {
			mGameHomeFragment = new GameHomeFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					mGameHomeFragment).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
		mSignedIn = true;
		final Fragment gameHomeFragment = getSupportFragmentManager()
				.findFragmentById(R.id.game_home_fragment_container);
		if (gameHomeFragment != null) {
			((GameHomeFragment) gameHomeFragment).notifySignedStateChanged(true);
		}
	}

	@Override
	public void onSignOutSucceeded() {
		mSignedIn = false;
		makeToast(getString(R.string.home_sign_out_success));
		final Fragment gameHomeFragment = getSupportFragmentManager()
				.findFragmentById(R.id.game_home_fragment_container);
		if (gameHomeFragment != null) {
			((GameHomeFragment) gameHomeFragment).notifySignedStateChanged(false);
		}
	}

	@Override
	public void onStartGameRequested() {
		if (mGameModeChooserFragment == null) {
			mGameModeChooserFragment = new GameModeChooserFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				mGameModeChooserFragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	@Override
	public void onShowAchievementsRequested() {
		if (mSignedIn) {
			startActivityForResult(getGamesClient().getAchievementsIntent(), REQUEST_ACHIEVEMENT);
		} else {
			makeToast(getResources().getString(R.string.home_not_sign_in_achievement));
		}
	}

	@Override
	public void onShowLeaderboardsRequested() {
		if (mSignedIn) {
			startActivityForResult(getGamesClient().getLeaderboardIntent(
					getResources().getString(R.string.leaderboard_easy)), REQUEST_LEADERBOARD);
		} else {
			makeToast(getResources().getString(R.string.home_not_sign_in_leaderboard));
		}
	}

	@Override
	public void onShowAboutRequested() {
		if (mAboutFragment == null) {
			mAboutFragment = new AboutFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				mAboutFragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	@Override
	public void onSignInButtonClicked() {
		beginUserInitiatedSignIn();
	}

	@Override
	public void onSignOutButtonClicked() {
		signOut();
	}

	@Override
	public void onWhisplyPictureClicked() {
		if (mSignedIn) {
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_curiosity));
		}
	}

	/**
	 * use to inform user
	 *
	 * @param message display on screen
	 */
	private void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onReplayRequested() {

	}

	@Override
	public void onSkipRequested() {

	}

	@Override
	public void onHomeRequested() {
		if (mGameHomeFragment == null) {
			mGameHomeFragment = new GameHomeFragment();
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				mGameHomeFragment).commit();
	}

	@Override
	public void onUpdateAchievements(final GameInformation gameInformation) {
		final GamesClient gamesClient = getGamesClient();
		if (gamesClient.isConnected()) {
			final int score = gameInformation.getCurrentScore();
			gamesClient.submitScore(getResources().getString(R.string.leaderboard_easy), score);
			gamesClient.incrementAchievement(getResources().getString(R.string.achievement_soldier), 1);
			gamesClient.incrementAchievement(getResources().getString(R.string.achievement_corporal), 1);
			gamesClient.incrementAchievement(getResources().getString(R.string.achievement_sergeant), 1);
			if (score == 0) {
				gamesClient.unlockAchievement(getResources().getString(R.string.achievement_pacifist));
			}
		}
	}

	@Override
	public void onLevelChosen(GameModeView g) {
		final Intent i = new Intent(this, GameActivity.class);
		i.putExtra(GameActivity.EXTRA_GAME_MODE, g.getModel());
		startActivity(i);
	}
}
