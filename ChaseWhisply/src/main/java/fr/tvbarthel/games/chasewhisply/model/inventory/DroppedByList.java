package fr.tvbarthel.games.chasewhisply.model.inventory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class DroppedByList implements Parcelable {
	public static final int DROP_RATE_BABY_DROOL = 30;
	public static final int DROP_RATE_BROKEN_HELMET_HORN = 50;
	public static final int DROP_RATE_COIN = 6;
	public static final int DROP_RATE_KING_CROWN = 50;

	private final HashMap<Integer, Integer> mMonstersAndPercents;

	public DroppedByList() {
		mMonstersAndPercents = new HashMap<Integer, Integer>();
	}

	public DroppedByList(Parcel in) {
		mMonstersAndPercents = new HashMap<Integer, Integer>();
		readFromParcel(in);
	}

	public void addMonster(Integer monsterNameResourceId, Integer percent) {
		if (mMonstersAndPercents.containsKey(monsterNameResourceId)) {
			percent += mMonstersAndPercents.get(monsterNameResourceId);
		}
		mMonstersAndPercents.put(monsterNameResourceId, percent);
	}

	public HashMap<Integer, Integer> getMonstersAndPercents() {
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
		for (Map.Entry<Integer, Integer> entry : mMonstersAndPercents.entrySet()) {
			dest.writeInt(entry.getKey());
			dest.writeInt(entry.getValue());
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
