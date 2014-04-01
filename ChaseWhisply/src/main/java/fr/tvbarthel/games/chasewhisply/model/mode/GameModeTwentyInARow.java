package fr.tvbarthel.games.chasewhisply.model.mode;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTime;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeTwentyInARow extends GameMode {
    private static final int RANK_LIMIT_ADMIRAL = 20000;
    private static final int RANK_LIMIT_SERGEANT = 25000;
    private static final int RANK_LIMIT_CORPORAL = 30000;
    private static final int RANK_LIMIT_SOLDIER = 35000;

    public GameModeTwentyInARow() {
        super();
    }

    protected GameModeTwentyInARow(Parcel in) {
        super(in);
    }

    @Override
    public boolean isAvailable(PlayerProfile p) {
        return p.getRankByGameMode(GameModeFactory.createRemainingTimeGame(1)) >= GameModeFactory.GAME_RANK_CORPORAL;
    }

    @Override
    public int getRank(GameInformation gameInformation) {
        return processRank((GameInformationTime) gameInformation);
    }

    public static final Parcelable.Creator<GameModeTwentyInARow> CREATOR = new Parcelable.Creator<GameModeTwentyInARow>() {
        public GameModeTwentyInARow createFromParcel(Parcel in) {
            return new GameModeTwentyInARow(in);
        }

        public GameModeTwentyInARow[] newArray(int size) {
            return new GameModeTwentyInARow[size];
        }
    };

    protected int processRank(GameInformationTime g) {
        final long score = g.getPlayingTime();
        if (score < RANK_LIMIT_ADMIRAL) {
            return GameModeFactory.GAME_RANK_ADMIRAL;
        } else if (score < RANK_LIMIT_SERGEANT) {
            return GameModeFactory.GAME_RANK_SERGEANT;
        } else if (score < RANK_LIMIT_CORPORAL) {
            return GameModeFactory.GAME_RANK_CORPORAL;
        } else if (score < RANK_LIMIT_SOLDIER) {
            return GameModeFactory.GAME_RANK_SOLDIER;
        } else {
            return GameModeFactory.GAME_RANK_DESERTER;
        }
    }

    @Override
    public String getAdmiralRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_twenty_in_a_row, RANK_LIMIT_ADMIRAL / 1000);
    }

    @Override
    public String getSergeantRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_twenty_in_a_row, RANK_LIMIT_SERGEANT / 1000);
    }

    @Override
    public String getCorporalRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_twenty_in_a_row, RANK_LIMIT_CORPORAL / 1000);
    }

    @Override
    public String getSoldierRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_twenty_in_a_row, RANK_LIMIT_SOLDIER / 1000);
    }

    @Override
    public String getDeserterRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_twenty_in_a_row_deserter);
    }
}
