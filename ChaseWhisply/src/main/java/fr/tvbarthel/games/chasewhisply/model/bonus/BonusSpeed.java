package fr.tvbarthel.games.chasewhisply.model.bonus;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class BonusSpeed implements Bonus, BonusInventoryItemConsumer {
    private long mSpeedReduction;
    private int mInventoryItemType;

    public BonusSpeed() {
        mSpeedReduction = 0;
        mInventoryItemType = 0;
    }

    public BonusSpeed(int inventoryItemType, long speedReduction) {
        mSpeedReduction = speedReduction;
        mInventoryItemType = inventoryItemType;
    }

    public BonusSpeed(Parcel in) {
        readFromParcel(in);
    }

    public long getSpeedReduction() {
        return mSpeedReduction;
    }

    @Override
    public void apply(GameInformationStandard gameInformation) {
        final long currentReloadingTime = gameInformation.getWeapon().getReloadingTime();
        gameInformation.getWeapon().setReloadingTime(currentReloadingTime - mSpeedReduction);
    }

    @Override
    public Bonus consume(PlayerProfile playerProfile) {
        final long remainingQuantity = playerProfile.decreaseInventoryItemQuantity(mInventoryItemType, 1);
        if (remainingQuantity > 0) {
            return this;
        } else {
            return new Bonus.DummyBonus();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<BonusSpeed> CREATOR = new Parcelable.Creator<BonusSpeed>() {
        public BonusSpeed createFromParcel(Parcel in) {
            return new BonusSpeed(in);
        }

        public BonusSpeed[] newArray(int size) {
            return new BonusSpeed[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mSpeedReduction);
        out.writeInt(mInventoryItemType);
    }

    private void readFromParcel(Parcel in) {
        mSpeedReduction = in.readLong();
        mInventoryItemType = in.readInt();
    }
}
