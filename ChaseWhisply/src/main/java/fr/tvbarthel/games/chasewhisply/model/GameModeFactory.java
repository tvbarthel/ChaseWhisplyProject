package fr.tvbarthel.games.chasewhisply.model;


import fr.tvbarthel.games.chasewhisply.R;

public class GameModeFactory {

	public static final int GAME_TYPE_REMAINING_TIME = 0x00000001;
	public static final int GAME_TYPE_LIMITED_TARGETS = 0x00000002;

	public static GameMode createRemainingTimeGame(int level) {
		final GameMode g = new GameMode();
		g.setType(GAME_TYPE_REMAINING_TIME);
		g.setLevel(level);
		g.setRules(R.string.game_mode_remaining_time);
		g.setImage(R.drawable.ghost);
		return g;
	}

	public static GameMode createLimitedTargetsGame(int level) {
		final GameMode g = new GameMode();
		g.setType(GAME_TYPE_LIMITED_TARGETS);
		g.setLevel(level);
		g.setRules(R.string.game_mode_target_limited);
		g.setImage(R.drawable.ghost_targeted);
		return g;
	}
}
