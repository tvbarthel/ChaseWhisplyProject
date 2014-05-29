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
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.mode.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.ui.adapter.GameModeViewAdapter;
import fr.tvbarthel.games.chasewhisply.ui.customviews.GameModeView;

public class LeaderboardChooserFragment extends Fragment implements GameModeViewAdapter.Listener {


    private Listener mListener;
    private GameModeViewAdapter mGameModeViewAdapter;

    public interface Listener {
        public void onLeaderboardChosen(int leaderboardStringId);
    }

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public LeaderboardChooserFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GameScoreFragment.Listener) {
            mListener = (LeaderboardChooserFragment.Listener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement LeaderboardChooserFragment.Listener");
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

        //First mission: Scouts First
        //Sprint mode
        mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(1));

        //Second mission: Everything is an illusion
        //Twenty in a row
        mGameModeViewAdapter.add(GameModeFactory.createTwentyInARow(1));

        //Third mission: Prove your stamina
        //Marathon mode
        mGameModeViewAdapter.add(GameModeFactory.createRemainingTimeGame(3));

        //Fourth mission: Brainteaser
        //Memorize
        mGameModeViewAdapter.add(GameModeFactory.createMemorize(1));

        //Fifth mission: Death to the king
        //Death to the king
        mGameModeViewAdapter.add(GameModeFactory.createKillTheKingGame(1));

        //Sixth mission: The Final Battle
        mGameModeViewAdapter.add(GameModeFactory.createSurvivalGame(1));

        mGameModeViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGameModeSelected(GameModeView view) {
        mListener.onLeaderboardChosen(view.getModel().getLeaderboardStringId());
    }

}