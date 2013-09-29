package fr.tvbarthel.games.chasewhisply.model;


import android.os.Parcel;

import fr.tvbarthel.games.chasewhisply.model.weapon.WeaponFactory;

public class GameBehaviorFactory {

	private static final long DEFAULT_SPAWNING_TIME = 1000;
	private static final long DEFAULT_TICKING_TIME = 1000;
	private static final long DEFAULT_BASIC_GAME_TIME = 30000;


	/**
	 * Create the death to the king mode
	 * start time 0 sec
	 * Basic Weapon
	 * init the game with 100 mobs, and 1 king
	 *
	 * @param cameraHorizontalViewAngle horizontal view angle of the phone camera
	 * @param cameraVerticalViewAngle   vertical view angle of the phone camera
	 * @param gameMode                  gameMode from gameModeChooser fragment
	 * @return Specific game behavior for this mode
	 */
	public static GameBehavior createKillTheKingGame(float cameraHorizontalViewAngle, float cameraVerticalViewAngle, GameMode gameMode) {

		//we use default spawning time and basic weapon
		final GameInformation gameInformation = new GameInformation(DEFAULT_SPAWNING_TIME, WeaponFactory.createBasicWeapon());

		gameInformation.setSceneWidth((int) Math.floor(cameraHorizontalViewAngle));
		gameInformation.setSceneHeight((int) Math.floor(cameraVerticalViewAngle));

		//only 10 target are allowed in the AR world
		gameInformation.setMaxTargetOnTheField(100);

		//initialize time to 5 sec
		gameInformation.setTime(5000);

		//use default tickingTime
		gameInformation.setTickingTime(DEFAULT_TICKING_TIME);

		gameInformation.setGameMode(gameMode);

		//implement survival behavior
		final GameBehavior.IGameBehavior i = new GameBehavior.IGameBehavior() {

			private boolean mIsKingDead = false;
			private long mStartTime;

			@Override
			public void onInit(GameInformation g) {
				//init world with 99 easy ghost and one king
				mStartTime = System.currentTimeMillis();
				for (int i = 0; i < 100; i++) {
					g.addTargetableItem(DisplayableItemFactory.createGhostWithRandomCoordinates(
							i == 50 ? DisplayableItemFactory.TYPE_KING_GHOST : randomGhostTypeWithoutKing()));
				}
			}

			@Override
			public void onSpawn(int xRange, int yRange, GameInformation g) {
				//no spawn behavior
			}

			@Override
			public synchronized void onKill(GameInformation g) {
				g.setScore(0);
				//check if the king is killed
				for (DisplayableItem d : g.getItemsForDisplay()) {
					if (d.getType() == DisplayableItemFactory.TYPE_KING_GHOST) return;
				}
				//king dead
				g.setScore((int) (System.currentTimeMillis() - mStartTime));
				mIsKingDead = true;
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
				//stop game when king was killed or time is over
				return mIsKingDead | g.getTime() <= 0;
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
		final float[] pos = gameInformation.getCurrentPosition();
		int ghostType = randomGhostType();
		gameInformation.addTargetableItem(DisplayableItemFactory.createGhostWithRandomCoordinates(
				ghostType,
				(int) pos[0] - xRange,
				(int) pos[0] + xRange,
				(int) pos[1] - yRange,
				(int) pos[1] + yRange
		));
	}

	/**
	 * spawn rules for all mobs
	 *
	 * @return
	 */
	private static int randomGhostType() {
		final int randomDraw = MathUtils.randomize(0, 100);
		if (randomDraw < 40) {
			return DisplayableItemFactory.TYPE_EASY_GHOST;
		} else if (randomDraw < 60) {
			return DisplayableItemFactory.TYPE_BLOND_GHOST;
		} else if (randomDraw < 75) {
			return DisplayableItemFactory.TYPE_HIDDEN_GHOST;
		} else if (randomDraw < 90) {
			return DisplayableItemFactory.TYPE_BABY_GHOST;
		} else if (randomDraw < 99) {
			return DisplayableItemFactory.TYPE_GHOST_WITH_HELMET;
		} else {
			return DisplayableItemFactory.TYPE_KING_GHOST;
		}

	}

	/**
	 * spawn rules for all mobs exept king
	 *
	 * @return
	 */
	private static int randomGhostTypeWithoutKing() {
		final int randomDraw = MathUtils.randomize(0, 100);
		if (randomDraw < 60) {
			return DisplayableItemFactory.TYPE_EASY_GHOST;
		} else if (randomDraw < 75) {
			return DisplayableItemFactory.TYPE_HIDDEN_GHOST;
		} else if (randomDraw < 90) {
			return DisplayableItemFactory.TYPE_BABY_GHOST;
		} else {
			return DisplayableItemFactory.TYPE_GHOST_WITH_HELMET;
		}

	}
}
