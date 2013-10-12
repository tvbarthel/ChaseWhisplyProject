package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;

import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;

public class GameInformationTutorial extends GameInformation {

	public static final int STEP_WELCOME = 0x00000000;
	public static final int STEP_UI_WELCOME = 0x0000001;
	public static final int STEP_CROSSHAIR = 0x0000002;
	public static final int STEP_AMMO = 0x0000003;
	public static final int STEP_COMBO = 0x0000004;
	public static final int STEP_SERIOUS_THINGS = 0x0000005;
	public static final int STEP_END = 0x00000006;

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
