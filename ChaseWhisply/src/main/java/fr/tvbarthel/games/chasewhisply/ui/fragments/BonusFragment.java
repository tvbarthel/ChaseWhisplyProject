package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusEntry;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusEntryFactory;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.ui.adapter.BonusEntryAdapter;

public class BonusFragment extends Fragment {
	public static final String EXTRA_GAME_MODE = "BonusFragment.Extra.GameMode";
	private Listener mListener;
	private ListView mBonusListView;
	private BonusEntryAdapter mBonusEntryAdapter;
	private PlayerProfile mPlayerProfile;
	private GameMode mGameMode;

	public static BonusFragment newInstance(GameMode gameMode) {
		final BonusFragment f = new BonusFragment();
		final Bundle arguments = new Bundle();
		arguments.putParcelable(EXTRA_GAME_MODE, gameMode);
		f.setArguments(arguments);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Bundle arguments = getArguments();
		if (arguments.containsKey(EXTRA_GAME_MODE)) {
			mGameMode = (GameMode) getArguments().get(EXTRA_GAME_MODE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bonus, container, false);
		mBonusListView = ((ListView) v.findViewById(R.id.bonus_list_view));

		if (mBonusEntryAdapter == null) {
			mBonusEntryAdapter = new BonusEntryAdapter(getActivity(), new BonusEntry[]{
					BonusEntryFactory.create(InventoryItemInformation.TYPE_STEEL_BULLET, mPlayerProfile.getSteelBulletQuantity()),
					BonusEntryFactory.create(InventoryItemInformation.TYPE_GOLD_BULLET, mPlayerProfile.getGoldBulletQuantity())});
		}

		mBonusListView.setAdapter(mBonusEntryAdapter);

		(v.findViewById(R.id.bonus_start)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Bonus equippedBonus = mBonusEntryAdapter.getEquippedBonus();
				mGameMode.setBonus(equippedBonus);
				mListener.onGameStartRequest(mGameMode);
			}
		});

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof BonusFragment.Listener) {
			mListener = (BonusFragment.Listener) activity;
			mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
					PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet BonusFragment.Listener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface Listener {
		public void onGameStartRequest(GameMode gameMode);
	}

}
