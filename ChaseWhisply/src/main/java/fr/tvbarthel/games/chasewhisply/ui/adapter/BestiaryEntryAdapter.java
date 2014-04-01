package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.bestiary.BestiaryEntry;

public class BestiaryEntryAdapter extends ArrayAdapter<BestiaryEntry> {
    private BestiaryEntry[] mBestiaryEntries;

    public BestiaryEntryAdapter(Context context, BestiaryEntry[] bestiaryEntries) {
        super(context, R.layout.row_bestiary_entry, bestiaryEntries);
        mBestiaryEntries = bestiaryEntries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = getContext();
        final BestiaryEntry currentBestiaryEntry = mBestiaryEntries[position];
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.row_bestiary_entry, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.entryTitle = (TextView) rowView.findViewById(R.id.row_bestiary_entry_title);
            viewHolder.entryImage = (ImageView) rowView.findViewById(R.id.row_bestiary_entry_image);
            viewHolder.health = (TextView) rowView.findViewById(R.id.bestiary_health);
            viewHolder.point = (TextView) rowView.findViewById(R.id.bestiary_point);
            viewHolder.exp = (TextView) rowView.findViewById(R.id.bestiary_exp);
            rowView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        viewHolder.entryTitle.setText(currentBestiaryEntry.getTitleResourceId());
        viewHolder.entryImage.setImageResource(currentBestiaryEntry.getImageResourceId());
        viewHolder.health.setText(currentBestiaryEntry.getHealth());
        viewHolder.point.setText(currentBestiaryEntry.getPointValue());
        viewHolder.exp.setText(currentBestiaryEntry.getExpValue());

        return rowView;
    }

    private final class ViewHolder {
        public TextView entryTitle;
        public ImageView entryImage;
        public TextView health;
        public TextView point;
        public TextView exp;
    }
}
