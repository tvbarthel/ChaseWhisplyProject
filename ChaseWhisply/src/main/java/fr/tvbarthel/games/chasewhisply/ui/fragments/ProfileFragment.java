package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.LevelInformation;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Listener mListener;
    private PlayerProfile mPlayerProfile;

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ((TextView) v.findViewById(R.id.profile_games_played_integer)).setText(String.valueOf(mPlayerProfile.getGamesPlayed()));
        ((TextView) v.findViewById(R.id.profile_targets_killed_integer)).setText(String.valueOf(mPlayerProfile.getTargetsKilled()));
        ((TextView) v.findViewById(R.id.profile_bullets_fired_integer)).setText(String.valueOf(mPlayerProfile.getBulletsFired()));
        ((TextView) v.findViewById(R.id.profile_accuracy_integer)).setText(String.valueOf(mPlayerProfile.getAccuracy()) + "%");
        final LevelInformation levelInformation = mPlayerProfile.getLevelInformation();
        ((TextView) v.findViewById(R.id.profile_level)).setText(String.format(getString(R.string.profile_level), levelInformation.getLevel()));
        ((TextView) v.findViewById(R.id.profile_exp)).setText(String.format(getString(R.string.profile_exp), levelInformation.getExpProgress(), levelInformation.getExpNeededToLevelUp()));
        ((ProgressBar) v.findViewById(R.id.profile_level_progess_bar)).setProgress(levelInformation.getProgressInPercent());

        final int[] clickable = new int[]{
                R.id.profile_bestiary,
                R.id.profile_weapons,
                R.id.profile_inventory,
                R.id.profile_missions
        };
        for (int i : clickable) {
            v.findViewById(i).setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ProfileFragment.Listener) {
            mListener = (ProfileFragment.Listener) activity;
            mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
                    PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ProfileFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null)
            return;
        switch (v.getId()) {
            case R.id.profile_bestiary:
                mListener.onBestiaryRequested();
                break;
            case R.id.profile_inventory:
                mListener.onInventoryRequested();
                break;
            case R.id.profile_weapons:
                mListener.onNotAvailableFeatureRequested();
                break;
            case R.id.profile_missions:
                mListener.onMissionRequested();
                break;
        }
    }

    public interface Listener {
        public void onNotAvailableFeatureRequested();

        public void onBestiaryRequested();

        public void onInventoryRequested();

        public void onMissionRequested();
    }
}
