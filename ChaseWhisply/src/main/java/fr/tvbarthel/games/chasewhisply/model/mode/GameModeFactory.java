package fr.tvbarthel.games.chasewhisply.model.mode;


import fr.tvbarthel.games.chasewhisply.R;

public class GameModeFactory {

    public static final int GAME_TYPE_TUTORIAL = 0x00000000;
    public static final int GAME_TYPE_REMAINING_TIME = 0x00000001;
    public static final int GAME_TYPE_LIMITED_TARGETS = 0x00000002;
    public static final int GAME_TYPE_SURVIVAL = 0x00000003;
    public static final int GAME_TYPE_DEATH_TO_THE_KING = 0x00000004;
    public static final int GAME_TYPE_OVERALL_RANKING = 0x00000005;
    public static final int GAME_TYPE_TWENTY_IN_A_ROW = 0x00000006;
    public static final int GAME_TYPE_MEMORIZE = 0x00000007;

    public static final int GAME_RANK_DESERTER = 0x00000000;
    public static final int GAME_RANK_SOLDIER = 0x00000001;
    public static final int GAME_RANK_CORPORAL = 0x00000002;
    public static final int GAME_RANK_SERGEANT = 0x00000003;
    public static final int GAME_RANK_ADMIRAL = 0x00000004;

    /**
     * game mode for learn how to play.
     * Always available and no leaderboard.
     *
     * @return
     */
    public static GameMode createTutorialGame() {
        final GameMode g = new GameModeTutorial();
        g.setType(GAME_TYPE_TUTORIAL);
        g.setImage(R.drawable.ic_icon_tutorial);
        g.setBonusAvailable(false);
        g.setTitle(R.string.game_mode_title_tutorial);
        return g;
    }

    public static GameMode createRemainingTimeGame(int level) {
        final GameMode g;
        switch (level) {
            case 1:
                g = new GameModeSprint();
                g.setImage(R.drawable.ic_icon_time_based_game_30_s);
                g.setLeaderboardStringId(R.string.leaderboard_scouts_first);
                g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_sprint);
                g.setLongDescription(R.string.game_mode_description_sprint);
                g.setTitle(R.string.game_mode_title_sprint);
                break;

            case 3:
                g = new GameModeMarathon();
                g.setImage(R.drawable.ic_icon_time_based_game_90_s);
                g.setLeaderboardStringId(R.string.leaderboard_prove_your_stamina);
                g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_marathon);
                g.setRequiredMessage(R.string.game_mode_required_message_marathon);
                g.setTitle(R.string.game_mode_title_marathon);
                g.setLongDescription(R.string.game_mode_description_marathon);
                break;

            default:
                g = new GameMode();
                g.setImage(R.drawable.ghost);
        }
        g.setType(GAME_TYPE_REMAINING_TIME);
        g.setLevel(level);
        g.setBonusAvailable(true);
        return g;
    }

/*	public static GameMode createLimitedTargetsGame(int level) {
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
	}*/

    public static GameMode createSurvivalGame(int level) {
        final GameMode g = new GameModeSurvival();
        g.setType(GAME_TYPE_SURVIVAL);
        g.setLevel(level);
        g.setImage(R.drawable.ic_icon_time_based_game_inf);
        g.setLeaderboardStringId(R.string.leaderboard_the_final_battle);
        g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_survival);
        g.setRequiredMessage(R.string.game_mode_required_message_survival);
        g.setBonusAvailable(true);
        g.setTitle(R.string.game_mode_title_survival);
        g.setLongDescription(R.string.game_mode_description_survival);
        return g;
    }

    public static GameMode createKillTheKingGame(int level) {
        final GameMode g = new GameModeDeathToTheKing();
        g.setType(GAME_TYPE_DEATH_TO_THE_KING);
        g.setLevel(level);
        g.setImage(R.drawable.ic_icon_death_to_the_king);
        g.setLeaderboardStringId(R.string.leaderboard_death_to_the_king);
        g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_death_to_the_king);
        g.setRequiredMessage(R.string.game_mode_required_message_death_to_the_king);
        g.setBonusAvailable(false);
        g.setTitle(R.string.game_mode_title_death_to_the_king);
        g.setLongDescription(R.string.game_mode_description_death_to_the_king);
        return g;
    }

    public static GameMode createTwentyInARow(int level) {
        final GameMode g = new GameModeTwentyInARow();
        g.setType(GAME_TYPE_TWENTY_IN_A_ROW);
        g.setLevel(level);
        g.setImage(R.drawable.ic_icon_twenty_in_a_row);
        //TODO
        g.setLeaderboardStringId(R.string.leaderboard_everything_is_an_illusion);
        g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_twenty_in_a_row);
        g.setRequiredMessage(R.string.game_mode_required_message_twenty_in_a_row);
        g.setTitle(R.string.game_mode_title_twenty_in_a_row);
        g.setLongDescription(R.string.game_mode_description_twenty_in_a_row);
        g.setBonusAvailable(false);
        return g;
    }

    public static GameMode createMemorize(int level) {
        final GameModeMemorize g = new GameModeMemorize();
        g.setType(GAME_TYPE_MEMORIZE);
        g.setLevel(level);
        g.setImage(R.drawable.ic_icon_memorize);
        //TODO
        g.setLeaderboardStringId(R.string.leaderboard_brainteaser);
        g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_memorize);
        g.setRequiredMessage(R.string.game_mode_required_message_memorize);
        g.setTitle(R.string.game_mode_title_memorize);
        g.setLongDescription(R.string.game_mode_description_memorize);
        return g;
    }

    public static GameMode createOverallRanking() {
        final GameMode g = new GameMode();
        g.setType(GAME_TYPE_OVERALL_RANKING);
        g.setImage(R.drawable.ic_icon_overall_ranking);
        g.setLeaderboardStringId(R.string.leaderboard_overall_ranking);
        g.setLeaderboardDescriptionStringId(R.string.leaderboard_chooser_title_overall_ranking);
        return g;
    }
}
