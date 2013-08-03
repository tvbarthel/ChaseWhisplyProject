package fr.tvbarthel.games.chasewhisply.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.TutoActivity;

public class TutoFragment extends Fragment {
	public static final String ARG_DRAWABLE = "Tuto_Picture_ARG";
	public static final String ARG_POSITION = "Tuto_Position_ARG";
	private int mDrawableResId;
	private int mPosition;
	private Callbacks mCallbacks;

	public static TutoFragment newInstance(int drawableResId, int position) {
		final TutoFragment f = new TutoFragment();
		final Bundle arguments = new Bundle();
		arguments.putInt(ARG_DRAWABLE, drawableResId);
		arguments.putInt(ARG_POSITION, position);
		f.setArguments(arguments);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof Callbacks)) {
			throw new IllegalArgumentException("Activity must implement Callbacks");
		}
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Bundle arguments = getArguments();
		mDrawableResId = arguments.getInt(ARG_DRAWABLE);
		mPosition = arguments.getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_tuto_page, container, false);

		((ImageView) rootView.findViewById(R.id.tuto_image)).setImageResource(mDrawableResId);

		if (mPosition == TutoActivity.NB_PAGES) {
			final View closeButton = rootView.findViewById(R.id.btn_close_tuto);
			closeButton.setVisibility(View.VISIBLE);
			closeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mCallbacks.onCloseRequested();
				}
			});
		}

		return rootView;
	}

	public interface Callbacks {
		void onCloseRequested();
	}
}
