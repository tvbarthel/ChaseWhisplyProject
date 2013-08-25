package fr.tvbarthel.games.chasewhisply.model;


import fr.tvbarthel.games.chasewhisply.R;

public class BestiaryEntryFactory {

	public static BestiaryEntry createBestiaryEntry(int ghostType) {
		BestiaryEntry bestiaryEntry = new BestiaryEntry();
		switch (ghostType) {
			case DisplayableItemFactory.TYPE_GHOST_WITH_HELMET:
				bestiaryEntry.setTargetableItem(DisplayableItemFactory.createGhostWithHelmet());
				bestiaryEntry.setImageResourceId(R.drawable.ghost_with_helmet_5);
				bestiaryEntry.setTitleResourceId(R.string.bestiary_ghost_with_helmet_title);
				break;
			case DisplayableItemFactory.TYPE_BABY_GHOST:
				bestiaryEntry.setTargetableItem(DisplayableItemFactory.createBabyGhost());
				bestiaryEntry.setImageResourceId(R.drawable.baby_ghost);
				bestiaryEntry.setTitleResourceId(R.string.bestiary_baby_ghost_title);
				break;

			case  DisplayableItemFactory.TYPE_HIDDEN_GHOST:
				bestiaryEntry.setTargetableItem(DisplayableItemFactory.createHiddenGhost());
				bestiaryEntry.setImageResourceId(R.drawable.hidden_ghost);
				bestiaryEntry.setTitleResourceId(R.string.bestiary_hidden_ghost_title);
				break;

			default:
				bestiaryEntry.setTargetableItem(DisplayableItemFactory.createEasyGhost());
				bestiaryEntry.setImageResourceId(R.drawable.ghost);
				bestiaryEntry.setTitleResourceId(R.string.bestiary_easy_ghost_title);
				break;
		}
		return bestiaryEntry;
	}
}
