package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

import fr.tvbarthel.games.chasewhisply.google.BaseGameActivity;
import fr.tvbarthel.games.chasewhisply.ui.AboutFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameHomeFragment;

public class HomeActivity extends BaseGameActivity implements GameHomeFragment.Listener {

	//Request code
	private static final int REQUEST_ACHIEVEMENT = 0x00000000;
	private static final int REQUEST_LEADERBOARD = 0x00000001;

	//Fragments
	private GameHomeFragment mGameHomeFragment;
	private AboutFragment mAboutFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_home);

		if (savedInstanceState == null) {
			mGameHomeFragment = new GameHomeFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					mGameHomeFragment).commit();
		}

//		initWhisplyPicture();
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
	}

	@Override
	public void onStartGameRequested() {
		startActivity(new Intent(HomeActivity.this, GameModeChooserActivity.class));
	}

	@Override
	public void onShowAchievementsRequested() {
		if (getGamesClient().isConnected()) {
			startActivityForResult(getGamesClient().getAchievementsIntent(), REQUEST_ACHIEVEMENT);
		}
	}

	@Override
	public void onShowLeaderboardsRequested() {
		if (getGamesClient().isConnected()) {
			startActivityForResult(getGamesClient().getLeaderboardIntent(
					getResources().getString(R.string.leaderboard_easy)), REQUEST_LEADERBOARD);
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

	}

	@Override
	public void onWhisplyPictureClicked() {
		if (getGamesClient().isConnected()) {
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

}
