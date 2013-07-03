package fr.tvbarthel.games.chasewhisply.ui;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

public class TargetableItemView extends DisplayableItemView {
	protected int mTargetDrawable;
	protected int mDeadDrawable;

	public TargetableItemView(Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeParcelable(mModel, i);
		out.writeInt(mDrawable);
		out.writeInt(mTargetDrawable);
		out.writeInt(mDeadDrawable);
	}

	@Override
	public void readFromParcel(Parcel in) {
		mModel = (TargetableItem) in.readParcelable(TargetableItem.class.getClassLoader());
		mDrawable = in.readInt();
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

	public void setModel(TargetableItem model) {
		mModel = model;
	}

	@Override
	public void setModel(DisplayableItem model) {
		if (!(model instanceof TargetableItem)) return;
		mModel = (TargetableItem) model;
	}

	@Override
	public TargetableItem getModel() {
		return (TargetableItem) mModel;
	}
}
