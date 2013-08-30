package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntryFactory;
import fr.tvbarthel.games.chasewhisply.ui.InventoryItemEntryAdapter;

public class InventoryFragment extends Fragment {

	private ListView mInventoryListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_inventory, container, false);
		mInventoryListView = ((ListView) v.findViewById(R.id.inventory_list_view));
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final PlayerProfile playerProfile = new PlayerProfile(getActivity().getSharedPreferences(
				PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		mInventoryListView.setAdapter(new InventoryItemEntryAdapter(getActivity(), new InventoryItemEntry[]{
				InventoryItemEntryFactory.createInventoryEntry(InventoryItemEntry.TYPE_COIN, playerProfile.getOldCoinQuantity()),
				InventoryItemEntryFactory.createInventoryEntry(InventoryItemEntry.TYPE_BROKEN_HELMET_HORN, playerProfile.getBrokenHelmetHornQuantity()),
				InventoryItemEntryFactory.createInventoryEntry(InventoryItemEntry.TYPE_BABY_DROOL, playerProfile.getBabyDroolQuantity()),
				InventoryItemEntryFactory.createInventoryEntry(InventoryItemEntry.TYPE_KING_CROWN, playerProfile.getKingCrownQuantity()),
				InventoryItemEntryFactory.createInventoryEntry(InventoryItemEntry.TYPE_STEEL_BULLET, playerProfile.getSteelBulletQuantity()),
				InventoryItemEntryFactory.createInventoryEntry(InventoryItemEntry.TYPE_GOLD_BULLET, playerProfile.getGoldBulletQuantity())
		}));
	}
}
