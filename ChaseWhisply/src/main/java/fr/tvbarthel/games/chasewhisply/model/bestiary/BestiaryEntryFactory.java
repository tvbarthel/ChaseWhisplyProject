package fr.tvbarthel.games.chasewhisply.model.bestiary;


import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;

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

            case DisplayableItemFactory.TYPE_HIDDEN_GHOST:
                bestiaryEntry.setTargetableItem(DisplayableItemFactory.createHiddenGhost());
                bestiaryEntry.setImageResourceId(R.drawable.hidden_ghost);
                bestiaryEntry.setTitleResourceId(R.string.bestiary_hidden_ghost_title);
                break;

            case DisplayableItemFactory.TYPE_KING_GHOST:
                bestiaryEntry.setTargetableItem(DisplayableItemFactory.createKingGhost());
                bestiaryEntry.setImageResourceId(R.drawable.king_ghost);
                bestiaryEntry.setTitleResourceId(R.string.bestiary_king_ghost_title);
                break;

            case DisplayableItemFactory.TYPE_BLOND_GHOST:
                bestiaryEntry.setTargetableItem(DisplayableItemFactory.createBlondGhost());
                bestiaryEntry.setImageResourceId(R.drawable.blond_ghost_in_tears);
                bestiaryEntry.setTitleResourceId(R.string.bestiary_blond_ghost_title);
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
