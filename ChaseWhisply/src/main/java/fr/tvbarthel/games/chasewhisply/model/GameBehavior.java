package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameBehavior implements Parcelable {

	protected GameInformation mGameInformation;
	protected IGameBehavior mInterface;

	public GameBehavior(GameInformation g, IGameBehavior i) {
		mGameInformation = g;
		mInterface = i;
	}

	/**
	 * init the game
	 */
	public void init() {
		mInterface.onInit(mGameInformation);
	}

	/**
	 * spawn routine
	 *
	 * @param xRange range for the spawn (abscissa)
	 * @param yRange range for the spawn (ordinate)
	 */
	public void spawn(int xRange, int yRange) {
		mInterface.onSpawn(xRange, yRange, mGameInformation);
	}

	/**
	 * target killed
	 */
	public void targetKilled() {
		mInterface.onKill(mGameInformation);
	}

	/**
	 * ticker routine
	 *
	 * @param tickingTime
	 */
	public void tick(long tickingTime) {
		mInterface.onTick(tickingTime, mGameInformation);
	}

	/**
	 * condition to stop the game
	 *
	 * @return true is game must be stopped
	 */
	public boolean isCompleted() {
		return mInterface.isCompleted(mGameInformation);
	}

	/**
	 * specific behavior onTouchScreen
	 */
	public void onTouchScreen() {
		mInterface.onTouchScreen();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private GameBehavior(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(mGameInformation, flags);
		out.writeParcelable(mInterface, flags);
	}

	private void readFromParcel(Parcel in) {
		mGameInformation = in.readParcelable(GameInformation.class.getClassLoader());
		mInterface = in.readParcelable(IGameBehavior.class.getClassLoader());
	}

	public static final Parcelable.Creator<GameBehavior> CREATOR = new Parcelable.Creator<GameBehavior>() {
		public GameBehavior createFromParcel(Parcel in) {
			return new GameBehavior(in);
		}

		public GameBehavior[] newArray(int size) {
			return new GameBehavior[size];
		}
	};

	public GameInformation getGameInformation() {
		return mGameInformation;
	}

	public interface IGameBehavior extends Parcelable {
		/**
		 * specific feature at initialisation
		 */
		abstract void onInit(GameInformation g);

		/**
		 * specific spawn behavior
		 *
		 * @param g
		 * @param xRange range for the spawn (abscissa)
		 * @param yRange range for the spawn (ordinate)
		 */
		abstract void onSpawn(int xRange, int yRange, GameInformation g);

		/**
		 * specific feature on target killed
		 *
		 * @param g
		 */
		abstract void onKill(GameInformation g);

		/**
		 * specific behavior on game tick
		 *
		 * @param tickingTime
		 * @param g
		 */
		abstract void onTick(long tickingTime, GameInformation g);

		/**
		 * specific behavior on touch screen
		 */
		abstract void onTouchScreen();

		/**
		 * specific feature to stop the game
		 *
		 * @param g
		 * @return
		 */
		abstract boolean isCompleted(GameInformation g);
	}


}
