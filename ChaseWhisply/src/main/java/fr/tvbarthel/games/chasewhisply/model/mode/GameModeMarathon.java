package fr.tvbarthel.games.chasewhisply.model.mode;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeMarathon extends GameMode {

    private static final int RANK_LIMIT_DESERTER = 0;
    private static final int RANK_LIMIT_SOLDIER = 3500;
    private static final int RANK_LIMIT_CORPORAL = 5000;
    private static final int RANK_LIMIT_SERGEANT = 6500;
    private static final int RANK_LIMIT_ADMIRAL = 7300;

    public GameModeMarathon() {
        super();
    }

    protected GameModeMarathon(Parcel in) {
        super(in);
    }

    @Override
    public boolean isAvailable(PlayerProfile p) {
        return p.getRankByGameMode(GameModeFactory.createTwentyInARow(0)) >= GameModeFactory.GAME_RANK_CORPORAL;
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

    public static final Parcelable.Creator<GameModeMarathon> CREATOR = new Parcelable.Creator<GameModeMarathon>() {
        public GameModeMarathon createFromParcel(Parcel in) {
            return new GameModeMarathon(in);
        }

        public GameModeMarathon[] newArray(int size) {
            return new GameModeMarathon[size];
        }
    };

    @Override
    public String getAdmiralRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_marathon), RANK_LIMIT_ADMIRAL);
    }

    @Override
    public String getSergeantRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_marathon), RANK_LIMIT_SERGEANT);
    }

    @Override
    public String getCorporalRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_marathon), RANK_LIMIT_CORPORAL);
    }

    @Override
    public String getSoldierRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_marathon), RANK_LIMIT_SOLDIER);
    }

    @Override
    public String getDeserterRankRule(Resources res) {
        return String.format(res.getString(R.string.game_mode_rank_rules_marathon_deserter), RANK_LIMIT_DESERTER);
    }
}
