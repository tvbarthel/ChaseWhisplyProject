package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.ui.GameModeView;
import fr.tvbarthel.games.chasewhisply.ui.adapter.GameModeViewAdapter;


public class GameModeChooserFragment extends Fragment implements GameModeViewAdapter.Listener {


	private Listener mListener;
	private PlayerProfile mPlayerProfile;
	private GameModeViewAdapter mGameModeViewAdapter;


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
		mGameModeViewAdapter = new GameModeViewAdapter(getActivity(), new ArrayList<GameMode>(), mPlayerProfile, this);
		((ListView) v.findViewById(R.id.gamemode_list_view)).setAdapter(mGameModeViewAdapter);

		loadGameMode();
		return v;
	}

	private void loadGameMode() {
		mGameModeViewAdapter.clear();

		//First mode: Kill as many Ghosts as you can in 30 seconds.
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(1));

		//Second mode: Kill as many Ghosts as you can in 60 seconds. (level 2)
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(2));

		//Third mode: Kill as many Ghosts as you can in 30 seconds. (level 3)
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(3));

		//Fourth mode: survival
		mGameModeViewAdapter.add(GameModeFactory.createSurvivalGame(1));

		mGameModeViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGameModeSelected(GameModeView view) {
		mListener.onLevelChosen(view);
	}
}
