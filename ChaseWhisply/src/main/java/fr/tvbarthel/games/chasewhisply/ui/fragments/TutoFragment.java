package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutoFragment extends Fragment {
    public static final String ARG_LAYOUT_ID = "Tuto_Layout_Id_ARG";
    private int mLayoutResId;

    public static TutoFragment newInstance(int layoutResId) {
        final TutoFragment f = new TutoFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt(ARG_LAYOUT_ID, layoutResId);
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
    public TutoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        mLayoutResId = arguments.getInt(ARG_LAYOUT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(mLayoutResId, container, false);
        return rootView;
    }

}
