package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.games.GamesClient;

public class HomeActivity extends Activity {

	private GamesClient mGamesClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);


		findViewById(R.id.home_sign_in).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//mGamesClient.connect();
			}
		});

		findViewById(R.id.home_play).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(HomeActivity.this, GameModeChooserActivity.class));
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
