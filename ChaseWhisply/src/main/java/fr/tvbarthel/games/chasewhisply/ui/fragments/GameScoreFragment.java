package fr.tvbarthel.games.chasewhisply.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.routine.TimerRoutine;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntryFactory;

public class GameScoreFragment extends Fragment implements View.OnClickListener {
	public static final String FRAGMENT_TAG = "GameScoreFragment_TAG";
	public static final String EXTRA_GAME_INFORMATION = "GameScoreFragment.Extra.GameInformation";
	private static final String BUNDLE_IS_DISPLAY_DONE = GameScoreFragment.class.getName() + ".Bundle.isDisplayDone";
	private static final String BUNDLE_CURRENT_NUMBER_OF_BULLETS_FIRED =
			GameScoreFragment.class.getName() + ".Bundle.currentNumberOfBulletsFired";
	private static final String BUNDLE_CURRENT_NUMBER_OF_TARGETS_KILLED =
			GameScoreFragment.class.getName() + ".Bundle.currentNumberOfTargetsKilled";
	private static final String BUNDLE_CURRENT_MAX_COMBO =
			GameScoreFragment.class.getName() + ".Bundle.currentMaxCombo";
	private static final String BUNDLE_CURRENT_FINAL_SCORE =
			GameScoreFragment.class.getName() + ".Bundle.currentFinalScore";
	private static final String BUNDLE_CURRENT_ACHIEVEMENT_CHECKED =
			GameScoreFragment.class.getName() + ".Bundle.achievementChecked";
	private static final String BUNDLE_CURRENT_PLAYER_PROFILE_SAVED =
			GameScoreFragment.class.getName() + ".Bundle.playerProfileSaved";
	private static final String BUNDLE_CURRENT_EXP_EARNED =
			GameScoreFragment.class.getName() + ".Bundle.expEarned";
	private static final long CLICK_DELAY = 1500;
	private static final long TICK_INTERVAL = 100;
	private static final int NUMBER_OF_TICK = 30;
	private Listener mListener = null;
	private GameInformation mGameInformation;
	private TimerRoutine mTimerRoutine;
	private float mNumberOfBulletsFiredByTick;
	private float mCurrentNumberOfBulletsFired;
	private float mNumberOfTargetsKilledByTick;
	private float mCurrentNumberOfTargetsKilled;
	private float mCurrentExpEarned;
	private float mExpEarnedByTick;
	private float mComboByTick;
	private float mCurrentMaxCombo;
	private float mFinalScoreByTick;
	private float mCurrentFinalScore;
	private float mCurrentTickNumber;
	private boolean mIsDisplayDone = false;
	private boolean mAchievementChecked = false;
	private PlayerProfile mPlayerProfile;
	private boolean mPlayerProfileSaved = false;
	//Details from game played
	private long mRetrievedBulletFired;
	private long mRetrievedTargetKilled;
	private long mRetrievedExpEarned;
	private long mRetrievedCombo;
	private long mRetrievedScore;
	//UI
	private TextView mNumberOfBulletsFiredTextView;
	private TextView mNumberOfTargetsKilledTextView;
	private TextView mMaxComboTextView;
	private TextView mFinalScoreTextView;
	private TextView mExpEarnedTextView;
	private Button mSkipButton;
	private View mSignInView;
	private long mAttachTime;

	public static GameScoreFragment newInstance(GameInformation gameInformation) {
		final GameScoreFragment fragment = new GameScoreFragment();
		final Bundle arguments = new Bundle();
		arguments.putParcelable(GameScoreFragment.EXTRA_GAME_INFORMATION,
				gameInformation);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAttachTime = System.currentTimeMillis();
		if (activity instanceof GameScoreFragment.Listener) {
			mListener = (GameScoreFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameScoreFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
		if (mTimerRoutine != null) {
			mTimerRoutine.stopRoutine();
			mTimerRoutine = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_score, container, false);
		final int[] clickable = new int[]{
				R.id.score_button_replay,
				R.id.score_button_home,
				R.id.score_button_skip,
				R.id.score_button_share
		};
		for (int i : clickable) {
			v.findViewById(i).setOnClickListener(this);
		}

		if (savedInstanceState != null) {
			mIsDisplayDone = savedInstanceState.getBoolean(BUNDLE_IS_DISPLAY_DONE, false);
			mAchievementChecked = savedInstanceState.getBoolean(BUNDLE_CURRENT_ACHIEVEMENT_CHECKED, false);
			mPlayerProfileSaved = savedInstanceState.getBoolean(BUNDLE_CURRENT_PLAYER_PROFILE_SAVED, false);
		}

		if (getArguments().containsKey(EXTRA_GAME_INFORMATION)) {
			mGameInformation = getArguments().getParcelable(EXTRA_GAME_INFORMATION);
			retrieveGameDetails(mGameInformation);
		}

		mNumberOfTargetsKilledTextView = (TextView) v.findViewById(R.id.numberOfTargetsKilled);
		mNumberOfBulletsFiredTextView = (TextView) v.findViewById(R.id.numberOfBulletsFired);
		mMaxComboTextView = (TextView) v.findViewById(R.id.maxCombo);
		mFinalScoreTextView = (TextView) v.findViewById(R.id.finalScore);
		mSkipButton = (Button) v.findViewById(R.id.score_button_skip);
		mSignInView = v.findViewById(R.id.sign_in_message);
		mExpEarnedTextView = (TextView) v.findViewById(R.id.expEarned);

		HashMap<Integer, Integer> loots = mGameInformation.getLoot();
		if (loots.size() != 0) {
			String stringLoot = "";
			for (Map.Entry<Integer, Integer> entry : loots.entrySet()) {
				InventoryItemEntry inventoryItemEntry = InventoryItemEntryFactory.create(entry.getKey(), entry.getValue());
				final long quantityDropped = inventoryItemEntry.getQuantityAvailable();
				final int titleResourceId = inventoryItemEntry.getTitleResourceId();
				stringLoot += String.valueOf(quantityDropped) + "x " + getResources().getQuantityString(titleResourceId, (int) quantityDropped) + "\n";
			}
			stringLoot = stringLoot.substring(0, stringLoot.length() - 1);
			((TextView) v.findViewById(R.id.score_loot_list)).setText(stringLoot);
		}

		updatePlayerProfile();
		return v;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTimerRoutine = new TimerRoutine(TICK_INTERVAL) {
			@Override
			protected void doOnTick() {
				if (mCurrentTickNumber >= NUMBER_OF_TICK) {
					finalizeScoreDisplayed();
				} else {
					incrementCurrentScoreDisplayed();
				}
			}
		};

		if (mIsDisplayDone) {
			finalizeScoreDisplayed();
		} else if (hasSomethingToDisplay()) {
			initScoreDisplay(savedInstanceState);
			mTimerRoutine.startRoutine();
		} else {
			mSkipButton.setVisibility(View.GONE);
			mIsDisplayDone = true;
		}
	}

	@Override
	public void onClick(View view) {
		if (mIsDisplayDone || (System.currentTimeMillis() - mAttachTime > CLICK_DELAY)) {
			switch (view.getId()) {
				case R.id.score_button_home:
					mListener.onHomeRequested();
					break;
				case R.id.score_button_skip:
					finalizeScoreDisplayed();
					break;
				case R.id.score_button_replay:
					mListener.onReplayRequested(mGameInformation);
					break;
				case R.id.score_button_share:
					mListener.onShareScoreRequested(mRetrievedScore);
					break;
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(BUNDLE_IS_DISPLAY_DONE, mIsDisplayDone);
		outState.putFloat(BUNDLE_CURRENT_NUMBER_OF_BULLETS_FIRED, mCurrentNumberOfBulletsFired);
		outState.putFloat(BUNDLE_CURRENT_NUMBER_OF_TARGETS_KILLED, mCurrentNumberOfTargetsKilled);
		outState.putFloat(BUNDLE_CURRENT_MAX_COMBO, mCurrentMaxCombo);
		outState.putFloat(BUNDLE_CURRENT_FINAL_SCORE, mCurrentFinalScore);
		outState.putBoolean(BUNDLE_CURRENT_ACHIEVEMENT_CHECKED, mAchievementChecked);
		outState.putBoolean(BUNDLE_CURRENT_PLAYER_PROFILE_SAVED, mPlayerProfileSaved);
		outState.putFloat(BUNDLE_CURRENT_EXP_EARNED, mCurrentExpEarned);
	}

	public void notifySignedStateChanged(boolean signedIn) {
		if (signedIn) {
			mSignInView.setVisibility(View.GONE);
			if (!mAchievementChecked) {
				mListener.onUpdateAchievements(mGameInformation, mPlayerProfile);
				mAchievementChecked = true;
			}
		} else {
			mSignInView.setVisibility(View.VISIBLE);
		}
	}

	private void retrieveGameDetails(GameInformation gameInformation) {
		mRetrievedBulletFired = gameInformation.getBulletsFired();
		mRetrievedTargetKilled = gameInformation.getFragNumber();
		mRetrievedCombo = gameInformation.getMaxCombo();
		mRetrievedExpEarned = gameInformation.getExpEarned();
		mRetrievedScore = gameInformation.getCurrentScore();
	}

	private void initCurrentScoreDisplayed(Bundle savedBundle) {
		if (savedBundle != null) {
			mCurrentNumberOfBulletsFired = savedBundle.getFloat(BUNDLE_CURRENT_NUMBER_OF_BULLETS_FIRED);
			mCurrentNumberOfTargetsKilled = savedBundle.getFloat(BUNDLE_CURRENT_NUMBER_OF_TARGETS_KILLED);
			mCurrentMaxCombo = savedBundle.getFloat(BUNDLE_CURRENT_MAX_COMBO);
			mCurrentFinalScore = savedBundle.getFloat(BUNDLE_CURRENT_FINAL_SCORE);
			mCurrentExpEarned = savedBundle.getFloat(BUNDLE_CURRENT_EXP_EARNED);
		} else {
			mCurrentNumberOfBulletsFired = 0;
			mCurrentNumberOfTargetsKilled = 0;
			mCurrentMaxCombo = 0;
			mCurrentFinalScore = 0;
			mCurrentExpEarned = 0;
		}
	}

	private void initScoreByTick() {
		if (mGameInformation != null) {
			mNumberOfBulletsFiredByTick =
					(float) (mRetrievedBulletFired - mCurrentNumberOfBulletsFired) / NUMBER_OF_TICK;
			mNumberOfTargetsKilledByTick =
					(float) (mRetrievedTargetKilled - mCurrentNumberOfTargetsKilled) / NUMBER_OF_TICK;
			mComboByTick = (float) (mRetrievedCombo - mCurrentMaxCombo) / NUMBER_OF_TICK;
			mFinalScoreByTick = (float) (mRetrievedScore - mCurrentFinalScore) / NUMBER_OF_TICK;
			mExpEarnedByTick = (float) (mRetrievedExpEarned - mCurrentExpEarned) / NUMBER_OF_TICK;
		}
	}

	private void initScoreDisplay(Bundle savedBundle) {
		mCurrentTickNumber = 0;
		initCurrentScoreDisplayed(savedBundle);
		initScoreByTick();
	}

	private void incrementCurrentScoreDisplayed() {
		mCurrentTickNumber++;
		mCurrentNumberOfBulletsFired += mNumberOfBulletsFiredByTick;
		mCurrentNumberOfTargetsKilled += mNumberOfTargetsKilledByTick;
		mCurrentMaxCombo += mComboByTick;
		mCurrentFinalScore += mFinalScoreByTick;
		mCurrentExpEarned += mExpEarnedByTick;

		displayDetails(
				(long) mCurrentNumberOfBulletsFired,
				(long) mCurrentNumberOfTargetsKilled,
				(long) mCurrentMaxCombo,
				(long) mCurrentFinalScore,
				(long) mCurrentExpEarned);
	}

	private void finalizeScoreDisplayed() {
		mTimerRoutine.stopRoutine();

		displayDetails(
				mRetrievedBulletFired,
				mRetrievedTargetKilled,
				mRetrievedCombo,
				mRetrievedScore,
				mRetrievedExpEarned);

		if (mIsDisplayDone) {
			mSkipButton.setVisibility(View.GONE);
		} else {
			fadeOut(mSkipButton);
			mIsDisplayDone = true;
		}

	}

	private void displayDetails(long bulletFired, long fragNumber, long maxCombo, long score, long expEarned) {

		mNumberOfBulletsFiredTextView.setText(String.valueOf(bulletFired));
		mNumberOfTargetsKilledTextView.setText(String.valueOf(fragNumber));
		mMaxComboTextView.setText(String.valueOf(maxCombo));
		mExpEarnedTextView.setText(String.valueOf(expEarned));

		//TODO create an abstract method displaysGamesDetails and implement a specific behavior for each mode
		switch (mGameInformation.getGameMode().getType()) {

			case GameModeFactory.GAME_TYPE_SURVIVAL:
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
			case GameModeFactory.GAME_TYPE_TUTORIAL:
				mFinalScoreTextView.setText(String.valueOf(score));
				break;
			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
				if (score > 0) {
					final Calendar cl = Calendar.getInstance();
					cl.setTimeInMillis(score);
					mFinalScoreTextView.setText(cl.get(Calendar.SECOND) + "' " + cl.get(Calendar.MILLISECOND) + "''");
				} else {
					mFinalScoreTextView.setText(getResources().getString(R.string.score_lost));
				}

				break;
		}
	}

	private boolean hasSomethingToDisplay() {
		return mGameInformation.getCurrentScore() != 0 ||
				mGameInformation.getMaxCombo() != 0 ||
				mGameInformation.getFragNumber() != 0 ||
				mGameInformation.getBulletsFired() != 0 ||
				mGameInformation.getExpEarned() != 0;

	}

	private void fadeOut(final View view) {
		final ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).setDuration(500);
		fadeOutAnimation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				view.setVisibility(View.GONE);
				fadeOutAnimation.removeListener(this);
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				fadeOutAnimation.removeListener(this);
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		fadeOutAnimation.start();
	}

	private void updatePlayerProfile() {
		if (mGameInformation != null && !mPlayerProfileSaved) {
			mPlayerProfile.increaseBulletsFired(mRetrievedBulletFired);
			mPlayerProfile.increaseGamesPlayed(1);
			mPlayerProfile.increaseTargetsKilled(mRetrievedTargetKilled);
			mPlayerProfile.increaseBulletsMissed(mGameInformation.getBulletsMissed());
			mPlayerProfile.increaseExperienceEarned(mRetrievedExpEarned);
			updateInventoryEntryQuantity();
			mGameInformation.useBonus(mPlayerProfile);
			mPlayerProfileSaved = mPlayerProfile.saveChanges();
		}
	}

	private void updateInventoryEntryQuantity() {
		for (Map.Entry<Integer, Integer> entry : mGameInformation.getLoot().entrySet()) {
			mPlayerProfile.increaseInventoryItemQuantity(entry.getKey(), entry.getValue());
		}
	}

	//interface
	public interface Listener {
		public void onReplayRequested(GameInformation gameInformation);

		public void onHomeRequested();

		public void onUpdateAchievements(final GameInformation gameInformation, final PlayerProfile playerProfile);

		public void onShareScoreRequested(long score);
	}
}
