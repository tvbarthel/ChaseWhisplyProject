package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameMode implements Parcelable {

	public String getmRules() {
		return mRules;
	}

	public void setmRules(String mRules) {
		this.mRules = mRules;
	}

	private int mType;
	private int mLevel;
	private int mImage;
	private String mRules;

	public GameMode() {
		mType = -1;
		mLevel = -1;
		mImage = -1;
		mRules = new String();
	}

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

	public int getType() {
		return mType;
	}

	public void setType(int mType) {
		this.mType = mType;
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int mLevel) {
		this.mLevel = mLevel;
	}

	public int getImage() {
		return mImage;
	}

	public void setImage(int mImage) {
		this.mImage = mImage;
	}


}
