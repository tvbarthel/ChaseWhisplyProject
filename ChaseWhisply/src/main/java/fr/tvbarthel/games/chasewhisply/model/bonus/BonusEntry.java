package fr.tvbarthel.games.chasewhisply.model.bonus;

import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformationFactory;

public class BonusEntry {
    private long mQuantity;
    private int mEffectResourceId;
    private InventoryItemInformation mInventoryItemInformation;
    private boolean mIsEquipped = false;
    private Bonus mBonus;

    public BonusEntry(int inventoryItemType, long quantity) {
        mInventoryItemInformation = InventoryItemInformationFactory.create(inventoryItemType);
        mQuantity = quantity;
    }

    public void setIsEquipped(boolean isEquipped) {
        mIsEquipped = isEquipped;
    }

    public boolean isEquipped() {
        return mIsEquipped;
    }

    public long getQuantity() {
        return mQuantity;
    }

    public void setTitleResourceId(int resourceId) {
        mInventoryItemInformation.setTitleResourceId(resourceId);
    }

    public int getTitleResourceId() {
        return mInventoryItemInformation.getTitleResourceId();
    }

    public void setEffectResourceId(int resourceId) {
        mEffectResourceId = resourceId;
    }

    public int getEffectResourceId() {
        return mEffectResourceId;
    }

    public int getInventoryItemType() {
        return mInventoryItemInformation.getType();
    }

    public void setBonus(Bonus bonus) {
        mBonus = bonus;
    }

    public Bonus getBonus() {
        return mBonus;
    }

    public int getImageResourceId() {
        return mInventoryItemInformation.getImageResourceId();
    }
}
