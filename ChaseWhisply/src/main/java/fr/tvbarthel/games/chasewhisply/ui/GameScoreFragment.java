package fr.tvbarthel.games.chasewhisply.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.R;

public class GameScoreFragment extends Fragment implements View.OnClickListener {


    //interface
    public interface Listener {
        public void onReplayRequested();

        public void onSkipRequested();

        public void onHomeRequested();

        public void onPushScoreRequested(int score);
    }

    private Listener mListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GameScoreFragment.Listener) {
            mListener = (GameScoreFragment.Listener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet GameScoreFragment.Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        final int[] clickable = new int[]{
                R.id.score_button_replay,
                R.id.score_button_home,
                R.id.score_button_skip
        };
        for (int i : clickable) {
            v.findViewById(i).setOnClickListener(this);
        }
        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.score_button_home:
                mListener.onHomeRequested();
                break;
            case R.id.score_button_skip:
                mListener.onSkipRequested();
                break;
            case R.id.score_button_replay:
                mListener.onReplayRequested();
                break;
        }

    }

}
