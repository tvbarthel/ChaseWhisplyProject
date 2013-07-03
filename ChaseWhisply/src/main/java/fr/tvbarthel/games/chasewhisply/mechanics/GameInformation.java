package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

import fr.tvbarthel.games.chasewhisply.model.Weapon;
import fr.tvbarthel.games.chasewhisply.ui.DisplayableItemView;

public class GameInformation implements Parcelable {
	private int mScore;
	private long mCurrentTime;
	private Weapon mWeapon;
	private long mReloadingTime;
	private ArrayList<DisplayableItemView> mItems;

	public GameInformation(long reloadingTime, Weapon weapon) {
		mReloadingTime = reloadingTime;
		mScore = 0;
		mCurrentTime = 0;
		mWeapon = weapon;
		mItems = new ArrayList<DisplayableItemView>();
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
		mCurrentTime = in.readLong();
		mWeapon = in.readParcelable(Weapon.class.getClassLoader());
		mItems = new ArrayList<DisplayableItemView>(Arrays.asList((
				DisplayableItemView[]) in.readParcelableArray(DisplayableItemView.class.getClassLoader())));
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeLong(mReloadingTime);
		out.writeInt(mScore);
		out.writeLong(mCurrentTime);
		out.writeParcelable(mWeapon, i);
		out.writeParcelableArray((DisplayableItemView[]) mItems.toArray(), i);
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
}
