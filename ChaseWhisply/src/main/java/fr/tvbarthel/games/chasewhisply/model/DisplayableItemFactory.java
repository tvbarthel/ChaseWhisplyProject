package fr.tvbarthel.games.chasewhisply.model;

public class DisplayableItemFactory {
	// Type
	public final static int TYPE_EASY_GHOST = 0x00000001;
	public final static int TYPE_BULLET_HOLE = 0x00000002;

	//Health
	public final static int HEALTH_EASY_GHOST = 1;

	public static TargetableItem createEasyGhost() {
		TargetableItem easyGhost = new TargetableItem();
		easyGhost.setType(TYPE_EASY_GHOST);
		easyGhost.setX(-140);
		easyGhost.setY(-70);
		easyGhost.setHealth(HEALTH_EASY_GHOST);
		return easyGhost;
	}

	public static DisplayableItem createBulletHole() {
		DisplayableItem hole = new DisplayableItem();
		hole.setType(TYPE_BULLET_HOLE);
		return hole;
	}
}
