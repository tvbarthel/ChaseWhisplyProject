package fr.tvbarthel.games.chasewhisply.model.inventory;

import android.os.Parcel;
import android.os.Parcelable;

public class InventoryItemEntry implements Parcelable {
    private InventoryItemInformation mInventoryItemInformation;
    private DroppedByList mDroppedBy;
    private Recipe mRecipe;
    private long mQuantityAvailable;
    private boolean mIsFrenchFeminineGender;

    public InventoryItemEntry() {
        mDroppedBy = null;
        mRecipe = null;
        mQuantityAvailable = 0;
        mIsFrenchFeminineGender = false;
    }

    public InventoryItemEntry(Parcel in) {
        readFromParcel(in);
    }

	/*
        Setters and Getters
	 */

    public void setFrenchFeminineGender(boolean isFrenchFeminineGender) {
        mIsFrenchFeminineGender = isFrenchFeminineGender;
    }

    public boolean isFrenchFeminineGender() {
        return mIsFrenchFeminineGender;
    }

    public void setTitleResourceId(int titleResourceId) {
        mInventoryItemInformation.setTitleResourceId(titleResourceId);
    }

    public int getTitleResourceId() {
        return mInventoryItemInformation.getTitleResourceId();
    }

    public void setDescriptionResourceId(int descriptionResourceId) {
        mInventoryItemInformation.setDescriptionResourceId(descriptionResourceId);
    }

    public int getDescriptionResourceId() {
        return mInventoryItemInformation.getDescriptionResourceId();
    }

    public int getImageResourceId() {
        return mInventoryItemInformation.getImageResourceId();
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

    public int getType() {
        return mInventoryItemInformation.getType();
    }

    public void setInventoryItemInformation(InventoryItemInformation inventoryItemInformation) {
        mInventoryItemInformation = inventoryItemInformation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mInventoryItemInformation, flags);
        dest.writeParcelable(mDroppedBy, flags);
        dest.writeParcelable(mRecipe, flags);
        dest.writeLong(mQuantityAvailable);
    }

    public void readFromParcel(Parcel in) {
        mInventoryItemInformation = in.readParcelable(InventoryItemInformation.class.getClassLoader());
        mDroppedBy = in.readParcelable(DroppedByList.class.getClassLoader());
        mRecipe = in.readParcelable(Recipe.class.getClassLoader());
        mQuantityAvailable = in.readLong();
    }

    public static final Parcelable.Creator<InventoryItemEntry> CREATOR = new Parcelable.Creator<InventoryItemEntry>() {
        public InventoryItemEntry createFromParcel(Parcel in) {
            return new InventoryItemEntry(in);
        }

        public InventoryItemEntry[] newArray(int size) {
            return new InventoryItemEntry[size];
        }
    };
}
