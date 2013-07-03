package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.Weapon;

public class GameInformation implements Parcelable {
	private int mScore;
	private long mRemainingTime;
	private Weapon mWeapon;
	private long mReloadingTime;

	public GameInformation(long reloadingTime, long remainingTime, Weapon weapon) {
		mReloadingTime = reloadingTime;
		mScore = 0;
		mRemainingTime = remainingTime;
		mWeapon = weapon;
	}

	public GameInformation(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		mReloadingTime = in.readLong();
		mScore = in.readInt();
		mRemainingTime = in.readLong();
		mWeapon = in.readParcelable(Weapon.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeLong(mReloadingTime);
		out.writeInt(mScore);
		out.writeLong(mRemainingTime);
		out.writeParcelable(mWeapon, i);
	}

	public static final Parcelable.Creator<GameInformation> CREATOR = new Parcelable.Creator<GameInformation>() {
		public GameInformation createFromParcel(Parcel in) {
			return new GameInformation(in);
		}

		public GameInformation[] newArray(int size) {
			return new GameInformation[size];
		}
	};

	/**
	 * Getters & Setters
	 */
	public long getReloadingTime() {
		return mReloadingTime;
	}

	public Weapon getWeapon() {
		return mWeapon;
	}
}
