package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusDamage;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusEntry;

public class BonusEntryAdapter extends ArrayAdapter<BonusEntry> {
	private Context mContext;
	private BonusEntry[] mBonusEntries;
	private BonusEntry mEquippedBonus;

	public BonusEntryAdapter(Context context, BonusEntry[] bonusEntries) {
		super(context, R.layout.row_bonus_entry, bonusEntries);
		mContext = context;
		this.mBonusEntries = bonusEntries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BonusEntry currentBonusEntry = mBonusEntries[position];
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_bonus_entry, parent, false);
		final CheckBox equippedCheckBox = ((CheckBox) rowView.findViewById(R.id.row_bonus_entry_equipped));
		final long quantity = currentBonusEntry.getQuantity();
		final Bonus bonus = currentBonusEntry.getBonus();

		if (quantity > 0) {
			equippedCheckBox.setChecked(currentBonusEntry.isEquipped());
			equippedCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isEquipped = ((CheckBox) v).isChecked();
					resetEquippedState();
					currentBonusEntry.setIsEquipped(isEquipped);
					if (isEquipped == true) {
						mEquippedBonus = currentBonusEntry;
					} else {
						mEquippedBonus = null;
					}
					notifyDataSetChanged();
				}
			});
		} else {
			equippedCheckBox.setEnabled(false);
		}

		((TextView) rowView.findViewById(R.id.row_bonus_entry_title)).setText(mContext.getResources().getQuantityString(currentBonusEntry.getTitleResourceId(), 1));
		((TextView) rowView.findViewById(R.id.row_bonus_entry_quantity)).setText(String.valueOf(currentBonusEntry.getQuantity()));

		if (bonus instanceof BonusDamage) {
			((TextView) rowView.findViewById(R.id.row_bonus_entry_effect)).setText(String.format(
					mContext.getString(currentBonusEntry.getEffectResourceId()),
					String.valueOf(((BonusDamage) bonus).getBonusDamage())));
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
