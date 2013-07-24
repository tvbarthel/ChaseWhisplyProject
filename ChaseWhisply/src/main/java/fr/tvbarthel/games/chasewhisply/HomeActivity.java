package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import fr.tvbarthel.games.chasewhisply.google.BaseGameActivity;
import fr.tvbarthel.games.chasewhisply.ui.GameHomeFragment;

public class HomeActivity extends BaseGameActivity implements GameHomeFragment.Listener {

	//Request code
	private static final int REQUEST_ACHIEVEMENT = 0x00000000;
	private static final int REQUEST_LEADERBOARD = 0x00000001;

	private ImageView mWhisplyPicture;
	private boolean mIsWhisplyAnimationRunning;

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

	public void initWhisplyPicture() {
		mWhisplyPicture = (ImageView) findViewById(R.id.home_whisply_picture);
		mIsWhisplyAnimationRunning = false;
		final Animation whisplyAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.whisply_picture_animation);
		if (whisplyAnimation == null) return;
		whisplyAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mIsWhisplyAnimationRunning = true;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mIsWhisplyAnimationRunning = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		mWhisplyPicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsWhisplyAnimationRunning) {
					mWhisplyPicture.startAnimation(whisplyAnimation);
				}
			}
		});
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

	}
}
