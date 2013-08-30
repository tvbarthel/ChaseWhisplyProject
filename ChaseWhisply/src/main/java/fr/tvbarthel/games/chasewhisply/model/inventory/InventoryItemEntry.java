package fr.tvbarthel.games.chasewhisply.model.inventory;

public class InventoryItemEntry {
	public static final int TYPE_KING_CROWN = 0x00000001;
	public static final int TYPE_BROKEN_HELMET_HORN = 0x00000002;
	public static final int TYPE_BABY_DROOL = 0x00000003;
	public static final int TYPE_COIN = 0x00000004;
	public static final int TYPE_STEEL_BULLET = 0x00000005;
	public static final int TYPE_GOLD_BULLET = 0x00000006;

	//Drop rate
	public static final int DROP_RATE_BABY_DROOL = 5;
	public static final int DROP_RATE_BROKEN_HELMET_HORN = 3;
	public static final int DROP_RATE_COIN = 2;
	public static final int DROP_RATE_KING_CROWN = 50;


	private int mTitleResourceId;
	private int mDescriptionResourceId;
	private DroppedByList mDroppedBy;
	private Recipe mRecipe;
	private long mQuantityAvailable;

	public InventoryItemEntry() {
		mTitleResourceId = 0;
		mDescriptionResourceId = 0;
		mDroppedBy = null;
		mRecipe = null;
		mQuantityAvailable = 0;
	}

	/*
		Setters and Getters
	 */

	public void setTitleResourceId(int titleResourceId) {
		mTitleResourceId = titleResourceId;
	}

	public int getTitleResourceId() {
		return mTitleResourceId;
	}

	public void setDescriptionResourceId(int descriptionResourceId) {
		mDescriptionResourceId = descriptionResourceId;
	}

	public int getDescriptionResourceId() {
		return mDescriptionResourceId;
	}

	public void setDroppedBy(DroppedByList lootlist) {
		mDroppedBy = lootlist;
	}

	public DroppedByList getDroppedBy() {
		return mDroppedBy;
	}

	public void setRecipe(Recipe recipe) {
		mRecipe = recipe;
	}

	public Recipe getRecipe() {
		return mRecipe;
	}

	public void setQuantityAvailable(long quantityAvailable) {
		mQuantityAvailable = quantityAvailable;
	}

	public long getQuantityAvailable() {
		return mQuantityAvailable;
	}
}
