package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fr.tvbarthel.games.chasewhisply.ui.GameModeView;

public class GameModeChooserActivity extends Activity {

	private GameModeView mGameMode1;
	private GameModeView mGameMode2;
	private View.OnClickListener mGameModeSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_mode_chooser);

		mGameModeSelected = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(GameModeChooserActivity.this, GameActivity.class));
			}
		};


		mGameMode1 = (GameModeView) findViewById(R.id.mode1);
		mGameMode1.setGameModeRules("Kill most ghost as you can in given time");
		mGameMode1.setGameModeImage(R.drawable.ghost);
		mGameMode1.setGameModeSelectedListener(mGameModeSelected);

		mGameMode2 = (GameModeView) findViewById(R.id.mode2);
		mGameMode2.setGameModeRules("Kill 10 ghost as fast as you can !");
		mGameMode2.setGameModeImage(R.drawable.ghost_targeted);
	}
}
