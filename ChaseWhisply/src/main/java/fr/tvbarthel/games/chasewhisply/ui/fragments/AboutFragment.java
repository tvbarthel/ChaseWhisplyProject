package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.beta.BetaUtils;
import fr.tvbarthel.games.chasewhisply.beta.SensorDelayDialogFragment;

public class AboutFragment extends Fragment {

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        v.findViewById(R.id.beta_sensor_delay).setOnClickListener(new View.OnClickListener() {
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

        setVersionName(v);
        initReportButton(v);
        initEmailButton(v);
        return v;
    }

    private void initEmailButton(View v) {
        v.findViewById(R.id.fragment_about_btn_contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUsAnEmail(R.string.email_us_subject_contact_us);
            }
        });
    }

    private void initReportButton(View v) {
        v.findViewById(R.id.fragment_about_btn_report_a_bug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUsAnEmail(R.string.email_us_subject_report_a_bug);
            }
        });
    }

    private void sendUsAnEmail(int subjectResourceId) {
        final String uriString = getString(R.string.email_us_uri,
                Uri.encode(getString(R.string.email_us_email)),
                Uri.encode(getString(subjectResourceId)));
        final Uri mailToUri = Uri.parse(uriString);
        Intent sendToIntent = new Intent(Intent.ACTION_SENDTO);
        sendToIntent.setData(mailToUri);
        startActivity(sendToIntent);
    }

    private void setVersionName(View v) {
        final TextView tvVersionName = (TextView) v.findViewById(R.id.fragment_about_version_name);
        try {
            final PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            tvVersionName.setText(getString(R.string.about_version_detail, packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            tvVersionName.setVisibility(View.GONE);
        }
    }
}
