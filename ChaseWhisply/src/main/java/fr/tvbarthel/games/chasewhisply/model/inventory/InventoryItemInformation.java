package fr.tvbarthel.games.chasewhisply.model.inventory;

import android.os.Parcel;
import android.os.Parcelable;

public class InventoryItemInformation implements Parcelable{
	public static final int TYPE_KING_CROWN = 0x00000001;
	public static final int TYPE_BROKEN_HELMET_HORN = 0x00000002;
	public static final int TYPE_BABY_DROOL = 0x00000003;
	public static final int TYPE_COIN = 0x00000004;
	public static final int TYPE_STEEL_BULLET = 0x00000005;
	public static final int TYPE_GOLD_BULLET = 0x00000006;

	private int mType;
	private int mTitleResourceId;
	private int mDescriptionResourceId;

	public InventoryItemInformation() {
		mType = 0;
		mTitleResourceId = 0;
		mDescriptionResourceId = 0;
	}

	public InventoryItemInformation(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mType);
		dest.writeInt(mTitleResourceId);
		dest.writeInt(mDescriptionResourceId);
	}

	public void readFromParcel(Parcel in) {
		mType = in.readInt();
		mTitleResourceId = in.readInt();
		mDescriptionResourceId = in.readInt();
	}

	public static final Parcelable.Creator<InventoryItemInformation> CREATOR = new Parcelable.Creator<InventoryItemInformation>() {
		public InventoryItemInformation createFromParcel(Parcel in) {
			return new InventoryItemInformation(in);
		}

		public InventoryItemInformation[] newArray(int size) {
			return new InventoryItemInformation[size];
		}
	};



	/**
	 * Setters & Getters
	 */

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public int getTitleResourceId() {
		return mTitleResourceId;
	}

	public void setTitleResourceId(int titleResourceId) {
		mTitleResourceId = titleResourceId;
	}

	public int getDescriptionResourceId() {
		return mDescriptionResourceId;
	}

	public void setDescriptionResourceId(int descriptionResourceId) {
		mDescriptionResourceId = descriptionResourceId;
	}


}
