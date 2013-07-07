package fr.tvbarthel.games.chasewhisply.model;

public class DisplayableItemFactory {
	// Type
	public final static int TYPE_EASY_GHOST = 0x00000001;

	//Health
	public final static int HEALTH_EASY_GHOST = 1;

	public static TargetableItem createEasyGhost() {
		TargetableItem easyGhost = new TargetableItem();
		easyGhost.setType(TYPE_EASY_GHOST);
		easyGhost.setHealth(HEALTH_EASY_GHOST);
		return easyGhost;
	}
}
