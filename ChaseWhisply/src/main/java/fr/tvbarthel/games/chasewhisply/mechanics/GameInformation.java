package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.model.Weapon;

public class GameInformation implements Parcelable {
	private int mScore;
	private long mRemainingTime;
	private Weapon mWeapon;
	private List<TargetableItem> mTargetableItems;
	private List<DisplayableItem> mDisplayableItems;
	protected int mSceneWidth;
	protected int mSceneHeight;
	protected float mCurrentX;
	protected float mCurrentY;

	/**
	 * create new game information
	 *
	 * @param remainingTime remaining time in millisecond
	 * @param weapon        weapon used for this game
	 */
	public GameInformation(long remainingTime, Weapon weapon) {
		mScore = 0;
		mRemainingTime = remainingTime;
		mWeapon = weapon;
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
		mRemainingTime = in.readLong();
		mWeapon = in.readParcelable(Weapon.class.getClassLoader());
		in.readTypedList(mTargetableItems, TargetableItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeInt(mScore);
		out.writeLong(mRemainingTime);
		out.writeParcelable(mWeapon, i);
		out.writeTypedList(mTargetableItems);
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

}
