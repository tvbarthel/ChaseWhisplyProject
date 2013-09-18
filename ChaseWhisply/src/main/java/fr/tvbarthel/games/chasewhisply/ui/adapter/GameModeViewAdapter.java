package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.ui.GameModeView;


public class GameModeViewAdapter extends ArrayAdapter<GameMode> {


	private Context mContext;
	private ArrayList<GameMode> mGameModes;
	private PlayerProfile mPlayerProfile;
	public Listener mListener;


	public GameModeViewAdapter(Context context, ArrayList<GameMode> gameModes, PlayerProfile p, Listener l) {
		super(context, R.layout.row_gamemode_entry, gameModes);
		mContext = context;
		mGameModes = gameModes;
		mPlayerProfile = p;
		mListener = l;
	}

	public GameModeViewAdapter(Context context, ArrayList<GameMode> gameModes, Listener l) {
		super(context, R.layout.row_gamemode_entry, gameModes);
		mContext = context;
		mGameModes = gameModes;
		mPlayerProfile = null;
		mListener = l;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final GameMode currentGameMode = mGameModes.get(position);
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = convertView;

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.row_gamemode_entry, parent, false);
		}

		GameModeView gameModeView = (GameModeView) rowView.findViewById(R.id.gamemodeview);
		if (mPlayerProfile == null) {
			gameModeView.setModelForLeaderboard(currentGameMode);
		} else {
			gameModeView.setModel(currentGameMode);
			gameModeView.setGameModeEnabled(currentGameMode.isAvailable(mPlayerProfile));
		}
		gameModeView.setGameModeSelectedListener(mListener);

		return rowView;

	}

	public interface Listener {
		public void onGameModeSelected(GameModeView view);
	}
}
