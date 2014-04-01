package fr.tvbarthel.games.chasewhisply.model;


import android.content.SharedPreferences;

import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.mode.GameModeFactory;

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
    private static final String KEY_ITEM_QUANTITY_ONE_SHOT_BULLET = "keyItemQuantityOneShotBullet";
    private static final String KEY_ITEM_QUANTITY_GHOST_TEAR = "keyItemQuantityGhostTear";
    private static final String KEY_ITEM_QUANTITY_SPEED_POTION = "keyItemQuantitySpeedPotion";
    private static final String KEY_RANK_SPRINT = "keyRankSprint";
    private static final String KEY_RANK_MARATHON = "keyRankMarathon";
    private static final String KEY_RANK_SURVIVAL = "keyRankSurvival";
    private static final String KEY_RANK_DEATH_TO_THE_KING = "keyRankDeathToTheKing";
    private static final String KEY_RANK_MEMORIZE = "keyRankMemorize";
    private static final String KEY_RANK_TWENTY_IN_A_ROW = "keyRankTwentyInARow";

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

    public long increaseInventoryItemQuantity(int inventoryItemType) {
        return increaseInventoryItemQuantity(inventoryItemType, 1);
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

            case InventoryItemInformation.TYPE_ONE_SHOT_BULLET:
                newQuantity = increaseOneShotQuantity(amount);
                break;

            case InventoryItemInformation.TYPE_GHOST_TEAR:
                newQuantity = increaseGhostTearQuantity(amount);
                break;

            case InventoryItemInformation.TYPE_SPEED_POTION:
                newQuantity = increaseSpeedPotionQuantity(amount);
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

            case InventoryItemInformation.TYPE_ONE_SHOT_BULLET:
                quantity = getOneShotBulletQuantity();
                break;

            case InventoryItemInformation.TYPE_GHOST_TEAR:
                quantity = getGhostTearQuantity();
                break;

            case InventoryItemInformation.TYPE_SPEED_POTION:
                quantity = getSpeedPotionQuantity();
                break;
        }
        return quantity;
    }

    public long getSpeedPotionQuantity() {
        return getSharedLongInteger(KEY_ITEM_QUANTITY_SPEED_POTION);
    }

    public long increaseSpeedPotionQuantity(long amount) {
        return increaseSharedLongInteger(KEY_ITEM_QUANTITY_SPEED_POTION, amount);
    }

    public long getGhostTearQuantity() {
        return getSharedLongInteger(KEY_ITEM_QUANTITY_GHOST_TEAR);
    }

    public long increaseGhostTearQuantity(long amount) {
        return increaseSharedLongInteger(KEY_ITEM_QUANTITY_GHOST_TEAR, amount);
    }

    public long getOneShotBulletQuantity() {
        return getSharedLongInteger(KEY_ITEM_QUANTITY_ONE_SHOT_BULLET);
    }

    public long increaseOneShotQuantity(long amount) {
        return increaseSharedLongInteger(KEY_ITEM_QUANTITY_ONE_SHOT_BULLET, amount);
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

    public long increaseGoldBulletQuantity(long amount) {
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
        return (int) (115 * level + Math.pow(level, 3));
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

    public int getRankByGameMode(GameMode g) {
        final int type = g.getType();
        final int level = g.getLevel();
        int rank;
        switch (type) {
            case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
                rank = mSharedPreferences.getInt(KEY_RANK_DEATH_TO_THE_KING, 0);
                break;
            case GameModeFactory.GAME_TYPE_SURVIVAL:
                rank = mSharedPreferences.getInt(KEY_RANK_SURVIVAL, 0);
                break;
            case GameModeFactory.GAME_TYPE_REMAINING_TIME:
                if (level == 1) {
                    rank = mSharedPreferences.getInt(KEY_RANK_SPRINT, 0);
                } else if (level == 3) {
                    rank = mSharedPreferences.getInt(KEY_RANK_MARATHON, 0);
                } else {
                    rank = 0;
                }
                break;
            case GameModeFactory.GAME_TYPE_MEMORIZE:
                rank = mSharedPreferences.getInt(KEY_RANK_MEMORIZE, 0);
                break;
            case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
                rank = mSharedPreferences.getInt(KEY_RANK_TWENTY_IN_A_ROW, 0);
                break;
            default:
                rank = 0;
                break;
        }
        return rank;
    }

    public void setRankByGameMode(GameMode g, int rank) {
        final int type = g.getType();
        final int level = g.getLevel();
        switch (type) {
            case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
                if (rank > mSharedPreferences.getInt(KEY_RANK_DEATH_TO_THE_KING, 0)) {
                    mEditor.putInt(KEY_RANK_DEATH_TO_THE_KING, rank);
                }
                break;
            case GameModeFactory.GAME_TYPE_SURVIVAL:
                if (rank > mSharedPreferences.getInt(KEY_RANK_SURVIVAL, 0)) {
                    mEditor.putInt(KEY_RANK_SURVIVAL, rank);
                }
                break;
            case GameModeFactory.GAME_TYPE_REMAINING_TIME:
                if (level == 1) {
                    if (rank > mSharedPreferences.getInt(KEY_RANK_SPRINT, 0)) {
                        mEditor.putInt(KEY_RANK_SPRINT, rank);
                    }
                } else if (level == 3) {
                    if (rank > mSharedPreferences.getInt(KEY_RANK_MARATHON, 0)) {
                        mEditor.putInt(KEY_RANK_MARATHON, rank);
                    }
                }
                break;
            case GameModeFactory.GAME_TYPE_MEMORIZE:
                if (rank > mSharedPreferences.getInt(KEY_RANK_MEMORIZE, 0)) {
                    mEditor.putInt(KEY_RANK_MEMORIZE, rank);
                }
                break;
            case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
                if (rank > mSharedPreferences.getInt(KEY_RANK_TWENTY_IN_A_ROW, 0)) {
                    mEditor.putInt(KEY_RANK_TWENTY_IN_A_ROW, rank);
                }
                break;
            default:
                break;
        }
    }

}
