package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;

public class InventoryItemEntryAdapter extends ArrayAdapter<InventoryItemEntry> {
	private Context mContext;
	private InventoryItemEntry[] mInventoryItemyEntries;

	public InventoryItemEntryAdapter(Context context, InventoryItemEntry[] inventoryItemyEntries) {
		super(context, R.layout.row_inventory_item_entry, inventoryItemyEntries);
		mContext = context;
		mInventoryItemyEntries = inventoryItemyEntries;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final InventoryItemEntry currentInventoryItemEntry = mInventoryItemyEntries[position];
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_inventory_item_entry, parent, false);
		((TextView) rowView.findViewById(R.id.row_inventory_item_entry_title)).setText(currentInventoryItemEntry.getTitleResourceId());
		((TextView) rowView.findViewById(R.id.row_inventory_item_entry_description)).setText(currentInventoryItemEntry.getDescriptionResourceId());
		((TextView) rowView.findViewById(R.id.row_inventory_item_entry_quantity)).setText(String.valueOf(currentInventoryItemEntry.getQuantityAvailable()));

		String stringDroppedBy = mContext.getString(R.string.inventory_item_can_t_be_dropped);
		final HashMap<Integer, Integer> lootsAndPercents = currentInventoryItemEntry.getDroppedBy().getMonstersAndPercents();
		if (lootsAndPercents.size() != 0) {
			stringDroppedBy = "";
			for (Map.Entry<Integer, Integer> entry : lootsAndPercents.entrySet()) {
				stringDroppedBy += mContext.getString(entry.getKey()) + " (" + String.valueOf(entry.getValue()) + "%), ";
			}
			stringDroppedBy = stringDroppedBy.substring(0, stringDroppedBy.length() - 2);
		}
		((TextView) rowView.findViewById(R.id.row_inventory_item_entry_dropped_by)).setText(stringDroppedBy);

		String stringRecipe = mContext.getString(R.string.inventory_item_can_t_be_crafted);
		final HashMap<Integer, Integer> ingredientsAndQuantites = currentInventoryItemEntry.getRecipe().getIngredientsAndQuantities();
		if (ingredientsAndQuantites.size() != 0) {
			stringRecipe = "";
			for (Map.Entry<Integer, Integer> entry : ingredientsAndQuantites.entrySet()) {
				stringRecipe += String.valueOf(entry.getValue()) + " x " + mContext.getString(entry.getKey()) + ", ";
			}
			stringRecipe = stringRecipe.substring(0, stringRecipe.length() - 2);
		}
		((TextView) rowView.findViewById(R.id.row_inventory_item_entry_recipe)).setText(stringRecipe);
		return rowView;
	}
}