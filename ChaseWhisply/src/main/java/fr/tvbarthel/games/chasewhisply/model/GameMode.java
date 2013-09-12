package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;

public abstract class GameMode implements Parcelable {

	private int mType;
	private int mLevel;
	private int mImage;
	private int mRules;
	private int mLeaderboardStringId;
	private int mLeaderboardDescriptionStringId;
	private Bonus mBonus;
	private int mRequiredLevel;
	private int mRequiredMessage;

	public GameMode() {
		mType = -1;
		mLevel = -1;
		mImage = -1;
		mRules = -1;
		mLeaderboardStringId = -1;
		mLeaderboardDescriptionStringId = -1;
		mBonus = null;
		mRequiredLevel = -1;
		mRequiredMessage = -1;

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
		out.writeInt(mRequiredLevel);
		out.writeInt(mRequiredMessage);
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
		mRequiredLevel = in.readInt();
		mRequiredMessage = in.readInt();
	}

	public static final Parcelable.Creator<GameMode> CREATOR = new Parcelable.Creator<GameMode>() {
		public GameMode createFromParcel(Parcel in) {
			return new GameMode(in) {
				@Override
				public boolean isAvailable(PlayerProfile p) {
					return true;
				}
			};
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

	/**
	 * set the required level to play this game mode
	 *
	 * @param level
	 */
	public void setRequiredLevel(int level) {
		mRequiredLevel = level;
	}

	/**
	 * get the required level to play this game mode
	 *
	 * @return
	 */
	public int getRequiredLevel() {
		return mRequiredLevel;
	}

	/**
	 * set message displayed to the user when he can't access to this game mode
	 *
	 * @param message
	 */
	public void setRequiredMessage(int message) {
		mRequiredMessage = message;
	}

	/**
	 * get message displaed to the user when he can't access to this game mode
	 *
	 * @return
	 */
	public int getRequiredMessage() {
		return mRequiredMessage;
	}

	/**
	 * define own rules for availability
	 *
	 * @param p player profile
	 * @return true if available
	 */
	abstract public boolean isAvailable(PlayerProfile p);


}
