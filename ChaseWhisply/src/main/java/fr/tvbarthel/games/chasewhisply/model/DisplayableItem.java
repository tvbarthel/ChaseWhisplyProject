package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
abstract public class DisplayableItem implements Parcelable {
	// x coordinate
	private int mX;
	// y coordinate
	private int mY;
	// width
	private int mWidth;
	// height
	private int mHeight;
	// tells if the item is active/visible
	private boolean mIsVisible;

	protected DisplayableItem(Parcel in) {
		readFromParcel(in);
	}

	protected void updateVisibility(final int windowX, final int windowY, final int windowWidth, final int windowHeight) {
		final int borderLeft = windowX - mWidth;
		final int borderTop = windowY - mHeight;
		final int borderRight = borderLeft + windowWidth + mWidth;
		final int borderBottom = borderTop + windowHeight + mHeight;
		if (mX > borderLeft && mX < borderRight && mY > borderBottom && mY < borderTop) {
			mIsVisible = true;
		} else {
			mIsVisible = false;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeInt(mX);
		out.writeInt(mY);
		out.writeInt(mWidth);
		out.writeInt(mHeight);
		out.writeByte((byte) (mIsVisible ? 1 : 0));
	}

	/**
	 * read parameters from a given Parcel
	 *
	 * @param in input Parcel to read from
	 */
	public void readFromParcel(Parcel in) {
		this.mX = in.readInt();
		this.mY = in.readInt();
		this.mWidth = in.readInt();
		this.mHeight = in.readInt();
		this.mIsVisible = in.readByte() == 1;
	}

	/**
	 * Getters and Setters
	 */
	public int getX() {
		return mX;
	}

	public void setX(int mX) {
		this.mX = mX;
	}

	public int getY() {
		return mY;
	}

	public void setY(int mY) {
		this.mY = mY;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public boolean isActive() {
		return mIsVisible;
	}

	public void setActive(boolean active) {
		mIsVisible = active;
	}

}
