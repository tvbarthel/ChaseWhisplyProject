package fr.tvbarthel.games.chasewhisply.ui;


import android.app.Activity;
import android.support.v4.app.Fragment;

public class GameScoreFragment extends Fragment {

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
}
