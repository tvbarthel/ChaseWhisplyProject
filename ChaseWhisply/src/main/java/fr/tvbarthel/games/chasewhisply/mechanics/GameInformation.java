package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.Weapon;

public class GameInformation implements Parcelable {
	private int mScore;
	private long mRemainingTime;
	private Weapon mWeapon;
	private List<DisplayableItem> mItems;
	protected int mSceneWidth;
	protected int mSceneHeight;

	/**
	 * create new game information
	 * @param remainingTime remaining time in millisecond
	 * @param weapon weapon used for this game
	 */
	public GameInformation(long remainingTime, Weapon weapon) {
		mScore = 0;
		mRemainingTime = remainingTime;
		mWeapon = weapon;
		mItems = new ArrayList<DisplayableItem>();
	}

	public GameInformation(Parcel in) {
		readFromParcel(in);
	}

	public void updateItemVisibility(final float currentPosX, final float currentPosY) {
		updateItemVisibility(currentPosX - mSceneWidth/2, currentPosY - mSceneHeight/2, mSceneWidth, mSceneHeight);
	}

	public void updateItemVisibility(final float windowX, final float windowY, final int windowWidth, final int windowHeight) {
		for (DisplayableItem item : mItems) {
			item.updateVisibility(windowX, windowY, windowWidth, windowHeight);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		mScore = in.readInt();
		mRemainingTime = in.readLong();
		mWeapon = in.readParcelable(Weapon.class.getClassLoader());
		in.readTypedList(mItems, DisplayableItem.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeInt(mScore);
		out.writeLong(mRemainingTime);
		out.writeParcelable(mWeapon, i);
		out.writeTypedList(mItems);
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

	public void addItem(DisplayableItem item) {
		mItems.add(item);
	}

	public List<DisplayableItem> getItems() {
		return mItems;
	}

	public void setSceneWidth(int sceneWidth) {
		mSceneWidth = sceneWidth;
	}

	public int getSceneWidth(){
		return mSceneWidth;
	}

	public void setSceneHeight(int sceneHeight) {
		mSceneHeight = sceneHeight;
	}

	public int getmSceneHeight(){
		return mSceneHeight;
	}

}
