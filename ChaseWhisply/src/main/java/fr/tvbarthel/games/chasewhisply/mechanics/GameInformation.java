package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.model.Weapon;

public class GameInformation implements Parcelable {
	protected int mScore;
	protected long mRemainingTime;
	protected long mSpawningTime;
	protected Weapon mWeapon;
	protected TargetableItem mCurrentTarget;
	protected List<TargetableItem> mTargetableItems;
	protected List<DisplayableItem> mDisplayableItems;
	protected int mTargetKilled;
	protected int mBulletFired;
	protected int mCurrentCombo;
	protected int mMaxCombo;
	protected int mSceneWidth;
	protected int mSceneHeight;
	protected float mCurrentX;
	protected float mCurrentY;

	/**
	 * Create a new GameInformation
	 *
	 * @param remainingTime remaining time in millisecond
	 * @param spawningTime  spawning time in millisecond
	 * @param weapon        weapon used for this game
	 */
	public GameInformation(long remainingTime, long spawningTime, Weapon weapon) {
		mScore = 0;
		mTargetKilled = 0;
		mBulletFired = 0;
		mCurrentCombo = 0;
		mMaxCombo = 0;
		mRemainingTime = remainingTime;
		mSpawningTime = spawningTime;
		mWeapon = weapon;
		mCurrentTarget = null;
		mTargetableItems = new ArrayList<TargetableItem>();
		mDisplayableItems = new ArrayList<DisplayableItem>();
	}

	public GameInformation(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		mScore = in.readInt();
		mTargetKilled = in.readInt();
		mBulletFired = in.readInt();
		mCurrentCombo = in.readInt();
		mMaxCombo = in.readInt();
		mRemainingTime = in.readLong();
		mSpawningTime = in.readLong();
		mWeapon = in.readParcelable(Weapon.class.getClassLoader());
		mCurrentTarget = in.readParcelable(TargetableItem.class.getClassLoader());
		mTargetableItems = new ArrayList<TargetableItem>();
		in.readTypedList(mTargetableItems, TargetableItem.CREATOR);
		mDisplayableItems = new ArrayList<DisplayableItem>();
		in.readTypedList(mDisplayableItems, DisplayableItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeInt(mScore);
		out.writeInt(mTargetKilled);
		out.writeInt(mBulletFired);
		out.writeInt(mCurrentCombo);
		out.writeInt(mMaxCombo);
		out.writeLong(mRemainingTime);
		out.writeLong(mSpawningTime);
		out.writeParcelable(mWeapon, i);
		out.writeParcelable(mCurrentTarget, i);
		out.writeTypedList(mTargetableItems);
		out.writeTypedList(mDisplayableItems);
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
	public Weapon getWeapon() {
		return mWeapon;
	}

	public long getRemainingTime() {
		return mRemainingTime;
	}

	public void setRemainingTime(long time) {
		mRemainingTime = time;
	}

	public long getSpawningTime() {
		return mSpawningTime;
	}

	public void setSpawningTime(long spawningTime) {
		mSpawningTime = spawningTime;
	}

	public void addTargetableItem(TargetableItem item) {
		mTargetableItems.add(item);
	}

	public void addDisplayableItem(DisplayableItem item) {
		mDisplayableItems.add(item);
	}

	public List<DisplayableItem> getItemsForDisplay() {
		final ArrayList<DisplayableItem> displayAll = new ArrayList<DisplayableItem>();
		displayAll.addAll(mTargetableItems);
		displayAll.addAll(mDisplayableItems);
		return displayAll;
	}

	public void setSceneWidth(int sceneWidth) {
		mSceneWidth = sceneWidth;
	}

	public int getSceneWidth() {
		return mSceneWidth;
	}

	public void setSceneHeight(int sceneHeight) {
		mSceneHeight = sceneHeight;
	}

	public int getSceneHeight() {
		return mSceneHeight;
	}

	public void setCurrentPosition(float x, float y) {
		mCurrentX = x;
		mCurrentY = y;
	}

	public float[] getCurrentPosition() {
		return new float[]{mCurrentX, mCurrentY};
	}

	/**
	 * get current target
	 *
	 * @return current target
	 */
	public TargetableItem getCurrentTarget() {
		return mCurrentTarget;
	}

	/**
	 * set current target
	 *
	 * @param t current TargetableItem targeted
	 */
	public void setCurrentTarget(TargetableItem t) {
		mCurrentTarget = t;
	}

	/**
	 * set current target to null
	 */
	public void removeTarget() {
		mCurrentTarget = null;
	}

	/**
	 * increase targets killed number
	 */
	public void targetKilled() {
		mCurrentTarget = null;
		mTargetKilled++;
	}

	/**
	 * used to know frag number
	 *
	 * @return number of frag
	 */
	public int getFragNumber() {
		return mTargetKilled;
	}

	/**
	 * increase bullets fired number
	 */
	public void bulletFired() {
		mBulletFired++;
	}

	/**
	 * used to get combo number
	 *
	 * @return current combo
	 */
	public int getCurrentCombo() {
		return mCurrentCombo;
	}

	/**
	 * return maximum combo during the game
	 *
	 * @return max combo number
	 */
	public int getMaxCombo() {
		if (mCurrentCombo > mMaxCombo) mMaxCombo = mCurrentCombo;
		return mMaxCombo;
	}

	/**
	 * increase combo if conditions are filled
	 */
	public void stackCombo() {
		if (mTargetKilled > mCurrentCombo * mCurrentCombo) {
			mCurrentCombo++;
		}
	}

	/**
	 * reset current combo counter
	 */
	public void resetCombo() {
		if (mCurrentCombo > mMaxCombo) mMaxCombo = mCurrentCombo;
		mCurrentCombo = 0;
	}

	/**
	 * increase score
	 *
	 * @param ammount score you want to add to the current one
	 */
	public void increaseScore(int ammount) {
		if (ammount > 0) {
			mScore += ammount;
		}
	}

	/**
	 * get current score
	 *
	 * @return current score
	 */
	public int getCurrentScore() {
		return mScore;
	}

}
