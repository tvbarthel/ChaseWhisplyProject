package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import fr.tvbarthel.games.chasewhisply.google.BaseGameActivity;
import fr.tvbarthel.games.chasewhisply.ui.GameHomeFragment;

public class HomeActivity extends BaseGameActivity implements GameHomeFragment.Listener {

	//Request code
	private static final int REQUEST_ACHIEVEMENT = 0x00000000;
	private static final int REQUEST_LEADERBOARD = 0x00000001;

	//Fragments
	private GameHomeFragment mGameHomeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_home);

		mGameHomeFragment = new GameHomeFragment();
		mGameHomeFragment.setListener(this);

		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				mGameHomeFragment).commit();

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
}
