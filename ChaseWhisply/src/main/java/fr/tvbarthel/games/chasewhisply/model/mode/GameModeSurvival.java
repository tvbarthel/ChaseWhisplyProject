package fr.tvbarthel.games.chasewhisply.model.mode;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationSurvival;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;


public class GameModeSurvival extends GameMode {

	private static final int RANK_LIMIT_DESERTER = 10;
	private static final int RANK_LIMIT_SOLDIER = 100;
	private static final int RANK_LIMIT_CORPORAL = 200;
	private static final int RANK_LIMIT_SERGEANT = 300;

	public GameModeSurvival() {
		super();
	}

	protected GameModeSurvival(Parcel in) {
		super(in);
	}

	@Override
	public boolean isAvailable(PlayerProfile p) {
		//only available if player level > required level
		return p.getLevelInformation().getLevel() >= this.getRequiredCondition();
	}

	@Override
	public int getRank(GameInformation gameInformation) {
		return processRank((GameInformationSurvival) gameInformation);
	}

	public static final Parcelable.Creator<GameModeSurvival> CREATOR = new Parcelable.Creator<GameModeSurvival>() {
		public GameModeSurvival createFromParcel(Parcel in) {
			return new GameModeSurvival(in);
		}

		public GameModeSurvival[] newArray(int size) {
			return new GameModeSurvival[size];
		}
	};

	protected int processRank(GameInformationSurvival g) {
		final int score = g.getScoreInformation().getScore();
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
