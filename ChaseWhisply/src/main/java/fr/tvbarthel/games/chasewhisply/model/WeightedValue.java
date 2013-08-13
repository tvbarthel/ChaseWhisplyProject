package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WeightedValue implements Parcelable {
	private float mValue;
	private float mWeight;

	public WeightedValue(float value, float weight) {
		mValue = value;
		mWeight = weight;
	}

	public WeightedValue() {
		mValue = 0;
		mWeight = 0;
	}

	public WeightedValue(Parcel in) {
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

	public static final Parcelable.Creator<WeightedValue> CREATOR = new Parcelable.Creator<WeightedValue>() {
		public WeightedValue createFromParcel(Parcel in) {
			return new WeightedValue(in);
		}

		public WeightedValue[] newArray(int size) {
			return new WeightedValue[size];
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
