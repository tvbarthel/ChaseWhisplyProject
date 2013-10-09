package fr.tvbarthel.games.chasewhisply.mechanics.engine;


import android.content.Context;

import fr.tvbarthel.games.chasewhisply.model.GameBehavior;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;

public class GameEngineFactory {

	/**
	 * Create gameEngine according to the gameMode
	 *
	 * @param context
	 * @param iGameEngine
	 * @param gameBehavior
	 * @param gameMode
	 * @return
	 */
	public static GameEngine createGameEngineByGameMode(final Context context,
														GameEngine.IGameEngine iGameEngine,
														GameBehavior gameBehavior, int gameMode) {
		GameEngine gameEngine = null;

		switch (gameMode) {
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
			case GameModeFactory.GAME_TYPE_SURVIVAL:
				gameEngine = new StandardGameEngine(context, iGameEngine, gameBehavior);
				break;
			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
				gameEngine = new MissionGameEngine(context, iGameEngine, gameBehavior);
				break;
			case GameModeFactory.GAME_TYPE_TUTORIAL:
				gameEngine = new TutorialGameEngine(context, iGameEngine, gameBehavior);
				break;
		}

		return gameEngine;
	}
}
