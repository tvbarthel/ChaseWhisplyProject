package fr.tvbarthel.games.chasewhisply.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import fr.tvbarthel.games.chasewhisply.R;

public class GameHomeFragment extends Fragment implements View.OnClickListener {
	public static final String FRAGMENT_TAG = "GameHomeFragment_TAG";

	private Listener mListener = null;
	//animation
	private Animation mWhisplyAnimation;
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
				R.id.home_sign_out,
				R.id.home_whisply_picture
		};
		for (int i : clickable) {
			v.findViewById(i).setOnClickListener(this);
		}
		initWhisplyPicture(v);
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof GameHomeFragment.Listener) {
			mListener = (GameHomeFragment.Listener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameHomeFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	private void initWhisplyPicture(View v) {
		mIsWhisplyAnimationRunning = false;
		mWhisplyAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.whisply_picture_animation);
		if (mWhisplyAnimation == null) return;
		mWhisplyAnimation.setAnimationListener(new Animation.AnimationListener() {
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
			case R.id.home_sign_out:
				mListener.onSignOutButtonClicked();
				break;
			case R.id.home_sign_in:
				mListener.onSignInButtonClicked();
				break;
			case R.id.home_whisply_picture:
				if (!mIsWhisplyAnimationRunning) {
					view.startAnimation(mWhisplyAnimation);
				}
				mListener.onWhisplyPictureClicked();
				break;
			default:
				break;
		}
	}

	public void notifySignedStateChanged(boolean signedIn) {
		final Activity activity = getActivity();
		if (signedIn) {
			activity.findViewById(R.id.home_sign_in).setVisibility(View.GONE);
			activity.findViewById(R.id.home_sign_out).setVisibility(View.VISIBLE);
		} else {
			activity.findViewById(R.id.home_sign_out).setVisibility(View.GONE);
			activity.findViewById(R.id.home_sign_in).setVisibility(View.VISIBLE);
		}

	}

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
}
