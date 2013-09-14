package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntryFactory;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.ui.adapter.InventoryItemEntryAdapter;

public class InventoryFragment extends Fragment implements InventoryItemEntryAdapter.Listener {
	public static final String TAG = "InventoryFragment_TAG";

	private ListView mInventoryListView;
	private PlayerProfile mPlayerProfile;
	private Listener mListener;
	private InventoryItemEntryAdapter mInventoryEntryAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_inventory, container, false);
		mInventoryEntryAdapter = new InventoryItemEntryAdapter(getActivity(), new ArrayList<InventoryItemEntry>(), this);
		mInventoryListView = ((ListView) v.findViewById(R.id.inventory_list_view));
		mInventoryListView.setAdapter(mInventoryEntryAdapter);
		loadInformation();
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof InventoryFragment.Listener) {
			mListener = (InventoryFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(getActivity().getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet InventoryFragment.Listener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCraftRequested(InventoryItemEntry inventoryItemEntry) {
		mListener.onCraftRequested(inventoryItemEntry);
	}

	public void loadInformation() {
		mInventoryEntryAdapter.clear();
		mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_COIN, mPlayerProfile.getOldCoinQuantity()));
		mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_BROKEN_HELMET_HORN, mPlayerProfile.getBrokenHelmetHornQuantity()));
		mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_BABY_DROOL, mPlayerProfile.getBabyDroolQuantity()));
		mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_KING_CROWN, mPlayerProfile.getKingCrownQuantity()));
		mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_STEEL_BULLET, mPlayerProfile.getSteelBulletQuantity()));
		mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_GOLD_BULLET, mPlayerProfile.getGoldBulletQuantity()));
		mInventoryEntryAdapter.notifyDataSetChanged();
	}

	public interface Listener {
		public void onCraftRequested(InventoryItemEntry inventoryItemEntry);
	}
}
