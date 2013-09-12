package fr.tvbarthel.games.chasewhisply.model;


import fr.tvbarthel.games.chasewhisply.R;

public class GameModeFactory {

	public static final int GAME_TYPE_REMAINING_TIME = 0x00000001;
	public static final int GAME_TYPE_LIMITED_TARGETS = 0x00000002;
	public static final int GAME_TYPE_SURVIVAL = 0x00000003;

	public static GameMode createRemainingTimeGame(int level) {
		final GameMode g = new GameMode() {
			@Override
			public boolean isAvailable(PlayerProfile p) {
				//always available
				return true;
			}
		};
		g.setType(GAME_TYPE_REMAINING_TIME);
		g.setLevel(level);
		switch (level) {
			case 1:
				g.setRules(R.string.game_mode_time_limited_level_1);
				g.setImage(R.drawable.ic_icon_time_based_game_30_s);
				g.setLeaderboardStringId(R.string.leaderboard_30_seconds);
				g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_time_limited_level_1_description);
				break;

			case 2:
				g.setRules(R.string.game_mode_time_limited_level_2);
				g.setImage(R.drawable.ic_icon_time_based_game_60_s);
				g.setLeaderboardStringId(R.string.leaderboard_60_seconds);
				g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_time_limited_level_2_description);
				break;

			case 3:
				g.setRules(R.string.game_mode_time_limited_level_3);
				g.setImage(R.drawable.ic_icon_time_based_game_90_s);
				g.setLeaderboardStringId(R.string.leaderboard_90_seconds);
				g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_time_limited_level_3_description);
				break;

			default:
				g.setRules(R.string.game_mode_remaining_time);
				g.setImage(R.drawable.ghost);
		}
		return g;
	}

	public static GameMode createLimitedTargetsGame(int level) {
		final GameMode g = new GameMode() {
			@Override
			public boolean isAvailable(PlayerProfile p) {
				//always available
				return true;
			}
		};
		g.setType(GAME_TYPE_LIMITED_TARGETS);
		g.setLevel(level);
		g.setRules(R.string.game_mode_target_limited);
		g.setImage(R.drawable.ghost_targeted);
		return g;
	}

	public static GameMode createSurvivalGame(int level) {
		final GameMode g = new GameMode() {
			@Override
			public boolean isAvailable(PlayerProfile p) {
				//only available if player level > required level
				return p.getLevelInformation().getLevel() >= this.getRequiredLevel();
			}
		};
		g.setType(GAME_TYPE_SURVIVAL);
		g.setLevel(level);
		g.setRules(R.string.game_mode_survival);
		g.setImage(R.drawable.ic_icon_time_based_game_inf);
		g.setLeaderboardStringId(R.string.leaderboard_survival);
		g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_survival_description);
		g.setRequiredLevel(2);
		g.setRequiredMessage(R.string.game_mode_survival_required_message);
		return g;
	}
}
