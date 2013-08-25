package fr.tvbarthel.games.chasewhisply.model;

public class DisplayableItemFactory {
	// Type
	public final static int TYPE_EASY_GHOST = 0x00000001;
	public final static int TYPE_BULLET_HOLE = 0x00000002;
	public final static int TYPE_BABY_GHOST = 0x00000003;
	public final static int TYPE_GHOST_WITH_HELMET = 0x00000004;
	public final static int TYPE_HIDDEN_GHOST = 0x00000005;
	public final static int TYPE_KING_GHOST = 0x00000006;

	private static final int DEFAULT_X_MIN_IN_DEGREE = -170;
	private static final int DEFAULT_X_MAX_IN_DEGREE = 170;
	private static final int DEFAULT_Y_MIN_IN_DEGREE = -80;
	private static final int DEFAULT_Y_MAX_IN_DEGREE = -50;

	//Health
	public final static int HEALTH_EASY_GHOST = 1;
	public final static int HEALTH_BABY_GHOST = 1;
	public final static int HEALTH_GHOST_WITH_HELMET = 5;
	public final static int HEALTH_HIDDEN_GHOST = 1;
	public final static int HEALTH_KING_GHOST = 1;

	//Base Point
	public final static int BASE_POINT_EAST_GHOST = 1;
	public final static int BASE_POINT_BABY_GHOST = 2;
	public final static int BASE_POINT_GHOST_WITH_HELMET = 10;
	public final static int BASE_POINT_HIDDEN_GHOST = 2;
	public final static int BASE_POINT_KING_GHOST = 0;

	//Exp Point
	public final static int EXP_POINT_EASY_GHOST = 2;
	public final static int EXP_POINT_BABY_GHOST = 4;
	public final static int EXP_POINT_GHOST_WITH_HELMET = 10;
	public final static int EXP_POINT_HIDDEN_GHOST = 5;
	public final static int EXP_POINT_KING_GHOST = 0;


	public static TargetableItem createGhostWithRandomCoordinates(int ghostType) {
		return createGhostWithRandomCoordinates(ghostType, DEFAULT_X_MIN_IN_DEGREE,
				DEFAULT_X_MAX_IN_DEGREE, DEFAULT_Y_MIN_IN_DEGREE, DEFAULT_Y_MAX_IN_DEGREE);
	}

	public static TargetableItem createGhostWithRandomCoordinates(int ghostType, int xMin, int xMax, int yMin, int yMax) {
		TargetableItem targetableItem = createEasyGhost();
		switch (ghostType) {

			case TYPE_BABY_GHOST:
				targetableItem = createBabyGhost();
				break;

			case TYPE_GHOST_WITH_HELMET:
				targetableItem = createGhostWithHelmet();
				break;

			case TYPE_HIDDEN_GHOST:
				targetableItem = createHiddenGhost();
				break;

			case TYPE_KING_GHOST:
				targetableItem = createKingGhost();
				break;
		}
		targetableItem.setRandomCoordinates(xMin, xMax, yMin, yMax);
		return targetableItem;
	}


	public static TargetableItem createGhostWithHelmet() {
		return createTargetableItem(TYPE_GHOST_WITH_HELMET,
				HEALTH_GHOST_WITH_HELMET,
				BASE_POINT_GHOST_WITH_HELMET,
				EXP_POINT_GHOST_WITH_HELMET);
	}

	public static TargetableItem createEasyGhost() {
		return createTargetableItem(TYPE_EASY_GHOST,
				HEALTH_EASY_GHOST,
				BASE_POINT_EAST_GHOST,
				EXP_POINT_EASY_GHOST);
	}

	public static TargetableItem createBabyGhost() {
		return createTargetableItem(TYPE_BABY_GHOST,
				HEALTH_BABY_GHOST,
				BASE_POINT_BABY_GHOST,
				EXP_POINT_BABY_GHOST);
	}

	public static TargetableItem createHiddenGhost() {
		return createTargetableItem(TYPE_HIDDEN_GHOST,
				HEALTH_HIDDEN_GHOST,
				BASE_POINT_HIDDEN_GHOST,
				EXP_POINT_HIDDEN_GHOST);
	}

	public static TargetableItem createKingGhost() {
		return createTargetableItem(TYPE_KING_GHOST,
				HEALTH_KING_GHOST,
				BASE_POINT_KING_GHOST,
				EXP_POINT_KING_GHOST);
	}


	private static TargetableItem createTargetableItem(int type, int health, int basePoint, int expPoint) {
		TargetableItem targetableItem = new TargetableItem();
		targetableItem.setType(type);
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
