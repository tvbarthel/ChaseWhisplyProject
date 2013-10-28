package fr.tvbarthel.games.chasewhisply.model.mode;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationMemorize;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeMemorize extends GameMode {
	private static final int RANK_LIMIT_DESERTER = 2;
	private static final int RANK_LIMIT_SOLDIER = 4;
	private static final int RANK_LIMIT_CORPORAL = 6;
	private static final int RANK_LIMIT_SERGEANT = 8;

	public GameModeMemorize() {
		super();
	}

	protected GameModeMemorize(Parcel in) {
		super(in);
	}

	@Override
	public boolean isAvailable(PlayerProfile p) {
		//only available if player level > required level
		return p.getLevelInformation().getLevel() >= this.getRequiredCondition();
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
		final int score = g.getCurrentWaveNumber();
		if (score < RANK_LIMIT_DESERTER) {
			return GameModeFactory.GAME_RANK_DESERTER;
		} else if (score < RANK_LIMIT_SOLDIER) {
			return GameModeFactory.GAME_RANK_SOLDIER;
		} else if (score < RANK_LIMIT_CORPORAL) {
			return GameModeFactory.GAME_RANK_CORPORAL;
		} else if (score < RANK_LIMIT_SERGEANT) {
			return GameModeFactory.GAME_RANK_SERGEANT;
		} else {
			return GameModeFactory.GAME_RANK_ADMIRAL;
		}
	}
}
