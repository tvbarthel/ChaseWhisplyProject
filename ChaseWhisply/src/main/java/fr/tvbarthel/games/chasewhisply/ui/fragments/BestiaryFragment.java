package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.bestiary.BestiaryEntry;
import fr.tvbarthel.games.chasewhisply.model.bestiary.BestiaryEntryFactory;
import fr.tvbarthel.games.chasewhisply.ui.adapter.BestiaryEntryAdapter;

public class BestiaryFragment extends Fragment {
    private GridView mBestiaryGridView;


    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public BestiaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bestiary, container, false);
        mBestiaryGridView = ((GridView) v.findViewById(R.id.bestiary_grid_view));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBestiaryGridView.setAdapter(new BestiaryEntryAdapter(getActivity(), new BestiaryEntry[]{
                BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_EASY_GHOST),
                BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_BLOND_GHOST),
                BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_BABY_GHOST),
                BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_GHOST_WITH_HELMET),
                BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_HIDDEN_GHOST),
                BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_KING_GHOST)

        }));
    }
}
