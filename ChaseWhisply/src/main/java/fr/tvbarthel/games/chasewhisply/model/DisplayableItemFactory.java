package fr.tvbarthel.games.chasewhisply.model;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;

public class DisplayableItemFactory {
	// Type
	public final static int TYPE_EASY_GHOST = 0x00000001;
	public final static int TYPE_BULLET_HOLE = 0x00000002;
	public final static int TYPE_BABY_GHOST = 0x00000003;
	public final static int TYPE_GHOST_WITH_HELMET = 0x00000004;
	public final static int TYPE_HIDDEN_GHOST = 0x00000005;
	public final static int TYPE_KING_GHOST = 0x00000006;

	//World Boundaries
	private static final int MAX_X_IN_DEGREE = 175;
	private static final int MIN_X_IN_DEGREE = -175;
	private static final int MAX_Y_IN_DEGREE = 175;
	private static final int MIN_Y_IN_DEGREE = -175;

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
		targetableItem.setRandomCoordinates(
				Math.max(MIN_X_IN_DEGREE, xMin),
				Math.min(MAX_X_IN_DEGREE, xMax),
				Math.max(MIN_Y_IN_DEGREE, yMin),
				Math.min(MAX_Y_IN_DEGREE, yMax));
		return targetableItem;
	}


	public static TargetableItem createGhostWithHelmet() {
		final int dropDraft = MathUtils.randomize(0, 100);
		final ArrayList<Integer> drops = new ArrayList<Integer>();
		final TargetableItem ghostWithHelmet = createTargetableItem(TYPE_GHOST_WITH_HELMET,
				HEALTH_GHOST_WITH_HELMET,
				BASE_POINT_GHOST_WITH_HELMET,
				EXP_POINT_GHOST_WITH_HELMET);

		if (dropDraft < InventoryItemEntry.DROP_RATE_BROKEN_HELMET_HORN) {
			drops.add(InventoryItemEntry.TYPE_BROKEN_HELMET_HORN);
		}

		if (dropDraft < InventoryItemEntry.DROP_RATE_COIN * 4) {
			drops.add(InventoryItemEntry.TYPE_COIN);
		}

		ghostWithHelmet.setDrop(drops);
		return ghostWithHelmet;
	}

	public static TargetableItem createEasyGhost() {
		final int dropDraft = MathUtils.randomize(0, 100);
		final ArrayList<Integer> drops = new ArrayList<Integer>();
		final TargetableItem easyGhost = createTargetableItem(TYPE_EASY_GHOST,
				HEALTH_EASY_GHOST,
				BASE_POINT_EAST_GHOST,
				EXP_POINT_EASY_GHOST);

		if (dropDraft < InventoryItemEntry.DROP_RATE_COIN) {
			drops.add(InventoryItemEntry.TYPE_COIN);
		}

		easyGhost.setDrop(drops);
		return easyGhost;
	}

	public static TargetableItem createBabyGhost() {
		final int dropDraft = MathUtils.randomize(0, 100);
		final ArrayList<Integer> drops = new ArrayList<Integer>();
		final TargetableItem babyGhost = createTargetableItem(TYPE_BABY_GHOST,
				HEALTH_BABY_GHOST,
				BASE_POINT_BABY_GHOST,
				EXP_POINT_BABY_GHOST);

		if (dropDraft < InventoryItemEntry.DROP_RATE_BABY_DROOL) {
			drops.add(InventoryItemEntry.TYPE_BABY_DROOL);
		}

		if (dropDraft < InventoryItemEntry.DROP_RATE_COIN * 2) {
			drops.add(InventoryItemEntry.TYPE_COIN);
		}

		babyGhost.setDrop(drops);
		return babyGhost;
	}

	public static TargetableItem createHiddenGhost() {
		final int dropDraft = MathUtils.randomize(0, 100);
		final ArrayList<Integer> drops = new ArrayList<Integer>();
		final TargetableItem hiddenGhost = createTargetableItem(TYPE_HIDDEN_GHOST,
				HEALTH_HIDDEN_GHOST,
				BASE_POINT_HIDDEN_GHOST,
				EXP_POINT_HIDDEN_GHOST);

		if (dropDraft < InventoryItemEntry.DROP_RATE_COIN) {
			drops.add(InventoryItemEntry.TYPE_COIN);
		}

		hiddenGhost.setDrop(drops);
		return hiddenGhost;
	}

	public static TargetableItem createKingGhost() {
		final int dropDraft = MathUtils.randomize(0, 100);
		final ArrayList<Integer> drops = new ArrayList<Integer>();
		final TargetableItem kingGhost = createTargetableItem(TYPE_KING_GHOST,
				HEALTH_KING_GHOST,
				BASE_POINT_KING_GHOST,
				EXP_POINT_KING_GHOST);

		if (dropDraft < InventoryItemEntry.DROP_RATE_KING_CROWN) {
			drops.add(InventoryItemEntry.TYPE_KING_CROWN);
		}

		if (dropDraft < InventoryItemEntry.DROP_RATE_COIN * 25) {
			drops.add(InventoryItemEntry.TYPE_COIN);
		}

		kingGhost.setDrop(drops);
		return kingGhost;
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
