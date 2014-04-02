package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;


public class GameModeDetailsAdapter extends ArrayAdapter<GameMode> {

    private ArrayList<GameMode> mGameModes;
    private PlayerProfile mPlayerProfile;
    public Listener mListener;

    public GameModeDetailsAdapter(Context context, ArrayList<GameMode> gameModes, Listener l, PlayerProfile p) {
        super(context, R.layout.row_mode_details_entry, gameModes);
        mListener = l;
        mGameModes = gameModes;
        mPlayerProfile = p;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = getContext();
        final Resources res = context.getResources();
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final GameMode currentGameMode = mGameModes.get(position);
        final String[] grades = res.getStringArray(R.array.ranks_array_full);
        final int currentRank = mPlayerProfile.getRankByGameMode(currentGameMode);


        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.row_mode_details_entry, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.row_game_details_title);
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.row_game_details_icon);
            viewHolder.rank = (TextView) rowView.findViewById(R.id.row_game_details_rank);
            rowView.setTag(viewHolder);
        }


        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.title.setText(currentGameMode.getTitle());
        viewHolder.icon.setBackgroundResource(currentGameMode.getImage());
        viewHolder.rank.setText(grades[currentRank]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGameModeSelected(currentGameMode);
            }
        });
        return rowView;
    }

    private final class ViewHolder {
        public TextView title;
        public ImageView icon;
        public TextView rank;
    }

    public interface Listener {
        public void onGameModeSelected(GameMode gameMode);
    }
}
