package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fr.tvbarthel.games.chasewhisply.ui.GameModeView;

public class LevelChooserActivity extends Activity {

	private GameModeView mMode1;
	private GameModeView mMode2;
	private View.OnClickListener mModeSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_chooser);

		mModeSelected = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LevelChooserActivity.this, GameActivity.class));
			}
		};

		mMode1 = (GameModeView) findViewById(R.id.mode1);
		mMode1.setGameModeRules("Kill most ghost as you can in given time");
		mMode1.setGameModeImage(R.drawable.ghost);
		mMode1.setGameModeSelectedListener(mModeSelected);

		mMode2 = (GameModeView) findViewById(R.id.mode2);
		mMode2.setGameModeRules("Kill 10 ghost as fast as you can !");
		mMode2.setGameModeImage(R.drawable.ghost_targeted);
	}
}
