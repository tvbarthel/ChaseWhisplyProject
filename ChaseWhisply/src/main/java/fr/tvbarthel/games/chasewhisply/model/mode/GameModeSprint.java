package fr.tvbarthel.games.chasewhisply.model.mode;


import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeSprint extends GameMode {

    private static final int RANK_LIMIT_DESERTER = 0;
    private static final int RANK_LIMIT_SOLDIER = 500;
    private static final int RANK_LIMIT_CORPORAL = 900;
    private static final int RANK_LIMIT_SERGEANT = 1300;
    private static final int RANK_LIMIT_ADMIRAL = 1650;

    public GameModeSprint() {
        super();
    }

    protected GameModeSprint(Parcel in) {
        super(in);
    }

    @Override
    public boolean isAvailable(PlayerProfile p) {
        //always available
        return true;
    }

    @Override
    public int getRank(GameInformation gameInformation) {
        final int score = ((GameInformationStandard) gameInformation).getScoreInformation().getScore();
        if (score >= RANK_LIMIT_ADMIRAL) {
            return GameModeFactory.GAME_RANK_ADMIRAL;
        } else if (score >= RANK_LIMIT_SERGEANT) {
            return GameModeFactory.GAME_RANK_SERGEANT;
        } else if (score >= RANK_LIMIT_CORPORAL) {
            return GameModeFactory.GAME_RANK_CORPORAL;
        } else if (score >= RANK_LIMIT_SOLDIER) {
            return GameModeFactory.GAME_RANK_SOLDIER;
        } else {
            return GameModeFactory.GAME_RANK_DESERTER;
        }
    }

    public static final Parcelable.Creator<GameModeSprint> CREATOR = new Parcelable.Creator<GameModeSprint>() {
        public GameModeSprint createFromParcel(Parcel in) {
            return new GameModeSprint(in);
        }

        public GameModeSprint[] newArray(int size) {
            return new GameModeSprint[size];
        }
    };

    @Override
    public String getAdmiralRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_sprint), RANK_LIMIT_ADMIRAL);
    }

    @Override
    public String getSergeantRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_sprint), RANK_LIMIT_SERGEANT);
    }

    @Override
    public String getCorporalRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_sprint), RANK_LIMIT_CORPORAL);
    }

    @Override
    public String getSoldierRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_sprint), RANK_LIMIT_SOLDIER);
    }

    @Override
    public String getDeserterRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_sprint_deserter), RANK_LIMIT_DESERTER);
    }
}

