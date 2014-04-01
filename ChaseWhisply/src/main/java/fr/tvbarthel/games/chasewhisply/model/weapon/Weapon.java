package fr.tvbarthel.games.chasewhisply.model.weapon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A model for all the weapons used in this game.
 * A Weapon has a cartridge clip capacity {@code mAmmunitionLimit}
 * A Weapon deals {@code mDamage} on each shot.
 * A Weapon should regain one ammunition every {@code mReloadingTime} millisecond.
 */
public class Weapon implements Parcelable {
    //the damage done when firing
    private int mDamage;
    //the current number of ammunition in the cartridge clip
    private int mCurrentAmmunition;
    //the cartridge clip capacity
    private int mAmmunitionLimit;
    //the reloading interval in millisecond
    private long mReloadingTime;
    private boolean mHasRunOutOfAmmo;

    public Weapon() {
        mDamage = 0;
        mCurrentAmmunition = 0;
        mAmmunitionLimit = 1;
        mReloadingTime = 1;
        mHasRunOutOfAmmo = false;
    }

    public Weapon(Parcel in) {
        readFromParcel(in);
    }

    /**
     * Try to add one ammunition to the cartridge clip.
     */
    public void reload() {
        if (mCurrentAmmunition < mAmmunitionLimit) {
            mCurrentAmmunition += 1;
        }
    }

    /**
     * Try to add {@code ammoAmount} ammunition to the cartridge clip
     *
     * @param ammoAmount the number of ammunition added to the cartridge clip
     */
    public void reload(int ammoAmount) {
        if (ammoAmount <= mAmmunitionLimit) {
            mCurrentAmmunition = ammoAmount;
        }
    }

    /**
     * Fire a bullet.
     *
     * @return the damage done during the shot or 0 if there is no ammunition
     */
    public int fire() {
        if (mCurrentAmmunition > 0) {
            mCurrentAmmunition -= 1;
            return mDamage;
        }
        mHasRunOutOfAmmo = true;
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(mDamage);
        out.writeInt(mCurrentAmmunition);
        out.writeInt(mAmmunitionLimit);
        out.writeByte((byte) (mHasRunOutOfAmmo ? 1 : 0));
    }

    public void readFromParcel(Parcel in) {
        mDamage = in.readInt();
        mCurrentAmmunition = in.readInt();
        mAmmunitionLimit = in.readInt();
        mHasRunOutOfAmmo = in.readByte() == 1;
    }

    public static final Parcelable.Creator<Weapon> CREATOR = new Parcelable.Creator<Weapon>() {
        public Weapon createFromParcel(Parcel in) {
            return new Weapon(in);
        }

        public Weapon[] newArray(int size) {
            return new Weapon[size];
        }
    };

    /**
     * Getters & Setters
     */
    public void setDamage(int damage) {
        mDamage = damage;
    }

    public int getDamage() {
        return mDamage;
    }

    public int getCurrentAmmunition() {
        return mCurrentAmmunition;
    }

    public void setAmmunitionLimit(int ammunitionLimit) {
        mAmmunitionLimit = ammunitionLimit;
    }

    public void setCurrentAmmunition(int currentAmmunition) {
        mCurrentAmmunition = currentAmmunition;
    }

    public int getAmmunitionLimit() {
        return mAmmunitionLimit;
    }

    public void setReloadingTime(long reloadingTime) {
        mReloadingTime = reloadingTime;
    }

    public long getReloadingTime() {
        return mReloadingTime;
    }

    public boolean hasRunOutOfAmmo() {
        return mHasRunOutOfAmmo;
    }

}
