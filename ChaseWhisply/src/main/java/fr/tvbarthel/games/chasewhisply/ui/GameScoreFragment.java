package fr.tvbarthel.games.chasewhisply.ui;


public class GameScoreFragment {

    //interface
    public interface Listener {
        public void onReplayRequested();

        public void onSkipRequested();

        public void onHomeRequested();

        public void onPushScoreRequested(int score);
    }

    private Listener mListener = null;
}
