package fr.tvbarthel.games.chasewhisply.mechanics;


import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.model.WeaponFactory;

public class GameInformationFactory {

	private static final long DEFAULT_REMAINING_TIME = 30 * 1000;
	private static final long DEFAULT_SPAWNING_TIME = 1000;
	private static final int DEMO_WORLD_ENEMY_NUMBER = 20;

	/**
	 * create basic world for demo purpose
	 *
	 * @param cameraHorizontalViewAngle from camera params
	 * @param cameraVerticalViewAngle   from camera params
	 * @return demo world
	 */
	public static GameInformation createDemoWorld(float cameraHorizontalViewAngle, float cameraVerticalViewAngle) {
		//create basic game information
		GameInformation demoWorld = new GameInformation(DEFAULT_REMAINING_TIME, DEFAULT_SPAWNING_TIME, WeaponFactory.createBasicWeapon());
		demoWorld.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		demoWorld.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));

		//place ghost
		for (int i = 0; i < DEMO_WORLD_ENEMY_NUMBER; i++) {
			demoWorld.addTargetableItem(DisplayableItemFactory.createEasyGhost());
		}

		return demoWorld;
	}

	/**
	 * create empty world for demo purpose
	 *
	 * @param cameraHorizontalViewAngle from camera params
	 * @param cameraVerticalViewAngle   from camera params
	 * @return demo world
	 */
	public static GameInformation createEmptyDemoWorld(float cameraHorizontalViewAngle, float cameraVerticalViewAngle) {
		//create basic game information
		GameInformation demoWorld = new GameInformation(DEFAULT_REMAINING_TIME, DEFAULT_SPAWNING_TIME, WeaponFactory.createBasicWeapon());
		demoWorld.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		demoWorld.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));
		demoWorld.setMaxTargetOnTheField(10);
		return demoWorld;
	}

	public static GameInformation createEmptyWorld(float cameraHorizontalViewAngle, float cameraVerticalViewAngle, GameMode gameMode) {
		final int gameType = gameMode.getType();
		final int gameLevel = gameMode.getLevel();

		GameInformation world = new GameInformation(DEFAULT_REMAINING_TIME, DEFAULT_SPAWNING_TIME, WeaponFactory.createBasicWeapon());
		world.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		world.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));
		world.setMaxTargetOnTheField(10);

		if (gameType == GameModeFactory.GAME_TYPE_REMAINING_TIME) {
			switch (gameLevel) {
				case 1:
					world.setRemainingTime(30000);
					break;

				case 2:
					world.setRemainingTime(60000);
					break;

				case 3:
					world.setRemainingTime(90000);
					break;

			}
		}

		return world;
	}


}
