package fr.tvbarthel.games.chasewhisply.model;


import android.content.SharedPreferences;

import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;

public class PlayerProfile {
	public static final String SHARED_PREFERENCES_NAME = "PlayerProfile";
	private static final String KEY_BULLETS_FIRED = "keyBulletsFired";
	private static final String KEY_GAMES_PLAYED = "keyGamesPlayed";
	private static final String KEY_TARGETS_KILLED = "keyTargetsKilled";
	private static final String KEY_BULLETS_MISSED = "keyBulletsMissed";
	private static final String KEY_EXPERIENCE_EARNED = "keyExperienceEarned";
	private static final String KEY_ITEM_QUANTITY_OLD_COIN = "keyItemQuantityOldCoin";
	private static final String KEY_ITEM_QUANTITY_BROKEN_HELMET_HORN = "keyItemQuantityBrokenHelmetHorn";
	private static final String KEY_ITEM_QUANTITY_BABY_DROOL = "keyItemQuantityBabyDrool";
	private static final String KEY_ITEM_QUANTITY_KING_CROWN = "keyItemQuantityKingCrown";
	private static final String KEY_ITEM_QUANTITY_STEEL_BULLET = "keyItemQuantitySteelBullet";
	private static final String KEY_ITEM_QUANTITY_GOLD_BULLET = "keyItemQuantityGoldBullet";
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	public PlayerProfile(SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
		mEditor = mSharedPreferences.edit();
	}

	private void increaseSharedLongInteger(String key, long amount) {
		long value = mSharedPreferences.getLong(key, 0);
		value += amount;
		mEditor.putLong(key, value);
	}

	private long getSharedLongInteger(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	public void increaseInventoryItemQuantity(int inventoryItemType) {
		increaseInventoryItemQuantity(inventoryItemType, 1);
	}

	public void increaseInventoryItemQuantity(int inventoryItemType, int amount) {
		switch (inventoryItemType) {
			case InventoryItemEntry.TYPE_COIN:
				increaseOldCoinQuantity(amount);
				break;

			case InventoryItemEntry.TYPE_BABY_DROOL:
				increaseBabyDroolQuantity(amount);
				break;

			case InventoryItemEntry.TYPE_BROKEN_HELMET_HORN:
				increaseBrokenHelmetHornQuantity(amount);
				break;

			case InventoryItemEntry.TYPE_KING_CROWN:
				increaseKingCrownQuantity(amount);
				break;

			case InventoryItemEntry.TYPE_STEEL_BULLET:
				increaseSteelBulletQuantity(amount);
				break;

			case InventoryItemEntry.TYPE_GOLD_BULLET:
				increaseGoldBulletQuantity(amount);
				break;
		}
	}

	public long getOldCoinQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_OLD_COIN);
	}

	public void increaseOldCoinQuantity(long amount) {
		increaseSharedLongInteger(KEY_ITEM_QUANTITY_OLD_COIN, amount);
	}

	public long getBrokenHelmetHornQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_BROKEN_HELMET_HORN);
	}

	public void increaseBrokenHelmetHornQuantity(long amount) {
		increaseSharedLongInteger(KEY_ITEM_QUANTITY_BROKEN_HELMET_HORN, amount);
	}

	public long getBabyDroolQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_BABY_DROOL);
	}

	public void increaseBabyDroolQuantity(long amount) {
		increaseSharedLongInteger(KEY_ITEM_QUANTITY_BABY_DROOL, amount);
	}

	public long getKingCrownQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_KING_CROWN);
	}

	public void increaseKingCrownQuantity(long amount) {
		increaseSharedLongInteger(KEY_ITEM_QUANTITY_KING_CROWN, amount);
	}

	public long getSteelBulletQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_STEEL_BULLET);
	}

	public void increaseSteelBulletQuantity(long amount) {
		increaseSharedLongInteger(KEY_ITEM_QUANTITY_STEEL_BULLET, amount);
	}

	public long getGoldBulletQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_GOLD_BULLET);
	}

	public void increaseGoldBulletQuantity(long amount) {
		increaseSharedLongInteger(KEY_ITEM_QUANTITY_GOLD_BULLET, amount);
	}

	public void increaseTargetsKilled(long amount) {
		increaseSharedLongInteger(KEY_TARGETS_KILLED, amount);
	}

	public long getTargetsKilled() {
		return getSharedLongInteger(KEY_TARGETS_KILLED);
	}

	public void increaseGamesPlayed(long amount) {
		increaseSharedLongInteger(KEY_GAMES_PLAYED, amount);
	}

	public long getGamesPlayed() {
		return getSharedLongInteger(KEY_GAMES_PLAYED);
	}

	public void increaseBulletsFired(long amount) {
		increaseSharedLongInteger(KEY_BULLETS_FIRED, amount);
	}

	public long getBulletsFired() {
		return getSharedLongInteger(KEY_BULLETS_FIRED);
	}

	public boolean saveChanges() {
		return mEditor.commit();
	}

	public void increaseBulletsMissed(long amount) {
		increaseSharedLongInteger(KEY_BULLETS_MISSED, amount);
	}

	public int getAccuracy() {
		final long bulletsFired = getSharedLongInteger(KEY_BULLETS_FIRED);
		if (bulletsFired == 0) return 0;
		final long bulletsMissed = getSharedLongInteger(KEY_BULLETS_MISSED);
		return (int) ((bulletsFired - bulletsMissed) * 100f / bulletsFired);
	}

	public void increaseExperienceEarned(long amount) {
		increaseSharedLongInteger(KEY_EXPERIENCE_EARNED, amount);
	}

	public int getLevelStep(int level) {
		return (int) (100 * level + Math.pow(level, 2.75));
	}

	public LevelInformation getLevelInformation() {
		long experienceEarned = getSharedLongInteger(KEY_EXPERIENCE_EARNED);
		int level = -1;
		long currentStep = 0;
		long nextStep = 0;

		do {
			level++;
			currentStep = getLevelStep(level);
			nextStep = getLevelStep(level + 1);
		} while (experienceEarned > nextStep);

		return new LevelInformation(level, experienceEarned, currentStep, nextStep);
	}

}
