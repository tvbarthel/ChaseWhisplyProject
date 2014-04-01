package fr.tvbarthel.games.chasewhisply.model.inventory;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

public class DroppedByList implements Parcelable {
    public static final int DROP_RATE_BABY_DROOL = 50;
    public static final int DROP_RATE_BROKEN_HELMET_HORN = 100;
    public static final int DROP_RATE_COIN = 75;
    public static final int DROP_RATE_KING_CROWN = 75;
    public static final int DROP_RATE_GHOST_TEAR = 50;

    private final SparseIntArray mMonstersAndPercents;

    public DroppedByList() {
        mMonstersAndPercents = new SparseIntArray();
    }

    public DroppedByList(Parcel in) {
        mMonstersAndPercents = new SparseIntArray();
        readFromParcel(in);
    }

    public void addMonster(Integer monsterNameResourceId, Integer percent) {
        percent += mMonstersAndPercents.get(monsterNameResourceId);
        mMonstersAndPercents.put(monsterNameResourceId, percent);
    }

    public SparseIntArray getMonstersAndPercents() {
        return mMonstersAndPercents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final int size = mMonstersAndPercents.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            dest.writeInt(mMonstersAndPercents.keyAt(i));
            dest.writeInt(mMonstersAndPercents.valueAt(i));
        }
    }

    public void readFromParcel(Parcel in) {
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            final int key = in.readInt();
            final int value = in.readInt();
            mMonstersAndPercents.put(key, value);
        }
    }

    public static final Parcelable.Creator<DroppedByList> CREATOR = new Parcelable.Creator<DroppedByList>() {
        public DroppedByList createFromParcel(Parcel in) {
            return new DroppedByList(in);
        }

        public DroppedByList[] newArray(int size) {
            return new DroppedByList[size];
        }
    };
}
