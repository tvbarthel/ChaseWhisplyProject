package fr.tvbarthel.games.chasewhisply.mechanics.informations;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;

public class GameInformationTutorial extends GameInformationStandard {

    public static final int STEP_WELCOME = 0;
    public static final int STEP_UI_WELCOME = 1;
    public static final int STEP_CROSSHAIR = 2;
    public static final int STEP_COMBO = 3;
    public static final int STEP_AMMO = 4;
    public static final int STEP_AMMO_2 = 5;
    public static final int STEP_SCORE = 6;
    public static final int STEP_SERIOUS_THINGS = 7;
    public static final int STEP_TARGET = 8;
    public static final int STEP_KILL = 9;
    public static final int STEP_CONGRATULATION = 10;
    public static final int STEP_TARGET_2 = 11;
    public static final int STEP_KILL_2 = 12;
    public static final int STEP_CONGRATULATION_2 = 13;
    public static final int STEP_END = 14;

    private int mCurrentStep;

    public GameInformationTutorial(GameMode gameMode, Weapon weapon) {
        super(gameMode, weapon);
        mCurrentStep = STEP_WELCOME;
    }

    protected GameInformationTutorial(Parcel in) {
        super(in);
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
        mScoreInformation.increaseScore(20);
        return ++mCurrentStep;
    }

    public void setCurrentStep(int step) {
        mCurrentStep = step;
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }

    public static final Parcelable.Creator<GameInformationTutorial> CREATOR = new Parcelable.Creator<GameInformationTutorial>() {
        public GameInformationTutorial createFromParcel(Parcel in) {
            return new GameInformationTutorial(in);
        }

        public GameInformationTutorial[] newArray(int size) {
            return new GameInformationTutorial[size];
        }
    };
}
