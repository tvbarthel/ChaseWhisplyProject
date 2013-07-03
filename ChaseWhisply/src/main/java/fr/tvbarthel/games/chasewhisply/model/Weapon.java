package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Weapon implements Parcelable {
	private int mDamage;
	private int mCurrentAmmunition;
	private int mAmmunitionLimit;
	private long mReloadingTime;

	public Weapon() {
		mDamage = 0;
		mCurrentAmmunition = 0;
		mAmmunitionLimit = 1;
		mReloadingTime = 1;
	}

	public Weapon(Parcel in) {
		readFromParcel(in);
	}

	public void reload() {
		if (mCurrentAmmunition < mAmmunitionLimit) {
			mCurrentAmmunition += 1;
		}
	}

	public int fire() {
		if (mCurrentAmmunition > 0) {
			mCurrentAmmunition -= 1;
			return mDamage;
		}
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
	}

	public void readFromParcel(Parcel in) {
		mDamage = in.readInt();
		mCurrentAmmunition = in.readInt();
		mAmmunitionLimit = in.readInt();
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

	public int getAmmunitionLimit() {
		return mAmmunitionLimit;
	}

	public void setReloadingTime(long reloadingTime) {
		mReloadingTime = reloadingTime;
	}

	public long getReloadingTime() {
		return mReloadingTime;
	}

}
