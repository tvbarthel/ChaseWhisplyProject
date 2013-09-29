package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.ui.adapter.GameModeViewAdapter;
import fr.tvbarthel.games.chasewhisply.ui.customviews.GameModeView;

public class LeaderboardChooserFragment extends Fragment implements GameModeViewAdapter.Listener {


	private Listener mListener;
	private GameModeViewAdapter mGameModeViewAdapter;

	public interface Listener {
		public void onLeaderboardChosen(int leaderboardStringId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof GameScoreFragment.Listener) {
			mListener = (LeaderboardChooserFragment.Listener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet LeaderboardChooserFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_leaderboard_chooser, container, false);
		mGameModeViewAdapter = new GameModeViewAdapter(getActivity(), new ArrayList<GameMode>(), this);
		((GridView) v.findViewById(R.id.leaderboard_grid_view)).setAdapter(mGameModeViewAdapter);

		loadGameMode();

		return v;
	}


	private void loadGameMode() {
		mGameModeViewAdapter.clear();

		//Overall Ranking
		mGameModeViewAdapter.add(GameModeFactory.createOverallRanking());

		//First mode: Kill as many Ghosts as you can in 30 seconds.
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(1));

		//Second mode: Kill as many Ghosts as you can in 60 seconds. (level 2)
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(2));

		//Third mode: Kill as many Ghosts as you can in 30 seconds. (level 3)
		mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(3));

		//Fourth mode: survival
		mGameModeViewAdapter.add(GameModeFactory.createSurvivalGame(1));

		//Fifth mode: Death to the king
		mGameModeViewAdapter.add(GameModeFactory.createKillTheKingGame(1));

		mGameModeViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGameModeSelected(GameModeView view) {
		mListener.onLeaderboardChosen(view.getModel().getLeaderboardStringId());
	}

}