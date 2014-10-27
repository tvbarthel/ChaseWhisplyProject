package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.mode.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.ui.adapter.GameModeViewAdapter;
import fr.tvbarthel.games.chasewhisply.ui.customviews.GameModeView;


public class GameModeChooserFragment extends Fragment implements GameModeViewAdapter.Listener {


    private Listener mListener;
    private PlayerProfile mPlayerProfile;
    private GameModeViewAdapter mGameModeViewAdapter;
    private ArrayList<GameMode> mGameModes;


    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public GameModeChooserFragment() {
    }

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
                    + " must implement GameModeChooserFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_game_mode_chooser, container, false);
        mGameModes = new ArrayList<GameMode>();
        mGameModeViewAdapter = new GameModeViewAdapter(mGameModes, mPlayerProfile, this);

        RecyclerView recyclerView = ((RecyclerView) v.findViewById(R.id.gamemode_grid_view));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(mGameModeViewAdapter);

        loadGameMode();
        return v;
    }

    private void loadGameMode() {
        mGameModes.clear();

        //how to play : learn basics of the game play
        mGameModes.add(GameModeFactory.createTutorialGame());

        //First mission: Scouts First
        //Sprint mode
        mGameModes.add(GameModeFactory.createRemainingTimeGame(1));

        //Second mission: Everything is an illusion
        //Twenty in a row
        mGameModes.add(GameModeFactory.createTwentyInARow(1));

        //Third mission: Prove your stamina
        //Marathon mode
        mGameModes.add(GameModeFactory.createRemainingTimeGame(3));

        //Fourth mission: Brainteaser
        //Memorize
        mGameModes.add(GameModeFactory.createMemorize(1));

        //Fifth mission: Death to the king
        //Death to the king
        mGameModes.add(GameModeFactory.createKillTheKingGame(1));

        //Sixth mission: The Final Battle
        mGameModes.add(GameModeFactory.createSurvivalGame(1));

        mGameModeViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGameModeSelected(GameModeView view) {
        mListener.onLevelChosen(view);
    }
}
