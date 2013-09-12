package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.ui.GameModeView;


public class GameModeChooserFragment extends Fragment implements View.OnClickListener {

	private GameModeView mGameMode1;
	private GameModeView mGameMode2;
	private GameModeView mGameMode3;
	private GameModeView mGameMode4;
	private Listener mListener;
	private PlayerProfile mPlayerProfile;

	public interface Listener {
		public void onLevelChosen(GameModeView g);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof GameScoreFragment.Listener) {
			mListener = (GameModeChooserFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet GameModeChooserFragment.Listener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_game_mode_chooser, container, false);

		//First mode: Kill as many Ghosts as you can in 30 seconds. (level 1)
		mGameMode1 = (GameModeView) v.findViewById(R.id.mode1);
		mGameMode1.setModel(GameModeFactory.createRemainingTimeGame(1));
		mGameMode1.setGameModeEnabled(mGameMode1.getModel().isAvailable(mPlayerProfile));
		mGameMode1.setGameModeSelectedListener(this);

		//Second mode: Kill as many Ghosts as you can in 60 seconds. (level 2)
		mGameMode2 = (GameModeView) v.findViewById(R.id.mode2);
		mGameMode2.setModel(GameModeFactory.createRemainingTimeGame(2));
		mGameMode2.setGameModeEnabled(mGameMode2.getModel().isAvailable(mPlayerProfile));
		mGameMode2.setGameModeSelectedListener(this);

		//Third mode: Kill as many Ghosts as you can in 30 seconds. (level 3)
		mGameMode3 = (GameModeView) v.findViewById(R.id.mode3);
		mGameMode3.setModel(GameModeFactory.createRemainingTimeGame(3));
		mGameMode3.setGameModeEnabled(mGameMode3.getModel().isAvailable(mPlayerProfile));
		mGameMode3.setGameModeSelectedListener(this);

		//Fourth mode: survival
		mGameMode4 = (GameModeView) v.findViewById(R.id.mode4);
		mGameMode4.setModel(GameModeFactory.createSurvivalGame(1));
		mGameMode4.setGameModeEnabled(mGameMode4.getModel().isAvailable(mPlayerProfile));
		mGameMode4.setGameModeSelectedListener(this);

		return v;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.mode1:
			case R.id.mode2:
			case R.id.mode3:
			case R.id.mode4:
				mListener.onLevelChosen((GameModeView) view);
				break;
		}

	}
}
