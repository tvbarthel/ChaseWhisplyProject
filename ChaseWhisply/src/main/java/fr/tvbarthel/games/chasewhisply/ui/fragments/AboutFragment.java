package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.beta.BetaUtils;
import fr.tvbarthel.games.chasewhisply.beta.SensorDelayDialogFragment;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        ((Button) v.findViewById(R.id.beta_sensor_delay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SensorDelayDialogFragment().show(getFragmentManager(), null);
            }
        });

        final ToggleButton compatibilityMode = ((ToggleButton) v.findViewById(R.id.beta_compatibility_button));
        final SharedPreferences betaSharedPreferences = getActivity().getSharedPreferences(BetaUtils.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = betaSharedPreferences.edit();
        final boolean isCompatibilityModeActivated = betaSharedPreferences.getBoolean(BetaUtils.KEY_COMPATIBILITY_MODE_ACTIVATED, false);
        compatibilityMode.setChecked(isCompatibilityModeActivated);
        compatibilityMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(BetaUtils.KEY_COMPATIBILITY_MODE_ACTIVATED, isChecked);
                editor.commit();
            }
        });

        return v;
    }
}
