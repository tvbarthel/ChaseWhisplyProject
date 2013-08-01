package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.TimerRoutine;

public class ScoreActivity extends Activity {
	public static final String EXTRA_GAME_INFORMATION = "ScoreActivity.Extra.GameInformation";
	private static final String BUNDLE_IS_DISPLAY_DONE = ScoreActivity.class.getName() + ".Bundle.isDisplayDone";
	private static final String BUNDLE_CURRENT_NUMBER_OF_BULLETS_FIRED =
			ScoreActivity.class.getName() + ".Bundle.currentNumberOfBulletsFired";
	private static final String BUNDLE_CURRENT_NUMBER_OF_TARGETS_KILLED =
			ScoreActivity.class.getName() + ".Bundle.currentNumberOfTargetsKilled";
	private static final String BUNDLE_CURRENT_MAX_COMBO =
			ScoreActivity.class.getName() + ".Bundle.currentMaxCombo";
	private static final String BUNDLE_CURRENT_FINAL_SCORE =
			ScoreActivity.class.getName() + ".Bundle.currentFinalScore";
	private static final long TICK_INTERVAL = 100;
	private static final int NUMBER_OF_TICK = 30;

	private GameInformation mGameInformation;

	private TimerRoutine mTimerRoutine;
	private float mNumberOfBulletsFiredByTick;
	private float mCurrentNumberOfBulletsFired;
	private float mNumberOfTargetsKilledByTick;
	private float mCurrentNumberOfTargetsKilled;
	private float mComboByTick;
	private float mCurrentMaxCombo;
	private float mFinalScoreByTick;
	private float mCurrentFinalScore;
	private float mCurrentTickNumber;
	private boolean mIsDisplayDone = false;


	//UI
	private TextView mNumberOfBulletsFiredTextView;
	private TextView mNumberOfTargetsKilledTextView;
	private TextView mMaxComboTextView;
	private TextView mFinalScoreTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_score);

		if (savedInstanceState != null) {
			mIsDisplayDone = savedInstanceState.getBoolean(BUNDLE_IS_DISPLAY_DONE, false);
		}

		mNumberOfTargetsKilledTextView = (TextView) findViewById(R.id.numberOfTargetsKilled);
		mNumberOfBulletsFiredTextView = (TextView) findViewById(R.id.numberOfBulletsFired);
		mMaxComboTextView = (TextView) findViewById(R.id.maxCombo);
		mFinalScoreTextView = (TextView) findViewById(R.id.finalScore);

		mGameInformation = (GameInformation) getIntent().getParcelableExtra(EXTRA_GAME_INFORMATION);

		findViewById(R.id.score_button_replay).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(ScoreActivity.this, GameActivity.class));
				finish();
			}
		});

		findViewById(R.id.score_button_home).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(ScoreActivity.this, HomeActivity.class));
				finish();
			}
		});

		findViewById(R.id.score_button_skip).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finalizeScoreDisplayed();
			}
		});

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
		} else {
			initScoreDisplay(savedInstanceState);
			mTimerRoutine.startRoutine();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(BUNDLE_IS_DISPLAY_DONE, mIsDisplayDone);
		outState.putFloat(BUNDLE_CURRENT_NUMBER_OF_BULLETS_FIRED, mCurrentNumberOfBulletsFired);
		outState.putFloat(BUNDLE_CURRENT_NUMBER_OF_TARGETS_KILLED, mCurrentNumberOfTargetsKilled);
		outState.putFloat(BUNDLE_CURRENT_MAX_COMBO, mCurrentMaxCombo);
		outState.putFloat(BUNDLE_CURRENT_FINAL_SCORE, mCurrentFinalScore);

	}

	private void initCurrentScoreDisplayed(Bundle savedBundle) {
		if (savedBundle != null) {
			mCurrentNumberOfBulletsFired = savedBundle.getFloat(BUNDLE_CURRENT_NUMBER_OF_BULLETS_FIRED);
			mCurrentNumberOfTargetsKilled = savedBundle.getFloat(BUNDLE_CURRENT_NUMBER_OF_TARGETS_KILLED);
			mCurrentMaxCombo = savedBundle.getFloat(BUNDLE_CURRENT_MAX_COMBO);
			mCurrentFinalScore = savedBundle.getFloat(BUNDLE_CURRENT_FINAL_SCORE);
		} else {
			mCurrentNumberOfBulletsFired = 0;
			mCurrentNumberOfTargetsKilled = 0;
			mCurrentMaxCombo = 0;
			mCurrentFinalScore = 0;
		}
	}

	private void initScoreByTick() {
		mNumberOfBulletsFiredByTick =
				(float) (mGameInformation.getBulletFired() - mCurrentNumberOfBulletsFired) / NUMBER_OF_TICK;
		mNumberOfTargetsKilledByTick =
				(float) (mGameInformation.getFragNumber() - mCurrentNumberOfTargetsKilled) / NUMBER_OF_TICK;
		mComboByTick = (float) (mGameInformation.getMaxCombo() - mCurrentMaxCombo) / NUMBER_OF_TICK;
		mFinalScoreByTick = (float) (mGameInformation.getCurrentScore() - mCurrentFinalScore) / NUMBER_OF_TICK;
	}

	private void initScoreDisplay(Bundle savedBundle) {
		mCurrentTickNumber = 0;
		initCurrentScoreDisplayed(savedBundle);
		initScoreByTick();
	}

	private void finalizeScoreDisplayed() {
		mTimerRoutine.stopRoutine();
		mNumberOfBulletsFiredTextView.setText(String.valueOf(mGameInformation.getBulletFired()));
		mNumberOfTargetsKilledTextView.setText(String.valueOf(mGameInformation.getFragNumber()));
		mMaxComboTextView.setText(String.valueOf(mGameInformation.getMaxCombo()));
		mFinalScoreTextView.setText(String.valueOf(mGameInformation.getCurrentScore()));
		mIsDisplayDone = true;
	}

	private void incrementCurrentScoreDisplayed() {
		mCurrentTickNumber++;
		mCurrentNumberOfBulletsFired += mNumberOfBulletsFiredByTick;
		mCurrentNumberOfTargetsKilled += mNumberOfTargetsKilledByTick;
		mCurrentMaxCombo += mComboByTick;
		mCurrentFinalScore += mFinalScoreByTick;

		mNumberOfBulletsFiredTextView.setText(String.valueOf((int) mCurrentNumberOfBulletsFired));
		mNumberOfTargetsKilledTextView.setText(String.valueOf((int) mCurrentNumberOfTargetsKilled));
		mMaxComboTextView.setText(String.valueOf((int) mCurrentMaxCombo));
		mFinalScoreTextView.setText(String.valueOf((int) mCurrentFinalScore));
	}
}
