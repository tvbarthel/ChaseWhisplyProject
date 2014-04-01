package fr.tvbarthel.games.chasewhisply.mechanics.informations;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;

public class GameInformationSurvival extends GameInformationTime {

    public static final int DIFFICULTY_EASY = 0x00000001;
    public static final int DIFFICULTY_HARD = 0x00000002;
    public static final int DIFFICULTY_HARDER = 0x00000003;
    public static final int DIFFICULTY_HARDEST = 0x00000004;

    private static final long TIME_EASY = 30000;
    private static final long TIME_HARD = 60000;
    private static final long TIME_HARDER = 90000;

    public GameInformationSurvival(GameMode gameMode, Weapon weapon, long currentTime) {
        super(gameMode, weapon, currentTime);
    }

    public GameInformationSurvival(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<GameInformationSurvival> CREATOR = new Parcelable.Creator<GameInformationSurvival>() {
        public GameInformationSurvival createFromParcel(Parcel in) {
            return new GameInformationSurvival(in);
        }

        public GameInformationSurvival[] newArray(int size) {
            return new GameInformationSurvival[size];
        }
    };

    public int getDifficulty() {
        final long timePlayed = System.currentTimeMillis() - mStartingTimeInMillis;
        int difficulty = DIFFICULTY_HARDEST;

        if (timePlayed < TIME_EASY) {
            difficulty = DIFFICULTY_EASY;
        } else if (timePlayed < TIME_HARD) {
            difficulty = DIFFICULTY_HARD;
        } else if (timePlayed < TIME_HARDER) {
            difficulty = DIFFICULTY_HARDER;
        }

        return difficulty;
    }

}
