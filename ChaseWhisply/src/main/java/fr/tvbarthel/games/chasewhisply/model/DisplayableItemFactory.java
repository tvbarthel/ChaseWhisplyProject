package fr.tvbarthel.games.chasewhisply.model;

public class DisplayableItemFactory {
	// Type
	public final static int TYPE_EASY_GHOST = 0x00000001;
	public final static int TYPE_BULLET_HOLE = 0x00000002;

	private static final int DEFAULT_X_MIN_IN_DEGREE = -170;
	private static final int DEFAULT_X_MAX_IN_DEGREE = 170;
	private static final int DEFAULT_Y_MIN_IN_DEGREE = -80;
	private static final int DEFAULT_Y_MAX_IN_DEGREE = -50;

	//Health
	public final static int HEALTH_EASY_GHOST = 1;

	public static TargetableItem createEasyGhost() {
		TargetableItem easyGhost = new TargetableItem();
		easyGhost.setType(TYPE_EASY_GHOST);
		final int randomX = randomize(DEFAULT_X_MIN_IN_DEGREE, DEFAULT_X_MAX_IN_DEGREE);
		final int randomY = randomize(DEFAULT_Y_MIN_IN_DEGREE, DEFAULT_Y_MAX_IN_DEGREE);
		easyGhost.setX(randomX);
		easyGhost.setY(randomY);
		easyGhost.setHealth(HEALTH_EASY_GHOST);
		return easyGhost;
	}

	public static TargetableItem createEasyGhost(int xmin, int xmax, int ymin, int ymax) {
		TargetableItem easyGhost = new TargetableItem();
		easyGhost.setType(TYPE_EASY_GHOST);
		final int randomX = randomize(xmin, xmax);
		final int randomY = randomize(ymin, ymax);
		easyGhost.setX(randomX);
		easyGhost.setY(randomY);
		easyGhost.setHealth(HEALTH_EASY_GHOST);
		return easyGhost;
	}

	public static DisplayableItem createBulletHole() {
		DisplayableItem hole = new DisplayableItem();
		hole.setType(TYPE_BULLET_HOLE);
		return hole;
	}

	private static int randomize(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
}
