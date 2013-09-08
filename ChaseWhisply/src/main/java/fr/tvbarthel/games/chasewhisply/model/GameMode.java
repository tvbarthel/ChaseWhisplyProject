package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;

public class GameMode implements Parcelable {

	private int mType;
	private int mLevel;
	private int mImage;
	private int mRules;
	private int mLeaderboardStringId;
	private int mLeaderboardDescriptionStringId;
	private Bonus mBonus;

	public GameMode() {
		mType = -1;
		mLevel = -1;
		mImage = -1;
		mRules = -1;
		mLeaderboardStringId = -1;
		mLeaderboardDescriptionStringId = -1;
		mBonus = null;
	}

	protected GameMode(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeInt(mType);
		out.writeInt(mLevel);
		out.writeInt(mImage);
		out.writeInt(mRules);
		out.writeInt(mLeaderboardStringId);
		out.writeInt(mLeaderboardDescriptionStringId);
		out.writeParcelable(mBonus, i);
	}

	/**
	 * read parameters from a given Parcel
	 *
	 * @param in input Parcel to read from
	 */
	public void readFromParcel(Parcel in) {
		mType = in.readInt();
		mLevel = in.readInt();
		mImage = in.readInt();
		mRules = in.readInt();
		mLeaderboardStringId = in.readInt();
		mLeaderboardDescriptionStringId = in.readInt();
		mBonus = in.readParcelable(Bonus.class.getClassLoader());
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

	public int getRules() {
		return mRules;
	}

	public void setRules(int mRules) {
		this.mRules = mRules;
	}

	public void setLeaderboardStringId(int stringId) {
		mLeaderboardStringId = stringId;
	}

	public int getLeaderboardStringId() {
		return mLeaderboardStringId;
	}

	public void setLeaderboardDescriptionStringId(int stringId) {
		mLeaderboardDescriptionStringId = stringId;
	}

	public int getLeaderboardDescriptionStringId() {
		return mLeaderboardDescriptionStringId;
	}

	public void setBonus(Bonus bonus) {
		mBonus = bonus;
	}

	public Bonus getBonus() {
		return mBonus;
	}


}
