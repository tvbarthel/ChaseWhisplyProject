package fr.tvbarthel.games.chasewhisply.model.mode;


import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeTutorial extends GameMode {

    public GameModeTutorial() {
        super();
    }

    protected GameModeTutorial(Parcel in) {
        super(in);
    }

    @Override
    public boolean isAvailable(PlayerProfile p) {
        //always available
        return true;
    }

    @Override
    public int getRank(GameInformation gameInformation) {
        //always get the rank admiral for tutorial
        return GameModeFactory.GAME_RANK_ADMIRAL;
    }

    public static final Parcelable.Creator<GameModeTutorial> CREATOR = new Parcelable.Creator<GameModeTutorial>() {
        public GameModeTutorial createFromParcel(Parcel in) {
            return new GameModeTutorial(in);
        }

        public GameModeTutorial[] newArray(int size) {
            return new GameModeTutorial[size];
        }
    };

}
