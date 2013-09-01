package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;

public class InventoryItemEntryAdapter extends ArrayAdapter<InventoryItemEntry> {
	private Context mContext;
	private ArrayList<InventoryItemEntry> mInventoryItemyEntries;
	private Listener mListener;

	public InventoryItemEntryAdapter(Context context, ArrayList<InventoryItemEntry> inventoryItemyEntries, Listener listener) {
		super(context, R.layout.row_inventory_item_entry, inventoryItemyEntries);
		mContext = context;
		mInventoryItemyEntries = inventoryItemyEntries;
		mListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final InventoryItemEntry currentInventoryItemEntry = mInventoryItemyEntries.get(position);
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

		final HashMap<InventoryItemInformation, Integer> ingredientsAndQuantites = currentInventoryItemEntry.getRecipe().getIngredientsAndQuantities();
		if (ingredientsAndQuantites.size() == 0) {
			(rowView.findViewById(R.id.row_inventory_item_entry_craft_action)).setEnabled(false);
		}
		((TextView) rowView.findViewById(R.id.row_inventory_item_entry_recipe)).setText(currentInventoryItemEntry.getRecipe().toString(getContext()));

		((ImageButton) rowView.findViewById(R.id.row_inventory_item_entry_craft_action)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onCraftRequested(currentInventoryItemEntry);
			}
		});

		return rowView;
	}

	public interface Listener {
		public void onCraftRequested(InventoryItemEntry inventoryItemEntry);
	}
}