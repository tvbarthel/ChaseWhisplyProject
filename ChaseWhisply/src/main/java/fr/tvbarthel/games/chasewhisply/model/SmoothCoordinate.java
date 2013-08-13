package fr.tvbarthel.games.chasewhisply.model;


import java.util.ArrayList;

public class SmoothCoordinate {
	private int mCursor;
	private ArrayList<WeightedValue> mValues;
	private float mNoise;
	private int mTempSize;
	private float mInitialWeight;

	public SmoothCoordinate(float noise, int tempSize, float initialWeight) {
		mNoise = noise;
		mTempSize = tempSize;
		mInitialWeight = initialWeight;
		mValues = new ArrayList<WeightedValue>();
		mCursor = 0;
	}

	public float update(float newCoordinate) {
		float smoothValue = getSmoothValue();
		if (shouldWeChangeValue(smoothValue, newCoordinate)) {
			lowerCoordinateWeight(getLowerWeightRatio(smoothValue, newCoordinate));
			smoothValue = changeValue(smoothValue, newCoordinate);
		}
		return smoothValue;
	}

	public float getSmoothValue() {
		float smoothValue = 0f;
		float totalWeight = 0f;

		for (WeightedValue weightedValue : mValues) {
			smoothValue += weightedValue.getWeightedValue();
			totalWeight += weightedValue.getWeight();
		}

		return smoothValue / totalWeight;
	}

	private float changeValue(float oldCoordinate, float newCoordinate) {
		if (mValues.size() != 0 && !isEqualSign(oldCoordinate, newCoordinate)) {
			reset();
		}

		if (mValues.size() < mTempSize) {
			mValues.add(mCursor, new WeightedValue(newCoordinate, mInitialWeight));
		} else {
			mValues.set(mCursor, new WeightedValue(newCoordinate, mInitialWeight));
		}
		mCursor = (mCursor + 1) % mTempSize;
		return getSmoothValue();
	}

	private boolean shouldWeChangeValue(float oldValue, float newValue) {
		boolean shouldWeChangeCoordinate = false;
		if (mValues.size() != 0) {
			if (isAbsDiffGreaterThanNoise(oldValue, newValue)) {
				shouldWeChangeCoordinate = true;
			}
		} else {
			shouldWeChangeCoordinate = true;
		}
		return shouldWeChangeCoordinate;
	}

	public void lowerCoordinateWeight(float i) {
		for (WeightedValue weightedValue : mValues) {
			weightedValue.lowerWeight(i);
		}
	}

	private float getLowerWeightRatio(float oldCoordinate, float newCoordinate) {
		final float delta = Math.abs(oldCoordinate - newCoordinate);
		float lowerWeight = 0;
		if (delta >= 0.14) {
			//"huge" movement
			lowerWeight = mInitialWeight / 5;
		} else if (delta >= 0.13) {
			//big movement
			lowerWeight = mInitialWeight / 6;
		} else if (delta >= 0.12) {
			//average movement
			lowerWeight = mInitialWeight / 8;
		} else if (delta >= 0.11) {
			//small movement
			lowerWeight = mInitialWeight / 12;
		} else if (delta >= 0.10) {
			//tiny movement
			lowerWeight = mInitialWeight / 14;
		}
		return lowerWeight;
	}

	private boolean isEqualSign(float a, float b) {
		return a * b > 0;
	}

	private boolean isAbsDiffGreaterThanNoise(float a, float b) {
		return Math.abs(a - b) > mNoise;
	}

	private void reset() {
		mValues.clear();
		mCursor = 0;
	}
}
