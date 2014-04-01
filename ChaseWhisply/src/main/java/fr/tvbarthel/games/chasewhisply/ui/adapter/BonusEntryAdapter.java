package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusDamage;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusEntry;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusSpeed;

public class BonusEntryAdapter extends ArrayAdapter<BonusEntry> {
    private BonusEntry[] mBonusEntries;
    private BonusEntry mEquippedBonus;

    public BonusEntryAdapter(Context context, BonusEntry[] bonusEntries) {
        super(context, R.layout.row_bonus_entry, bonusEntries);
        this.mBonusEntries = bonusEntries;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = getContext();
        final BonusEntry currentBonusEntry = mBonusEntries[position];
        final long quantity = currentBonusEntry.getQuantity();
        final Bonus bonus = currentBonusEntry.getBonus();
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final boolean isEquipped = currentBonusEntry.isEquipped();


        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.row_bonus_entry, parent, false);
        }
        final CheckBox equippedCheckBox = ((CheckBox) rowView.findViewById(R.id.row_bonus_entry_equipped));
        equippedCheckBox.setChecked(isEquipped);

        //display card
        if (isEquipped) {
            rowView.setBackgroundResource(R.drawable.card_shadow_pressed);
        } else {
            rowView.setBackgroundResource(R.drawable.card_shadow_base);
        }


        if (quantity > 0) {
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean afterClickState = !currentBonusEntry.isEquipped();
                    resetEquippedState();
                    if (afterClickState == true) {
                        mEquippedBonus = currentBonusEntry;
                    } else {
                        mEquippedBonus = null;
                    }
                    currentBonusEntry.setIsEquipped(afterClickState);
                    notifyDataSetChanged();
                }
            });
        } else {
            rowView.setBackgroundResource(R.drawable.card_shadow_disable);
            equippedCheckBox.setEnabled(false);
        }

        ((TextView) rowView.findViewById(R.id.row_bonus_entry_title)).setText(context.getResources().getQuantityString(currentBonusEntry.getTitleResourceId(), 1));
        ((TextView) rowView.findViewById(R.id.row_bonus_entry_quantity)).setText("x" + String.valueOf(currentBonusEntry.getQuantity()));
        ((ImageView) rowView.findViewById(R.id.row_bonus_entry_image)).setImageResource(currentBonusEntry.getImageResourceId());

        if (bonus instanceof BonusDamage) {
            ((TextView) rowView.findViewById(R.id.row_bonus_entry_effect)).setText(String.format(
                    context.getString(currentBonusEntry.getEffectResourceId()),
                    String.valueOf(((BonusDamage) bonus).getBonusDamage())));
        } else if (bonus instanceof BonusSpeed) {
            ((TextView) rowView.findViewById(R.id.row_bonus_entry_effect)).setText(String.format(
                    context.getString(currentBonusEntry.getEffectResourceId()),
                    String.valueOf(((BonusSpeed) bonus).getSpeedReduction())));
        }

        return rowView;
    }

    private void resetEquippedState() {
        for (BonusEntry entry : mBonusEntries) {
            entry.setIsEquipped(false);
        }
    }

    public Bonus getEquippedBonus() {
        return mEquippedBonus == null ? new Bonus.DummyBonus() : mEquippedBonus.getBonus();
    }
}
