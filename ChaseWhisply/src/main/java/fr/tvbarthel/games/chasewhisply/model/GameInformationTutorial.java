package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;

import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;

public class GameInformationTutorial extends GameInformation {

	public static final int STEP_WELCOME = 0;
	public static final int STEP_UI_WELCOME = 1;
	public static final int STEP_CROSSHAIR = 2;
	public static final int STEP_COMBO = 3;
	public static final int STEP_AMMO = 4;
	public static final int STEP_AMMO_2 = 5;
	public static final int STEP_SCORE = 6;
	public static final int STEP_SERIOUS_THINGS = 7;
	public static final int STEP_END = 8;

	private int mCurrentStep;

	public GameInformationTutorial(long spawningTime, Weapon weapon) {
		super(spawningTime, weapon);
		mCurrentStep = STEP_WELCOME;
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		super.writeToParcel(out, i);
		out.writeInt(mCurrentStep);
	}

	@Override
	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mCurrentStep = in.readInt();
	}

	public int nextStep() {
		mScoreInformation.increaseScore(10);
		return ++mCurrentStep;
	}

	public void setCurrentStep(int step) {
		mCurrentStep = step;
	}

	public int getCurrentStep() {
		return mCurrentStep;
	}
}
