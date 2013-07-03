package fr.tvbarthel.games.chasewhisply.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class TargetableItemView extends DisplayableItemView {
	protected int mTargetDrawable;
	protected int mDeadDrawable;

	public TargetableItemView(Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		super.writeToParcel(out, i);
		out.writeInt(mTargetDrawable);
		out.writeInt(mDeadDrawable);
	}

	@Override
	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mTargetDrawable = in.readInt();
		mDeadDrawable = in.readInt();
	}

	public static final Parcelable.Creator<TargetableItemView> CREATOR = new Parcelable.Creator<TargetableItemView>() {
		public TargetableItemView createFromParcel(Parcel in) {
			return new TargetableItemView(in);
		}

		public TargetableItemView[] newArray(int size) {
			return new TargetableItemView[size];
		}
	};

	/**
	 * Getters and Setters
	 */
	public int getTargetDrawableId() {
		return mTargetDrawable;
	}

	public void setTargetDrawableId(int drawableId) {
		mTargetDrawable = drawableId;
	}

	public int getDeadDrawableId() {
		return mDeadDrawable;
	}

	public void setDeadDrawableId(int drawableId) {
		mDeadDrawable = drawableId;
	}
}
