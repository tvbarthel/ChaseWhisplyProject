package fr.tvbarthel.games.chasewhisply;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.games.GamesClient;

import fr.tvbarthel.games.chasewhisply.google.BaseGameActivity;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.ui.AboutFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameHomeFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameModeChooserFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameModeView;
import fr.tvbarthel.games.chasewhisply.ui.GameScoreFragment;

public class HomeActivity extends BaseGameActivity implements GameHomeFragment.Listener, GameScoreFragment.Listener, GameModeChooserFragment.Listener {
	//Request code
	private static final int REQUEST_ACHIEVEMENT = 0x00000000;
	private static final int REQUEST_LEADERBOARD = 0x00000001;
	private static final int REQUEST_GAME_ACTIVITY_FRESH_START = 0x00000002;
	private static final int REQUEST_GAME_ACTIVITY_REPLAY = 0x00000003;
	//sign in
	private boolean mSignedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_home);

		mSignedIn = false;

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
					new GameHomeFragment(), GameHomeFragment.FRAGMENT_TAG).commit();
		}

	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);

		if (REQUEST_GAME_ACTIVITY_REPLAY == request) {
			getSupportFragmentManager().popBackStackImmediate();
		}

		if (RESULT_OK == response) {
			if (REQUEST_GAME_ACTIVITY_REPLAY == request || REQUEST_GAME_ACTIVITY_FRESH_START == request) {
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.game_home_fragment_container,
								GameScoreFragment.newInstance(
										(GameInformation) data.getParcelableExtra(GameScoreFragment.EXTRA_GAME_INFORMATION)))
						.addToBackStack(null).commitAllowingStateLoss();
			}
		}
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
		mSignedIn = true;
		final Fragment gameHomeFragment = getSupportFragmentManager()
				.findFragmentByTag(GameHomeFragment.FRAGMENT_TAG);
		if (gameHomeFragment != null) {
			((GameHomeFragment) gameHomeFragment).notifySignedStateChanged(mSignedIn);
		}
	}

	@Override
	public void onSignOutSucceeded() {
		mSignedIn = false;
		makeToast(getString(R.string.home_sign_out_success));
		final Fragment gameHomeFragment = getSupportFragmentManager()
				.findFragmentByTag(GameHomeFragment.FRAGMENT_TAG);
		if (gameHomeFragment != null) {
			((GameHomeFragment) gameHomeFragment).notifySignedStateChanged(mSignedIn);
		}
	}

	@Override
	public void onStartGameRequested() {
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				new GameModeChooserFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
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
		getSupportFragmentManager().beginTransaction().replace(R.id.game_home_fragment_container,
				new AboutFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	@Override
	public void onSignInButtonClicked() {
		beginUserInitiatedSignIn();
	}

	@Override
	public void onSignOutButtonClicked() {
		SignOutConfirmDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
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
	public void onReplayRequested(GameInformation gameInformation) {
		startNewGame(gameInformation.getGameMode(), REQUEST_GAME_ACTIVITY_REPLAY);
	}

	@Override
	public void onHomeRequested() {
		Intent i = new Intent(this, HomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
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
		startNewGame(g.getModel(), REQUEST_GAME_ACTIVITY_FRESH_START);
	}

	public void startNewGame(GameMode gameMode, int requestCode) {
		final Intent i = new Intent(this, GameActivity.class);
		i.putExtra(GameActivity.EXTRA_GAME_MODE, gameMode);
		startActivityForResult(i, requestCode);
	}

	public static class SignOutConfirmDialogFragment extends DialogFragment {
		public static SignOutConfirmDialogFragment newInstance() {
			return new SignOutConfirmDialogFragment();
		}

		public SignOutConfirmDialogFragment() {
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.app_name)
					.setMessage(R.string.home_sign_out_confirm_dialog_message)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int which) {
							((HomeActivity) getActivity()).signOut();
						}
					})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int which) {
							dialogInterface.dismiss();
						}
					})
					.create();
		}
	}
}
