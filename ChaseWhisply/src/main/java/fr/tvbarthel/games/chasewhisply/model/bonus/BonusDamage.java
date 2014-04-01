package fr.tvbarthel.games.chasewhisply.model.bonus;


import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class BonusDamage implements Bonus, BonusInventoryItemConsumer {
    private int mBonusDamage;
    private int mInventoryItemType;

    public BonusDamage() {
        mBonusDamage = 0;
        mInventoryItemType = 0;
    }

    public BonusDamage(int inventoryItemType, int bonusDamage) {
        mBonusDamage = bonusDamage;
        mInventoryItemType = inventoryItemType;
    }

    public BonusDamage(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void apply(GameInformationStandard gameInformation) {
        int currentDamage = gameInformation.getWeapon().getDamage();
        gameInformation.getWeapon().setDamage(currentDamage + mBonusDamage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mBonusDamage);
        out.writeInt(mInventoryItemType);
    }

    private void readFromParcel(Parcel in) {
        mBonusDamage = in.readInt();
        mInventoryItemType = in.readInt();
    }

    public void setBonusDamage(int bonusDamage) {
        mBonusDamage = bonusDamage;
    }

    public int getBonusDamage() {
        return mBonusDamage;
    }

    public static final Parcelable.Creator<BonusDamage> CREATOR = new Parcelable.Creator<BonusDamage>() {
        public BonusDamage createFromParcel(Parcel in) {
            return new BonusDamage(in);
        }

        public BonusDamage[] newArray(int size) {
            return new BonusDamage[size];
        }
    };

    @Override
    public Bonus consume(PlayerProfile playerProfile) {
        final long remainingQuantity = playerProfile.decreaseInventoryItemQuantity(mInventoryItemType, 1);
        if (remainingQuantity > 0) {
            return this;
        } else {
            return new Bonus.DummyBonus();
        }
    }
}
