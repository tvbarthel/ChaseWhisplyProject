package fr.tvbarthel.games.chasewhisply.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

import fr.tvbarthel.games.chasewhisply.ui.DisplayableItemView;

public class GameInformation implements Parcelable {
	private int mScore;
	private long mCurrentTime;
	private int mAmmunition;
	private ArrayList<DisplayableItemView> mItems;

	public GameInformation() {
		mScore = 0;
		mCurrentTime = 0;
		mAmmunition = 0;
		mItems = new ArrayList<DisplayableItemView>();
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
		mCurrentTime = in.readLong();
		mAmmunition = in.readInt();
		mItems = new ArrayList<DisplayableItemView>(Arrays.asList((
				DisplayableItemView[]) in.readParcelableArray(DisplayableItemView.class.getClassLoader())));
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeInt(mScore);
		out.writeLong(mCurrentTime);
		out.writeInt(mAmmunition);
		out.writeParcelableArray((DisplayableItemView[]) mItems.toArray(), i);
	}

	public static final Parcelable.Creator<GameInformation> CREATOR = new Parcelable.Creator<GameInformation>() {
		public GameInformation createFromParcel(Parcel in) {
			return new GameInformation(in);
		}

		public GameInformation[] newArray(int size) {
			return new GameInformation[size];
		}
	};
}
