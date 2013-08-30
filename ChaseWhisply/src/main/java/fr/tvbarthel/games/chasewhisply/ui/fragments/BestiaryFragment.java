package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.bestiary.BestiaryEntry;
import fr.tvbarthel.games.chasewhisply.model.bestiary.BestiaryEntryFactory;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.ui.adapter.BestiaryEntryAdapter;

public class BestiaryFragment extends Fragment {
	private ListView mBestiaryListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_bestiary, container, false);
		mBestiaryListView = ((ListView) v.findViewById(R.id.bestiary_list_view));
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mBestiaryListView.setAdapter(new BestiaryEntryAdapter(getActivity(), new BestiaryEntry[]{
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_EASY_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_BABY_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_GHOST_WITH_HELMET),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_HIDDEN_GHOST),
				BestiaryEntryFactory.createBestiaryEntry(DisplayableItemFactory.TYPE_KING_GHOST)
		}));
	}
}
