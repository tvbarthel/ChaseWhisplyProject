package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameMode implements Parcelable {

	protected GameMode(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {

	}

	/**
	 * read parameters from a given Parcel
	 *
	 * @param in input Parcel to read from
	 */
	public void readFromParcel(Parcel in) {

	}

	public static final Parcelable.Creator<GameMode> CREATOR = new Parcelable.Creator<GameMode>() {
		public GameMode createFromParcel(Parcel in) {
			return new GameMode(in);
		}

		public GameMode[] newArray(int size) {
			return new GameMode[size];
		}
	};
}
