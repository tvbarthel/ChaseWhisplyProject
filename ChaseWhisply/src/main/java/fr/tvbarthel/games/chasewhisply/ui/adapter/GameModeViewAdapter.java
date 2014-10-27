package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.ui.customviews.GameModeView;


public class GameModeViewAdapter extends RecyclerView.Adapter<GameModeViewAdapter.ViewHolder> {


    private ArrayList<GameMode> mGameModes;
    private PlayerProfile mPlayerProfile;
    public Listener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private GameModeView mGameView;

        public ViewHolder(GameModeView gameView) {
            super(gameView);
            mGameView = gameView;
        }
    }


    public GameModeViewAdapter(ArrayList<GameMode> gameModes, PlayerProfile p, Listener l) {
        mGameModes = gameModes;
        mPlayerProfile = p;
        mListener = l;
    }

    public GameModeViewAdapter(ArrayList<GameMode> gameModes, Listener l) {
        mGameModes = gameModes;
        mPlayerProfile = null;
        mListener = l;
    }


    @Override
    public GameModeViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        GameModeView raw = new GameModeView(viewGroup.getContext());
        return new ViewHolder(raw);
    }

    @Override
    public void onBindViewHolder(GameModeViewAdapter.ViewHolder viewHolder, int i) {
        final GameMode currentGameMode = mGameModes.get(i);

        if (mPlayerProfile == null) {
            viewHolder.mGameView.setModelForLeaderboard(currentGameMode);
        } else {
            viewHolder.mGameView.setModel(currentGameMode);
            viewHolder.mGameView.setGameModeEnabled(currentGameMode.isAvailable(mPlayerProfile));
        }
        viewHolder.mGameView.setGameModeSelectedListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mGameModes.size();
    }

    public interface Listener {
        public void onGameModeSelected(GameModeView view);
    }
}
