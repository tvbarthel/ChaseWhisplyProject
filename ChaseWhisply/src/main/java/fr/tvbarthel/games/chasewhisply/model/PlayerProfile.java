package fr.tvbarthel.games.chasewhisply.model;


import android.content.SharedPreferences;

import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;

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

	private long increaseSharedLongInteger(String key, long amount) {
		long value = mSharedPreferences.getLong(key, 0);
		value += amount;
		mEditor.putLong(key, value);
		return value;
	}

	private long getSharedLongInteger(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	public void increaseInventoryItemQuantity(int inventoryItemType) {
		increaseInventoryItemQuantity(inventoryItemType, 1);
	}

	public long decreaseInventoryItemQuantity(int inventoryItemType, int amount) {
		return increaseInventoryItemQuantity(inventoryItemType, -amount);
	}

	public long increaseInventoryItemQuantity(int inventoryItemType, int amount) {
		long newQuantity = 0;
		switch (inventoryItemType) {
			case InventoryItemInformation.TYPE_COIN:
				newQuantity = increaseOldCoinQuantity(amount);
				break;

			case InventoryItemInformation.TYPE_BABY_DROOL:
				newQuantity = increaseBabyDroolQuantity(amount);
				break;

			case InventoryItemInformation.TYPE_BROKEN_HELMET_HORN:
				newQuantity = increaseBrokenHelmetHornQuantity(amount);
				break;

			case InventoryItemInformation.TYPE_KING_CROWN:
				newQuantity = increaseKingCrownQuantity(amount);
				break;

			case InventoryItemInformation.TYPE_STEEL_BULLET:
				newQuantity = increaseSteelBulletQuantity(amount);
				break;

			case InventoryItemInformation.TYPE_GOLD_BULLET:
				newQuantity = increaseGoldBulletQuantity(amount);
				break;
		}
		return newQuantity;
	}

	public long getInventoryItemQuantity(int inventoryItemEntryType) {
		long quantity = 0;
		switch (inventoryItemEntryType) {
			case InventoryItemInformation.TYPE_COIN:
				quantity = getOldCoinQuantity();
				break;

			case InventoryItemInformation.TYPE_BROKEN_HELMET_HORN:
				quantity = getBrokenHelmetHornQuantity();
				break;

			case InventoryItemInformation.TYPE_BABY_DROOL:
				quantity = getBabyDroolQuantity();
				break;

			case InventoryItemInformation.TYPE_KING_CROWN:
				quantity = getKingCrownQuantity();
				break;

			case InventoryItemInformation.TYPE_STEEL_BULLET:
				quantity = getSteelBulletQuantity();
				break;

			case InventoryItemInformation.TYPE_GOLD_BULLET:
				quantity = getGoldBulletQuantity();
				break;
		}
		return quantity;
	}

	public long getOldCoinQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_OLD_COIN);
	}

	public long increaseOldCoinQuantity(long amount) {
		return increaseSharedLongInteger(KEY_ITEM_QUANTITY_OLD_COIN, amount);
	}

	public long getBrokenHelmetHornQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_BROKEN_HELMET_HORN);
	}

	public long increaseBrokenHelmetHornQuantity(long amount) {
		return increaseSharedLongInteger(KEY_ITEM_QUANTITY_BROKEN_HELMET_HORN, amount);
	}

	public long getBabyDroolQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_BABY_DROOL);
	}

	public long increaseBabyDroolQuantity(long amount) {
		return increaseSharedLongInteger(KEY_ITEM_QUANTITY_BABY_DROOL, amount);
	}

	public long getKingCrownQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_KING_CROWN);
	}

	public long increaseKingCrownQuantity(long amount) {
		return increaseSharedLongInteger(KEY_ITEM_QUANTITY_KING_CROWN, amount);
	}

	public long getSteelBulletQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_STEEL_BULLET);
	}

	public long increaseSteelBulletQuantity(long amount) {
		return increaseSharedLongInteger(KEY_ITEM_QUANTITY_STEEL_BULLET, amount);
	}

	public long getGoldBulletQuantity() {
		return getSharedLongInteger(KEY_ITEM_QUANTITY_GOLD_BULLET);
	}

	public long  increaseGoldBulletQuantity(long amount) {
		return increaseSharedLongInteger(KEY_ITEM_QUANTITY_GOLD_BULLET, amount);
	}

	public long increaseTargetsKilled(long amount) {
		return increaseSharedLongInteger(KEY_TARGETS_KILLED, amount);
	}

	public long getTargetsKilled() {
		return getSharedLongInteger(KEY_TARGETS_KILLED);
	}

	public long increaseGamesPlayed(long amount) {
		return increaseSharedLongInteger(KEY_GAMES_PLAYED, amount);
	}

	public long getGamesPlayed() {
		return getSharedLongInteger(KEY_GAMES_PLAYED);
	}

	public long increaseBulletsFired(long amount) {
		return increaseSharedLongInteger(KEY_BULLETS_FIRED, amount);
	}

	public long getBulletsFired() {
		return getSharedLongInteger(KEY_BULLETS_FIRED);
	}

	public boolean saveChanges() {
		return mEditor.commit();
	}

	public long increaseBulletsMissed(long amount) {
		return increaseSharedLongInteger(KEY_BULLETS_MISSED, amount);
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
