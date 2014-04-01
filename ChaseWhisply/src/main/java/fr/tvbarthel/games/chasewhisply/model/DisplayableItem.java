package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class DisplayableItem implements Parcelable {
    // x coordinate
    protected int mX;
    // y coordinate
    protected int mY;
    // tells if the item is active/visible
    protected boolean mIsVisible;
    // type
    protected int mType;

    public DisplayableItem() {
        mX = 0;
        mY = 0;
        mType = 0;
    }

    public DisplayableItem(int x, int y, int type) {
        mX = x;
        mY = y;
        mType = type;
    }

    protected DisplayableItem(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(mX);
        out.writeInt(mY);
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
        this.mIsVisible = in.readByte() == 1;
    }

    public static final Parcelable.Creator<DisplayableItem> CREATOR = new Parcelable.Creator<DisplayableItem>() {
        public DisplayableItem createFromParcel(Parcel in) {
            return new DisplayableItem(in);
        }

        public DisplayableItem[] newArray(int size) {
            return new DisplayableItem[size];
        }
    };

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

    public boolean isActive() {
        return mIsVisible;
    }

    public void setActive(boolean active) {
        mIsVisible = active;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setRandomCoordinates(int xMin, int xMax, int yMin, int yMax) {
        setX(MathUtils.randomize(xMin, xMax));
        setY(MathUtils.randomize(yMin, yMax));
    }
}
