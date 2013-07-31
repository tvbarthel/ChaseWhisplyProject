package fr.tvbarthel.games.chasewhisply.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;


public class GameModeChooserFragment extends Fragment implements View.OnClickListener {

    private GameModeView mGameMode1;
    private GameModeView mGameMode2;
    private Listener mListener;

    public interface Listener {
        public void onLevelChosen(GameModeView g
        );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GameScoreFragment.Listener) {
            mListener = (GameModeChooserFragment.Listener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet GameModeChooserFragment.Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_game_mode_chooser, container, false);

        mGameMode1 = (GameModeView) v.findViewById(R.id.mode1);
        mGameMode1.setModel(GameModeFactory.createRemainingTimeGame(1));
        mGameMode1.setGameModeSelectedListener(this);

        mGameMode2 = (GameModeView) v.findViewById(R.id.mode2);
        mGameMode2.setModel(GameModeFactory.createSurvivalGame(1));
        mGameMode2.setGameModeSelectedListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mode1:
            case R.id.mode2:
                mListener.onLevelChosen((GameModeView) view);
                break;
        }

    }
}
