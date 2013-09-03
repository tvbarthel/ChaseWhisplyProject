package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.tvbarthel.games.chasewhisply.R;
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
		return v;
	}
}
