package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GameInformation implements Parcelable {
	protected long mRemainingTime;
	protected long mSpawningTime;
	protected Weapon mWeapon;
	protected int mMaxTargetOnTheField;
	protected TargetableItem mCurrentTarget;
	protected List<TargetableItem> mTargetableItems;
	protected List<DisplayableItem> mDisplayableItems;
	protected int mSceneWidth;
	protected int mSceneHeight;
	protected float mCurrentX;
	protected float mCurrentY;
	protected GameMode mGameMode;
	protected ScoreInformation mScoreInformation;

	/**
	 * Create a new GameInformation
	 *
	 * @param remainingTime remaining time in millisecond
	 * @param spawningTime  spawning time in millisecond
	 * @param weapon        weapon used for this game
	 */
	public GameInformation(long remainingTime, long spawningTime, Weapon weapon) {
		mScoreInformation = new ScoreInformation();
		mRemainingTime = remainingTime;
		mSpawningTime = spawningTime;
		mWeapon = weapon;
		mMaxTargetOnTheField = 0;
		mCurrentTarget = null;
		mTargetableItems = new ArrayList<TargetableItem>();
		mDisplayableItems = new ArrayList<DisplayableItem>();
	}

	public GameInformation(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		mScoreInformation = in.readParcelable(ScoreInformation.class.getClassLoader());
		mRemainingTime = in.readLong();
		mSpawningTime = in.readLong();
		mWeapon = in.readParcelable(Weapon.class.getClassLoader());
		mMaxTargetOnTheField = in.readInt();
		mCurrentTarget = in.readParcelable(TargetableItem.class.getClassLoader());
		mTargetableItems = new ArrayList<TargetableItem>();
		in.readTypedList(mTargetableItems, TargetableItem.CREATOR);
		mDisplayableItems = new ArrayList<DisplayableItem>();
		in.readTypedList(mDisplayableItems, DisplayableItem.CREATOR);
		mGameMode = in.readParcelable(GameMode.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeParcelable(mScoreInformation, i);
		out.writeLong(mRemainingTime);
		out.writeLong(mSpawningTime);
		out.writeParcelable(mWeapon, i);
		out.writeInt(mMaxTargetOnTheField);
		out.writeParcelable(mCurrentTarget, i);
		out.writeTypedList(mTargetableItems);
		out.writeTypedList(mDisplayableItems);
		out.writeParcelable(mGameMode, i);
	}

	public static final Parcelable.Creator<GameInformation> CREATOR = new Parcelable.Creator<GameInformation>() {
		public GameInformation createFromParcel(Parcel in) {
			return new GameInformation(in);
		}

		public GameInformation[] newArray(int size) {
			return new GameInformation[size];
		}
	};

	/**
	 * Getters & Setters
	 */
	public Weapon getWeapon() {
		return mWeapon;
	}

	public long getRemainingTime() {
		return mRemainingTime;
	}

	public void setRemainingTime(long time) {
		mRemainingTime = time;
	}

	public long getSpawningTime() {
		return mSpawningTime;
	}

	public void setSpawningTime(long spawningTime) {
		mSpawningTime = spawningTime;
	}

	public void addTargetableItem(TargetableItem item) {
		mTargetableItems.add(item);
	}

	public void addDisplayableItem(DisplayableItem item) {
		mDisplayableItems.add(item);
	}

	public List<DisplayableItem> getItemsForDisplay() {
		final ArrayList<DisplayableItem> displayAll = new ArrayList<DisplayableItem>();
		displayAll.addAll(mDisplayableItems);
		displayAll.addAll(mTargetableItems);
		return displayAll;
	}

	public void setSceneWidth(int sceneWidth) {
		mSceneWidth = sceneWidth;
	}

	public int getSceneWidth() {
		return mSceneWidth;
	}

	public void setSceneHeight(int sceneHeight) {
		mSceneHeight = sceneHeight;
	}

	public int getSceneHeight() {
		return mSceneHeight;
	}

	public void setCurrentPosition(float x, float y) {
		mCurrentX = x;
		mCurrentY = y;
	}

	public float[] getCurrentPosition() {
		return new float[]{mCurrentX, mCurrentY};
	}

	/**
	 * get current target
	 *
	 * @return current target
	 */
	public TargetableItem getCurrentTarget() {
		return mCurrentTarget;
	}

	/**
	 * set current target
	 *
	 * @param t current TargetableItem targeted
	 */
	public void setCurrentTarget(TargetableItem t) {
		mCurrentTarget = t;
	}

	/**
	 * set current target to null
	 */
	public void removeTarget() {
		mCurrentTarget = null;
	}

	/**
	 * increase targets killed number
	 */
	public void targetKilled() {
		mTargetableItems.remove(mCurrentTarget);
		mCurrentTarget = null;
		mScoreInformation.increaseNumberOfTargetsKilled();

	}

	/**
	 * used to know frag number
	 *
	 * @return number of frag
	 */
	public int getFragNumber() {
		return mScoreInformation.getNumberOfTargetsKilled();
	}

	/**
	 * increase bullets fired number
	 */
	public void bulletFired() {
		mScoreInformation.increaseNumberOfBulletsFired();
	}

	public void bulletMissed() {
		resetCombo();
		mScoreInformation.increaseNumberOfBulletsMissed();
	}

	public void earnExp(int expEarned) {
		mScoreInformation.increaseExpEarned(expEarned);
	}

	/**
	 * used to get combo number
	 *
	 * @return current combo
	 */
	public int getCurrentCombo() {
		return mScoreInformation.getCurrentCombo();
	}

	/**
	 * return maximum combo during the game
	 *
	 * @return max combo number
	 */
	public int getMaxCombo() {
		return mScoreInformation.getMaxCombo();
	}

	/**
	 * increase combo if conditions are filled
	 */
	public void stackCombo() {
		mScoreInformation.increaseCurrentCombo();
	}

	/**
	 * reset current combo counter
	 */
	public void resetCombo() {
		mScoreInformation.resetCurrentCombo();
	}

	/**
	 * increase score
	 *
	 * @param amount score you want to add to the current one
	 */
	public void increaseScore(int amount) {
		mScoreInformation.increaseScore(amount);
	}

	/**
	 * get current score
	 *
	 * @return current score
	 */
	public int getCurrentScore() {
		return mScoreInformation.getScore();
	}


	public int getBulletsFired() {
		return mScoreInformation.getmNumberOfBulletsFired();
	}

	public int getBulletsMissed() {
		return mScoreInformation.getNumberOfBulletsMissed();
	}

	public int getExpEarned() {
		return mScoreInformation.getExpEarned();
	}

	public int getMaxTargetOnTheField() {
		return mMaxTargetOnTheField;
	}

	public void setMaxTargetOnTheField(int maxTargetOnTheField) {
		mMaxTargetOnTheField = maxTargetOnTheField;
	}

	public int getCurrentTargetsNumber() {
		return mTargetableItems.size();
	}

	public ScoreInformation getScoreInformation() {
		return mScoreInformation;
	}

	public GameMode getGameMode() {
		return mGameMode;
	}

	public void setGameMode(GameMode gameMode) {
		final int gameType = gameMode.getType();
		final int gameLevel = gameMode.getLevel();

		if (gameType == GameModeFactory.GAME_TYPE_REMAINING_TIME) {
			switch (gameLevel) {
				case 1:
					this.setRemainingTime(30000);
					break;

				case 2:
					this.setRemainingTime(60000);
					break;

				case 3:
					this.setRemainingTime(90000);
					break;

			}
		}
		mGameMode = gameMode;
	}

}