package fr.tvbarthel.games.chasewhisply.model.mode;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationMemorize;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeMemorize extends GameMode {
    private static final int RANK_LIMIT_DESERTER = 0;
    private static final int RANK_LIMIT_SOLDIER = 2;
    private static final int RANK_LIMIT_CORPORAL = 4;
    private static final int RANK_LIMIT_SERGEANT = 7;
    private static final int RANK_LIMIT_ADMIRAL = 10;

    public GameModeMemorize() {
        super();
    }

    protected GameModeMemorize(Parcel in) {
        super(in);
    }

    @Override
    public boolean isAvailable(PlayerProfile p) {
        //only available if player level > required level
        return p.getRankByGameMode(GameModeFactory.createRemainingTimeGame(3)) >= GameModeFactory.GAME_RANK_CORPORAL;
    }

    @Override
    public int getRank(GameInformation gameInformation) {
        return processRank((GameInformationMemorize) gameInformation);
    }

    public static final Parcelable.Creator<GameModeMemorize> CREATOR = new Parcelable.Creator<GameModeMemorize>() {
        public GameModeMemorize createFromParcel(Parcel in) {
            return new GameModeMemorize(in);
        }

        public GameModeMemorize[] newArray(int size) {
            return new GameModeMemorize[size];
        }
    };

    protected int processRank(GameInformationMemorize g) {
        final int score = g.getCurrentWaveNumber() - 1;

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

    @Override
    public String getAdmiralRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_memorize, RANK_LIMIT_ADMIRAL);
    }

    @Override
    public String getSergeantRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_memorize, RANK_LIMIT_SERGEANT);
    }

    @Override
    public String getCorporalRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_memorize, RANK_LIMIT_CORPORAL);
    }

    @Override
    public String getSoldierRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_memorize, RANK_LIMIT_SOLDIER);
    }

    @Override
    public String getDeserterRankRule(Resources res) {
        return res.getString(R.string.game_mode_rank_rules_memorize, RANK_LIMIT_DESERTER);
    }
}
