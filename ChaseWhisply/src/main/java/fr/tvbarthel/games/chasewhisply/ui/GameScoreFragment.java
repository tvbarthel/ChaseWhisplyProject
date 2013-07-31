package fr.tvbarthel.games.chasewhisply.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.TimerRoutine;

public class GameScoreFragment extends Fragment implements View.OnClickListener {


    //interface
    public interface Listener {
        public void onReplayRequested();

        public void onSkipRequested();

        public void onHomeRequested();

        public void onPushScoreRequested(int score);
    }

    private Listener mListener = null;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GameScoreFragment.Listener) {
            mListener = (GameScoreFragment.Listener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet GameScoreFragment.Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_score, container, false);
        final int[] clickable = new int[]{
                R.id.score_button_replay,
                R.id.score_button_home,
                R.id.score_button_skip
        };
        for (int i : clickable) {
            v.findViewById(i).setOnClickListener(this);
        }

        if (savedInstanceState != null) {
            mIsDisplayDone = savedInstanceState.getBoolean(BUNDLE_IS_DISPLAY_DONE, false);
        }

        if (getArguments().containsKey(EXTRA_GAME_INFORMATION)) {
            mGameInformation = (GameInformation) getArguments().getParcelable(EXTRA_GAME_INFORMATION);
        }

        mNumberOfTargetsKilledTextView = (TextView) v.findViewById(R.id.numberOfTargetsKilled);
        mNumberOfBulletsFiredTextView = (TextView) v.findViewById(R.id.numberOfBulletsFired);
        mMaxComboTextView = (TextView) v.findViewById(R.id.maxCombo);
        mFinalScoreTextView = (TextView) v.findViewById(R.id.finalScore);

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

        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.score_button_home:
                mListener.onHomeRequested();
                mListener.onPushScoreRequested(mGameInformation.getCurrentScore());
                break;
            case R.id.score_button_skip:
                mListener.onSkipRequested();
                break;
            case R.id.score_button_replay:
                mListener.onReplayRequested();
                break;
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
        if (mGameInformation != null) {
            mNumberOfBulletsFiredByTick =
                    (float) (mGameInformation.getBulletFired() - mCurrentNumberOfBulletsFired) / NUMBER_OF_TICK;
            mNumberOfTargetsKilledByTick =
                    (float) (mGameInformation.getFragNumber() - mCurrentNumberOfTargetsKilled) / NUMBER_OF_TICK;
            mComboByTick = (float) (mGameInformation.getMaxCombo() - mCurrentMaxCombo) / NUMBER_OF_TICK;
            mFinalScoreByTick = (float) (mGameInformation.getCurrentScore() - mCurrentFinalScore) / NUMBER_OF_TICK;
        }
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
