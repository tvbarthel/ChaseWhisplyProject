package fr.tvbarthel.games.chasewhisply.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.ui.InventoryCraftListener;
import fr.tvbarthel.games.chasewhisply.ui.customviews.InventoryItemEntryView;

public class InventoryItemEntryAdapter extends ArrayAdapter<InventoryItemEntry> {

    private ArrayList<InventoryItemEntry> mInventoryItemyEntries;
    private InventoryItemEntryView.Listener mDetailRequestListener;
    private InventoryCraftListener mCraftRequestListener;
    private PlayerProfile mPlayerProfile;

    public InventoryItemEntryAdapter(Context context, ArrayList<InventoryItemEntry> inventoryItemyEntries,
                                     InventoryItemEntryView.Listener detailListener,
                                     InventoryCraftListener craftListener, PlayerProfile playerProfile) {
        super(context, R.layout.view_inventory_item_entry, inventoryItemyEntries);
        mInventoryItemyEntries = inventoryItemyEntries;
        mDetailRequestListener = detailListener;
        mCraftRequestListener = craftListener;
        mPlayerProfile = playerProfile;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = getContext();
        final InventoryItemEntry currentInventoryItemEntry = mInventoryItemyEntries.get(position);
        InventoryItemEntryView rowView = (InventoryItemEntryView) convertView;

        if (rowView == null) {
            rowView = new InventoryItemEntryView(context);
        }

        rowView.setModel(currentInventoryItemEntry);
        rowView.setDetailRequestListener(mDetailRequestListener);
        rowView.setCraftRequestListener(mCraftRequestListener);
        if (currentInventoryItemEntry.getRecipe().getMissingResources(mPlayerProfile).size() != 0) {
            rowView.setCraftEnable(false);
        } else {
            rowView.setCraftEnable(true);
        }
        return rowView;
    }
}