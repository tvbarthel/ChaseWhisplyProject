package fr.tvbarthel.games.chasewhisply.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import fr.tvbarthel.games.chasewhisply.R;

public class GameHomeFragment extends Fragment implements View.OnClickListener {

	//interface
	public interface Listener {
		public void onStartGameRequested();

		public void onShowAchievementsRequested();

		public void onShowLeaderboardsRequested();

		public void onShowAboutRequested();

		public void onSignInButtonClicked();

		public void onSignOutButtonClicked();

		public void onWhisplyPictureClicked();
	}

	private Listener mListener = null;

	//animation
	private ImageView mWhisplyPicture;
	private boolean mIsWhisplyAnimationRunning;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		final int[] clickable = new int[]{
				R.id.home_play,
				R.id.home_leaderboard,
				R.id.home_achievement,
				R.id.home_about,
				R.id.home_sign_in,
				R.id.home_whisply_picture
		};
		for (int i : clickable) {
			v.findViewById(i).setOnClickListener(this);
		}
		initWhisplyPicture(v);
		return v;
	}

	/**
	 * set a listener for action callback
	 *
	 * @param l GameHomeFragment.Listener
	 */
	public void setListener(Listener l) {
		mListener = l;
	}

	private void initWhisplyPicture(View v) {
		mWhisplyPicture = (ImageView) v.findViewById(R.id.home_whisply_picture);
		mIsWhisplyAnimationRunning = false;
		final Animation whisplyAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.whisply_picture_animation);
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
	public void onClick(View view) {
		if (mListener == null)
			return;
		switch (view.getId()) {
			case R.id.home_play:
				mListener.onStartGameRequested();
				break;
			case R.id.home_leaderboard:
				mListener.onShowLeaderboardsRequested();
				break;
			case R.id.home_achievement:
				mListener.onShowAchievementsRequested();
				break;
			case R.id.home_about:
				mListener.onShowAboutRequested();
				break;
			case R.id.home_sign_in:
				mListener.onSignInButtonClicked();
				break;
			case R.id.home_whisply_picture:
				mListener.onWhisplyPictureClicked();
				break;
			default:
				break;
		}
	}
}
