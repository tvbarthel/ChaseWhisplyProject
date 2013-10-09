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
			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
			case GameModeFactory.GAME_TYPE_TUTORIAL:
				gameEngine = createBasicGameEngine(context, iGameEngine, gameBehavior);
				break;
		}

		return gameEngine;
	}


	/**
	 * Create Basic GameEngine
	 * shoot on touch
	 * works with timer
	 *
	 * @param context
	 * @param iGameEngine
	 * @param gameBehavior
	 * @return
	 */
	public static GameEngine createBasicGameEngine(final Context context,
												   GameEngine.IGameEngine iGameEngine,
												   GameBehavior gameBehavior) {

		return new GameEngine(context, iGameEngine, gameBehavior);
	}
}
