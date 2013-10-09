package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;

import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;

public class GameViewFactory {

	/**
	 * Factory manage to create right gameView according to gameMode
	 *
	 * @param c
	 * @param g
	 * @param gameMode
	 * @return
	 */
	public static GameView createGameViewByGameMode(Context c, GameInformation g, int gameMode) {
		GameView gameView;
		switch (gameMode) {
			case GameModeFactory.GAME_TYPE_REMAINING_TIME:
			case GameModeFactory.GAME_TYPE_SURVIVAL:
			case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
				gameView = createStandardGameView(c, g);
				break;
			default:
				gameView = createStandardGameView(c, g);
				break;
		}

		return gameView;
	}


	/**
	 * Create GameView which display :
	 * cross hair in the middle of the screen
	 * ammo amount in bottom right corner
	 * remaining time in top left corner
	 * current combo just on the right of the crossHair
	 * the score in the bottom left corner
	 * time in top left corner
	 *
	 * @param c context
	 * @param g model of the view
	 * @return game view
	 */
	public static GameView createStandardGameView(Context c, GameInformation g) {
		return new StandardGameView(c, g);
	}


}
