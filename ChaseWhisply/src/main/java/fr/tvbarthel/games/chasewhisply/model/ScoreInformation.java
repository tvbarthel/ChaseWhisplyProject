package fr.tvbarthel.games.chasewhisply.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * ScoreInformation stores :
 * the player score
 * the number of targets killed
 * the number of bullets fired
 * the current combo
 * the max combo reached by the player
 */
public class ScoreInformation implements Parcelable {
	private int mScore;
	private int mNumberOfTargetsKilled;
	private int mNumberOfBulletsFired;
	private int mCurrentCombo;
	private int mMaxCombo;

	public ScoreInformation() {
		mScore = 0;
		mNumberOfBulletsFired = 0;
		mNumberOfTargetsKilled = 0;
		mCurrentCombo = 0;
		mMaxCombo = 0;
	}

	public ScoreInformation(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * Increase the score by one.
	 */
	public void increaseScore() {
		increaseScore(1);
	}

	/**
	 * Increase the score by {@code amount}.
	 *
	 * @param amount
	 */
	public void increaseScore(int amount) {
		mScore += amount;
	}

	/**
	 * Increase the number of targets killed by one.
	 */
	public void increaseNumberOfTargetsKilled() {
		mNumberOfTargetsKilled += 1;
	}

	/**
	 * Increase the number of bullets fired by one.
	 */
	public void increaseNumberOfBulletsFired() {
		mNumberOfBulletsFired += 1;
	}

	/**
	 * If the number of targets killed is greater than
	 * the current Combo squared, increase the current combo by one.
	 *
	 * If the new current combo is higher than the max combo
	 * the max combo is set to the current combo.
	 */
	public void increaseCurrentCombo() {
		if(mNumberOfTargetsKilled > mCurrentCombo * mCurrentCombo) {
			mCurrentCombo += 1;
			mMaxCombo = Math.max(mMaxCombo, mCurrentCombo);
		}
	}

	/**
	 * Set the current combo to 0.
	 */
	public void resetCurrentCombo() {
		mCurrentCombo = 0;
	}

	/*
		Parcelable stuff
	 */

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mScore);
		out.writeInt(mNumberOfBulletsFired);
		out.writeInt(mNumberOfTargetsKilled);
		out.writeInt(mCurrentCombo);
		out.writeInt(mMaxCombo);
	}

	private void readFromParcel(Parcel in) {
		mScore = in.readInt();
		mNumberOfBulletsFired = in.readInt();
		mNumberOfTargetsKilled = in.readInt();
		mCurrentCombo = in.readInt();
		mMaxCombo = in.readInt();
	}

	public static final Parcelable.Creator<ScoreInformation> CREATOR = new Parcelable.Creator<ScoreInformation>() {
		public ScoreInformation createFromParcel(Parcel in) {
			return new ScoreInformation(in);
		}

		public ScoreInformation[] newArray(int size) {
			return new ScoreInformation[size];
		}
	};

	/*
		Getters & Setters
	 */

	public int getScore() {
		return mScore;
	}

	public int getNumberOfTargetsKilled() {
		return mNumberOfTargetsKilled;
	}

	public int getmNumberOfBulletsFired() {
		return mNumberOfBulletsFired;
	}

	public int getCurrentCombo() {
		return mCurrentCombo;
	}

	public int getMaxCombo() {
		return mMaxCombo;
	}

}