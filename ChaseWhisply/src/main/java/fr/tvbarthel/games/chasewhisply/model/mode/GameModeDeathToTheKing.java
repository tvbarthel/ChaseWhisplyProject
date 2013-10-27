package fr.tvbarthel.games.chasewhisply.model.mode;


import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTime;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class GameModeDeathToTheKing extends GameMode {

	private static final int RANK_LIMIT_ADMIRAL = 2000;
	private static final int RANK_LIMIT_SERGEANT = 4000;
	private static final int RANK_LIMIT_CORPORAL = 6000;
	private static final int RANK_LIMIT_SOLDIER = 8000;

	public GameModeDeathToTheKing() {
		super();
	}

	protected GameModeDeathToTheKing(Parcel in) {
		super(in);
	}

	@Override
	public boolean isAvailable(PlayerProfile p) {
		//only available if player level > required level
		return p.getLevelInformation().getLevel() >= this.getRequiredCondition();
	}

	@Override
	public int getRank(GameInformation gameInformation) {
		return processRank((GameInformationTime) gameInformation);
	}

	public static final Parcelable.Creator<GameModeDeathToTheKing> CREATOR = new Parcelable.Creator<GameModeDeathToTheKing>() {
		public GameModeDeathToTheKing createFromParcel(Parcel in) {
			return new GameModeDeathToTheKing(in);
		}

		public GameModeDeathToTheKing[] newArray(int size) {
			return new GameModeDeathToTheKing[size];
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
}

