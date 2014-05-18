package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntryFactory;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.ui.InventoryCraftListener;
import fr.tvbarthel.games.chasewhisply.ui.adapter.InventoryItemEntryAdapter;
import fr.tvbarthel.games.chasewhisply.ui.customviews.InventoryItemEntryView;

public class InventoryFragment extends Fragment implements InventoryItemEntryView.Listener, InventoryCraftListener {
    public static final String TAG = "InventoryFragment_TAG";

    private TextView mTextViewCoins;
    private GridView mInventoryGridView;
    private PlayerProfile mPlayerProfile;
    private Listener mListener;
    private InventoryCraftListener mCraftListener;
    private InventoryItemEntryAdapter mInventoryEntryAdapter;

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public InventoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        mInventoryEntryAdapter = new InventoryItemEntryAdapter(getActivity(), new ArrayList<InventoryItemEntry>(),
                this, this, mPlayerProfile);
        mInventoryGridView = ((GridView) v.findViewById(R.id.inventory_grid_view));
        mInventoryGridView.setAdapter(mInventoryEntryAdapter);
        mTextViewCoins = (TextView) v.findViewById(R.id.inventory_old_coins);
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
                    + " must implement InventoryFragment.Listener");
        }

        if (activity instanceof InventoryCraftListener) {
            mCraftListener = (InventoryCraftListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement InventoryCraftListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCraftListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onInventoryItemEntryDetailRequest(InventoryItemEntry inventoryItemEntry) {
        mListener.onInventoryItemEntryDetailRequest(inventoryItemEntry);
    }

    public void loadInformation() {
        final long numberOfCoins = mPlayerProfile.getOldCoinQuantity();
        mTextViewCoins.setText(getResources().getQuantityText(R.plurals.inventory_item_coin_title,
                (int) numberOfCoins) + " : " + String.valueOf(numberOfCoins));
        mInventoryEntryAdapter.clear();
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_BROKEN_HELMET_HORN, mPlayerProfile.getBrokenHelmetHornQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_BABY_DROOL, mPlayerProfile.getBabyDroolQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_GHOST_TEAR, mPlayerProfile.getGhostTearQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_SPEED_POTION, mPlayerProfile.getSpeedPotionQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_KING_CROWN, mPlayerProfile.getKingCrownQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_STEEL_BULLET, mPlayerProfile.getSteelBulletQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_GOLD_BULLET, mPlayerProfile.getGoldBulletQuantity()));
        mInventoryEntryAdapter.add(InventoryItemEntryFactory.create(InventoryItemInformation.TYPE_ONE_SHOT_BULLET, mPlayerProfile.getOneShotBulletQuantity()));
        mInventoryEntryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCraftRequested(InventoryItemEntry inventoryItemEntry) {
        mCraftListener.onCraftRequested(inventoryItemEntry);
    }

    public interface Listener {
        public void onInventoryItemEntryDetailRequest(InventoryItemEntry inventoryItemEntry);
    }
}
