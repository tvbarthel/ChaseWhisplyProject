package fr.tvbarthel.games.chasewhisply.mechanics.informations;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;


public class GameInformationTwentyInARow extends GameInformationTime {
    protected int mCurrentStack;

    public GameInformationTwentyInARow(GameMode gameMode, Weapon weapon, long currentTime) {
        super(gameMode, weapon, currentTime);
        mCurrentStack = 0;
    }

    public GameInformationTwentyInARow(Parcel in) {
        super(in);
    }

    public int increaseCurrentStack() {
        return ++mCurrentStack;
    }

    public void resetCurrentStack() {
        mCurrentStack = 0;
    }

    public int getCurrentStack() {
        return mCurrentStack;
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mCurrentStack = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        super.writeToParcel(out, i);
        out.writeInt(mCurrentStack);
    }

    public static final Parcelable.Creator<GameInformationTwentyInARow> CREATOR = new Parcelable.Creator<GameInformationTwentyInARow>() {
        public GameInformationTwentyInARow createFromParcel(Parcel in) {
            return new GameInformationTwentyInARow(in);
        }

        public GameInformationTwentyInARow[] newArray(int size) {
            return new GameInformationTwentyInARow[size];
        }
    };
}
