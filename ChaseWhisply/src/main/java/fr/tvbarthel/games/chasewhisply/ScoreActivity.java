package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;

public class ScoreActivity extends Activity {
	public static final String EXTRA_GAME_INFORMATION = "ScoreActivity.Extra.GameInformation";

	private GameInformation mGameInformation;

	//UI
	private TextView mNumberOfBulletsFiredTextView;
	private TextView mNumberOfTargetsKilledTextView;
	private TextView mMaxComboTextView;
	private TextView mFinalScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);

		mNumberOfTargetsKilledTextView = (TextView) findViewById(R.id.numberOfTargetsKilled);
		mNumberOfBulletsFiredTextView = (TextView) findViewById(R.id.numberOfBulletsFired);
		mMaxComboTextView = (TextView) findViewById(R.id.maxCombo);
		mFinalScore = (TextView) findViewById(R.id.finalScore);

		mGameInformation = (GameInformation) getIntent().getParcelableExtra(EXTRA_GAME_INFORMATION);

		if (mGameInformation != null) {
			mNumberOfTargetsKilledTextView.setText(String.valueOf(mGameInformation.getFragNumber()));
			mMaxComboTextView.setText(String.valueOf(mGameInformation.getMaxCombo()));
			mNumberOfBulletsFiredTextView.setText(String.valueOf(mGameInformation.getBulletFired()));
			mFinalScore.setText(String.valueOf(mGameInformation.getCurrentScore()));
		}

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

	}
}
