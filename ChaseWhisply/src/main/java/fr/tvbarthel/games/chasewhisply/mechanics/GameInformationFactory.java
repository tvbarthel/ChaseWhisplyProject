package fr.tvbarthel.games.chasewhisply.mechanics;


import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.WeaponFactory;

public class GameInformationFactory {

	private static final long DEFAULT_REMAINING_TIME = 2 * 60 * 1000;
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
		GameInformation demoWorld = new GameInformation(DEFAULT_REMAINING_TIME, WeaponFactory.createBasicWeapon());
		demoWorld.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		demoWorld.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));

		//place ghost
		for (int i = 0; i < DEMO_WORLD_ENEMY_NUMBER; i++) {
			demoWorld.addTargetableItem(DisplayableItemFactory.createEasyGhost());
		}

		return demoWorld;
	}


}
