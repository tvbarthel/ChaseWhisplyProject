package fr.tvbarthel.games.chasewhisply.mechanics.informations;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.weapon.Weapon;

public class GameInformationMemorize extends GameInformationStandard {
	public static int STATE_MEMORIZING = 0x0000001;
	public static int STATE_KILLING = 0x00000002;

	private static int DEFAULT_GHOST_NUMBER = 2;
	private int mCurrentWave;
	private int mState;
	private int mCursor;
	private ArrayList<Integer> mGhostTypeList;

	public GameInformationMemorize(GameMode gameMode, Weapon weapon) {
		super(gameMode, weapon);
		mCurrentWave = 1;
		mState = STATE_MEMORIZING;
		mCursor = 0;
		generateCurrentWave();
	}

	public GameInformationMemorize(Parcel in) {
		super(in);
	}

	public void generateNextWave() {
		mCursor = -1;
		nextWave();
		generateCurrentWave();
	}

	public void generateCurrentWave() {
		if (mGhostTypeList == null) {
			mGhostTypeList = new ArrayList<Integer>();
			for (int i = 0; i < DEFAULT_GHOST_NUMBER + mCurrentWave; i++) {
				mGhostTypeList.add(TargetableItem.randomGhostType());
			}
		} else {
			mGhostTypeList.add(TargetableItem.randomGhostType());
		}
	}


	public void resetCursor() {
		mCursor = 0;
	}


	public int increaseCursor() {
		return ++mCursor;
	}

	public void setState(int state) {
		mState = state;
	}

	public boolean isPlayerMemorizing() {
		return mState == STATE_MEMORIZING;
	}

	public boolean isPlayerKilling() {
		return mState == STATE_KILLING;
	}

	public int getCursor() {
		return mCursor;
	}

	public int getWaveSize() {
		return mGhostTypeList.size();
	}

	public int getCurrentWaveNumber() {
		return mCurrentWave;
	}

	public ArrayList<Integer> getCurrentWave() {
		return mGhostTypeList;
	}

	public void nextWave() {
		mCurrentWave++;
	}

	public int getCurrentGhostType() {
		return mGhostTypeList.get(mCursor);
	}

	@Override
	public int getCurrentScore() {
		return mCurrentWave;
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		super.writeToParcel(out, i);
		out.writeInt(mCurrentWave);
		out.writeInt(mCursor);
		out.writeInt(mState);
		out.writeList(mGhostTypeList);

	}

	@Override
	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mCurrentWave = in.readInt();
		mCursor = in.readInt();
		mState = in.readInt();
		mGhostTypeList = new ArrayList<Integer>();
		in.readList(mGhostTypeList, Integer.class.getClassLoader());
	}

	public static final Parcelable.Creator<GameInformationMemorize> CREATOR = new Parcelable.Creator<GameInformationMemorize>() {
		public GameInformationMemorize createFromParcel(Parcel in) {
			return new GameInformationMemorize(in);
		}

		public GameInformationMemorize[] newArray(int size) {
			return new GameInformationMemorize[size];
		}
	};
}
