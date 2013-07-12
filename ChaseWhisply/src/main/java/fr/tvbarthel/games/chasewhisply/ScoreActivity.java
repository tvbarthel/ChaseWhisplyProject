package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;

public class ScoreActivity extends Activity {
	public static final String EXTRA_GAME_INFORMATION = "ScoreActivity.Extra.GameInformation";

	private GameInformation mGameInformation;

	//UI
	private TextView mFiredBulletTextView;
	private TextView mKilledTargetTextView;
	private TextView mMaxComboTextView;
	private TextView mFinalScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);

		mKilledTargetTextView = (TextView) findViewById(R.id.killedTargets);
		mFiredBulletTextView = (TextView) findViewById(R.id.firedBullets);
		mMaxComboTextView = (TextView) findViewById(R.id.maxCombo);
		mFinalScore = (TextView) findViewById(R.id.finalScore);

		mGameInformation = (GameInformation) getIntent().getParcelableExtra(EXTRA_GAME_INFORMATION);

		if (mGameInformation != null) {
			mKilledTargetTextView.setText(String.valueOf(mGameInformation.getFragNumber()));
			mMaxComboTextView.setText(String.valueOf(mGameInformation.getMaxCombo()));
			mFiredBulletTextView.setText(String.valueOf(mGameInformation.getBulletFired()));
			mFinalScore.setText(String.valueOf(mGameInformation.getCurrentScore()));
		}

	}
}
