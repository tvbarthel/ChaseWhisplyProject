package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WeightedCoordinate implements Parcelable {
	private float mValue;
	private float mWeight;

	public WeightedCoordinate(float value, float weight) {
		mValue = value;
		mWeight = weight;
	}

	public WeightedCoordinate() {
		mValue = 0;
		mWeight = 0;
	}

	public WeightedCoordinate(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(mValue);
		dest.writeFloat(mWeight);
	}

	public void readFromParcel(Parcel in) {
		mValue = in.readFloat();
		mWeight = in.readFloat();
	}

	public static final Parcelable.Creator<WeightedCoordinate> CREATOR = new Parcelable.Creator<WeightedCoordinate>() {
		public WeightedCoordinate createFromParcel(Parcel in) {
			return new WeightedCoordinate(in);
		}

		public WeightedCoordinate[] newArray(int size) {
			return new WeightedCoordinate[size];
		}
	};

	public void lowerWeight(float i) {
		mWeight = Math.max(0, mWeight - i);
	}

	public void lowerWeight() {
		lowerWeight(1f);
	}

	/**
	 * Setters and Getters
	 */

	public void setWeight(float weight) {
		mWeight = weight;
	}

	public float getWeight() {
		return mWeight;
	}

	public void setValue(float value) {
		mValue = value;
	}

	public float getValue() {
		return mValue;
	}

	public float getWeightedValue() {
		return mValue * mWeight;
	}
}
