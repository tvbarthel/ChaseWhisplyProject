package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.os.Bundle;

import fr.tvbarthel.games.chasewhisply.ui.GameModeView;

public class LevelChooserActivity extends Activity {

	private GameModeView mMode1;
	private GameModeView mMode2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_chooser);

		mMode1 = (GameModeView) findViewById(R.id.mode1);
		mMode1.setGameModeRules("Kill most ghost as you can in given time");
		mMode1.setGameModeImage(R.drawable.ghost);

		mMode2 = (GameModeView) findViewById(R.id.mode2);
		mMode2.setGameModeRules("Kill 10 ghost as fast as you can !");
		mMode2.setGameModeImage(R.drawable.ghost_targeted);
	}
}
