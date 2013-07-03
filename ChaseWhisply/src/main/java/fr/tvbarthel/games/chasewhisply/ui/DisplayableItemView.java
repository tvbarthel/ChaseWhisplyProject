package fr.tvbarthel.games.chasewhisply.ui;


import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;

public class DisplayableItemView implements Parcelable {
	protected DisplayableItem mModel;
	protected int mDrawable;

	public DisplayableItemView(Parcel in) {
		mModel = (DisplayableItem) in.readParcelable(DisplayableItem.class.getClassLoader());
		mDrawable = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeParcelable(mModel, i);
		out.writeInt(mDrawable);
	}

	public static final Parcelable.Creator<DisplayableItemView> CREATOR = new Parcelable.Creator<DisplayableItemView>() {
		public DisplayableItemView createFromParcel(Parcel in) {
			return new DisplayableItemView(in);
		}

		public DisplayableItemView[] newArray(int size) {
			return new DisplayableItemView[size];
		}
	};

	/**
	 * Getters and Setters
	 */
	public DisplayableItem getModel() {
		return mModel;
	}

	public void setModel(DisplayableItem model) {
		mModel = model;
	}

	public int getDrawableId() {
		return mDrawable;
	}

	public void setDrawableId(int drawable) {
		mDrawable = drawable;
	}
}
