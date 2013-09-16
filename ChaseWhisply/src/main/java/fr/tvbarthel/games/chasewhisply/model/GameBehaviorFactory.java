package fr.tvbarthel.games.chasewhisply.model;


import android.os.Parcel;

import fr.tvbarthel.games.chasewhisply.model.weapon.WeaponFactory;

public class GameBehaviorFactory {

	private static final long DEFAULT_SPAWNING_TIME = 1000;
	private static final long DEFAULT_TICKING_TIME = 1000;
	private static final long DEFAULT_BASIC_GAME_TIME = 30000;


	/**
	 * Create the survival mode
	 * start time 30 sec
	 * Each kill increase remaining time by 1 sec
	 * Spawning time 1 sec
	 * Mobs all, 10 max
	 * Basic Weapon
	 *
	 * @param cameraHorizontalViewAngle horizontal view angle of the phone camera
	 * @param cameraVerticalViewAngle   vertical view angle of the phone camera
	 * @param gameMode                  gameMode from gameModeChooser fragment
	 * @return Specific game behavior for this mode
	 */
	public static GameBehavior createSurvivalGame(float cameraHorizontalViewAngle, float cameraVerticalViewAngle, GameMode gameMode) {

		//we use default spawning time and basic weapon
		final GameInformation gameInformation = new GameInformation(DEFAULT_SPAWNING_TIME, WeaponFactory.createBasicWeapon());

		gameInformation.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		gameInformation.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));

		//only 10 target are allowed in the AR world
		gameInformation.setMaxTargetOnTheField(10);

		//initialize time to 30 sec
		gameInformation.setTime(30000);

		//use default tickingTime
		gameInformation.setTickingTime(DEFAULT_TICKING_TIME);

		gameInformation.setGameMode(gameMode);

		//implement survival behavior
		final GameBehavior.IGameBehavior i = new GameBehavior.IGameBehavior() {
			@Override
			public void onInit(GameInformation g) {
				//initial world is empty
			}

			@Override
			public void onSpawn(int xRange, int yRange, GameInformation g) {
				if (g.getCurrentTargetsNumber() < g.getMaxTargetOnTheField()) {
					//use all spawn routine
					allSpawnBehavior(xRange, yRange, g);
				}
			}

			@Override
			public synchronized void onKill(GameInformation g) {
				//increase time for each target killed
				g.setTime(g.getTime() + 1000);
			}

			@Override
			public synchronized void onTick(long tickingTime, GameInformation g) {
				//decrease time at each tick
				if (g.getTime() > 0) {
					g.setTime(g.getTime() - tickingTime);
				}
			}

			@Override
			public boolean isCompleted(GameInformation g) {
				//stop game when time is over
				return g.getTime() <= 0;
			}

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {

			}
		};
		return new GameBehavior(gameInformation, i);
	}


	/**
	 * Create the basic mode
	 * time decrease from remaining time to 0
	 * Spawning time 1 sec
	 * Mobs all, 10 max
	 * Basic Weapon
	 *
	 * @param level                     level
	 * @param cameraHorizontalViewAngle horizontal view angle of the phone camera
	 * @param cameraVerticalViewAngle   vertical view angle of the phone camera
	 * @param gameMode                  gameMode from gameModeChooser fragment
	 * @return Specific game behavior for this mode
	 */
	public static GameBehavior createRemainingTimeAllMobsGame(int level, float cameraHorizontalViewAngle, float cameraVerticalViewAngle, GameMode gameMode) {

		//we use default spawning time and basic weapon
		final GameInformation gameInformation = new GameInformation(DEFAULT_SPAWNING_TIME, WeaponFactory.createBasicWeapon());

		gameInformation.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		gameInformation.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));

		//only 10 target are allowed in the AR world
		gameInformation.setMaxTargetOnTheField(10);

		gameInformation.setTime(level * DEFAULT_BASIC_GAME_TIME);

		//use default tickingTime
		gameInformation.setTickingTime(DEFAULT_TICKING_TIME);

		gameInformation.setGameMode(gameMode);

		//implement survival behavior
		final GameBehavior.IGameBehavior i = new GameBehavior.IGameBehavior() {
			@Override
			public void onInit(GameInformation g) {
				//initial world is empty
			}

			@Override
			public void onSpawn(int xRange, int yRange, GameInformation g) {
				if (g.getCurrentTargetsNumber() < g.getMaxTargetOnTheField()) {
					//use all spawn routine
					allSpawnBehavior(xRange, yRange, g);
				}
			}

			@Override
			public synchronized void onKill(GameInformation g) {
				//do nothing specific
			}

			@Override
			public synchronized void onTick(long tickingTime, GameInformation g) {
				//decrease time at each tick
				if (g.getTime() > 0) {
					g.setTime(g.getTime() - tickingTime);
				}
			}

			@Override
			public boolean isCompleted(GameInformation g) {
				//stop game when time is over
				return g.getTime() <= 0;
			}

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {

			}
		};
		return new GameBehavior(gameInformation, i);
	}


	/**
	 * default method for spawning all kinds of mobs
	 *
	 * @param xRange          x range for the spawn rules
	 * @param yRange          y range for the spawn rules
	 * @param gameInformation gameInformation
	 */
	private static void allSpawnBehavior(int xRange, int yRange, GameInformation gameInformation) {
		final int randomDraw = MathUtils.randomize(0, 100);
		final float[] pos = gameInformation.getCurrentPosition();
		int ghostType;
		if (randomDraw < 60) {
			ghostType = DisplayableItemFactory.TYPE_EASY_GHOST;
		} else if (randomDraw < 75) {
			ghostType = DisplayableItemFactory.TYPE_HIDDEN_GHOST;
		} else if (randomDraw < 90) {
			ghostType = DisplayableItemFactory.TYPE_BABY_GHOST;
		} else if (randomDraw < 99) {
			ghostType = DisplayableItemFactory.TYPE_GHOST_WITH_HELMET;
		} else {
			ghostType = DisplayableItemFactory.TYPE_KING_GHOST;
		}
		gameInformation.addTargetableItem(DisplayableItemFactory.createGhostWithRandomCoordinates(
				ghostType,
				(int) pos[0] - xRange,
				(int) pos[0] + xRange,
				(int) pos[1] - yRange,
				(int) pos[1] + yRange
		));
	}
}
