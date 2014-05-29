package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusEntry;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusEntryFactory;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.ui.adapter.BonusEntryAdapter;
import fr.tvbarthel.games.chasewhisply.ui.dialogfragments.SimpleDialogFragment;

public class BonusFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_GAME_MODE = "BonusFragment.Extra.GameMode";
    private Listener mListener;
    private GridView mBonusGridView;
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

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public BonusFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments.containsKey(EXTRA_GAME_MODE)) {
            mGameMode = (GameMode) getArguments().get(EXTRA_GAME_MODE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO beurk, to rework
        mBonusEntryAdapter = new BonusEntryAdapter(getActivity(), new BonusEntry[]{
                BonusEntryFactory.create(InventoryItemInformation.TYPE_STEEL_BULLET, mPlayerProfile.getSteelBulletQuantity()),
                BonusEntryFactory.create(InventoryItemInformation.TYPE_GOLD_BULLET, mPlayerProfile.getGoldBulletQuantity()),
                BonusEntryFactory.create(InventoryItemInformation.TYPE_ONE_SHOT_BULLET, mPlayerProfile.getOneShotBulletQuantity()),
                BonusEntryFactory.create(InventoryItemInformation.TYPE_SPEED_POTION, mPlayerProfile.getSpeedPotionQuantity()),
        });
        mBonusGridView.setAdapter(mBonusEntryAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bonus, container, false);
        mBonusGridView = ((GridView) v.findViewById(R.id.bonus_grid_view));

        v.findViewById(R.id.bonus_start).setOnClickListener(this);
        v.findViewById(R.id.fragment_bonus_btn_help).setOnClickListener(this);
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
                    + " must implement BonusFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.fragment_bonus_btn_help) {
            showHelpMessage();
        } else if (viewId == R.id.bonus_start) {
            startGame();
        }
    }


    private void startGame() {
        final Bonus equippedBonus = mBonusEntryAdapter.getEquippedBonus();
        mGameMode.setBonus(equippedBonus);
        mListener.onGameStartRequest(mGameMode);
    }

    private void showHelpMessage() {
        SimpleDialogFragment.newInstance(R.string.bonus_help_title,
                R.string.bonus_help_message).show(getFragmentManager(), null);
    }

    public interface Listener {
        public void onGameStartRequest(GameMode gameMode);
    }

}
