package fr.tvbarthel.games.chasewhisply.model;

public class DisplayableItemFactory {
	// Type
	public final static int TYPE_EASY_GHOST = 0x00000001;
	public final static int TYPE_BULLET_HOLE = 0x00000002;
	public final static int TYPE_BABY_GHOST = 0x00000003;
	public final static int TYPE_GHOST_WITH_HELMET = 0x00000004;

	private static final int DEFAULT_X_MIN_IN_DEGREE = -170;
	private static final int DEFAULT_X_MAX_IN_DEGREE = 170;
	private static final int DEFAULT_Y_MIN_IN_DEGREE = -80;
	private static final int DEFAULT_Y_MAX_IN_DEGREE = -50;

	//Health
	public final static int HEALTH_EASY_GHOST = 1;
	public final static int HEALTH_BABY_GHOST = 1;
	public final static int HEALTH_GHOST_WITH_HELMET = 5;

	//Base Point
	public final static int BASE_POINT_EAST_GHOST = 1;
	public final static int BASE_POINT_BABY_GHOST = 2;
	public final static int BASE_POINT_GHOST_WITH_HELMET = 10;

	//Exp Point
	public final static int EXP_POINT_EASY_GHOST = 1;
	public final static int EXP_POINT_BABY_GHOST = 2;
	public final static int EXP_POINT_GHOST_WITH_HELMET = 5;

	public static TargetableItem createEasyGhost() {
		TargetableItem easyGhost = new TargetableItem();
		easyGhost.setType(TYPE_EASY_GHOST);
		final int randomX = MathUtils.randomize(DEFAULT_X_MIN_IN_DEGREE, DEFAULT_X_MAX_IN_DEGREE);
		final int randomY = MathUtils.randomize(DEFAULT_Y_MIN_IN_DEGREE, DEFAULT_Y_MAX_IN_DEGREE);
		easyGhost.setX(randomX);
		easyGhost.setY(randomY);
		easyGhost.setHealth(HEALTH_EASY_GHOST);
		return easyGhost;
	}

	public static TargetableItem createGhostWithHelmet(int xmin, int xmax, int ymin, int ymax) {
		return createTargetableItem(TYPE_GHOST_WITH_HELMET,
				HEALTH_GHOST_WITH_HELMET,
				BASE_POINT_GHOST_WITH_HELMET,
				EXP_POINT_GHOST_WITH_HELMET,
				xmin, xmax, ymin, ymax);
	}

	public static TargetableItem createEasyGhost(int xmin, int xmax, int ymin, int ymax) {
		return createTargetableItem(TYPE_EASY_GHOST,
				HEALTH_EASY_GHOST,
				BASE_POINT_EAST_GHOST,
				EXP_POINT_EASY_GHOST,
				xmin, xmax, ymin, ymax);
	}

	public static TargetableItem createBabyGhost(int xmin, int xmax, int ymin, int ymax) {
		return createTargetableItem(TYPE_BABY_GHOST,
				HEALTH_BABY_GHOST,
				BASE_POINT_BABY_GHOST,
				EXP_POINT_BABY_GHOST,
				xmin, xmax, ymin, ymax);
	}

	private static TargetableItem createTargetableItem(int type, int health, int basePoint, int expPoint, int xmin, int xmax, int ymin, int ymax) {
		TargetableItem targetableItem = new TargetableItem();
		targetableItem.setType(type);
		final int randomX = MathUtils.randomize(xmin, xmax);
		final int randomY = MathUtils.randomize(ymin, ymax);
		targetableItem.setX(randomX);
		targetableItem.setY(randomY);
		targetableItem.setHealth(health);
		targetableItem.setBasePoint(basePoint);
		targetableItem.setExpPoint(expPoint);
		return targetableItem;
	}

	public static DisplayableItem createBulletHole() {
		DisplayableItem hole = new DisplayableItem();
		hole.setType(TYPE_BULLET_HOLE);
		return hole;
	}
}
