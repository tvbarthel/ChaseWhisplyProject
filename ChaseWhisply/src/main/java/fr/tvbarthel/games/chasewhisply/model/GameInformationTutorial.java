package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;

import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;

public class GameInformationTutorial extends GameInformation {

	public static final int STEP_WELCOME = 0x00000000;
	public static final int STEP_END = 0x00000001;

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

	public void setCurrentStep(int step) {
		mCurrentStep = step;
	}

	public int getCurrentStep() {
		return mCurrentStep;
	}
}
