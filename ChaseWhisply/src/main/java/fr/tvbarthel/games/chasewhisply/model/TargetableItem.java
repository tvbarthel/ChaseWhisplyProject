package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TargetableItem extends DisplayableItem {
	//health of the item
	protected int mHealth;
	protected int mBasePoint;
	protected int mExpPoint;
	protected ArrayList<Integer> mDrop;


	public TargetableItem() {
		super();
		mBasePoint = 1;
		mHealth = 1;
		mExpPoint = 0;
		mDrop = new ArrayList<Integer>();
	}

	public TargetableItem(int x, int y, int type) {
		super(x, y, type);
		mBasePoint = 1;
		mHealth = 1;
		mExpPoint = 0;
		mDrop = new ArrayList<Integer>();
	}

	protected TargetableItem(Parcel in) {
		super(in);
	}

	/**
	 * Hit the item with damage
	 *
	 * @param damage
	 */
	public void hit(int damage) {
		mHealth = Math.max(0, mHealth - damage);
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		super.writeToParcel(out, i);
		out.writeInt(mHealth);
		out.writeInt(mBasePoint);
		out.writeInt(mExpPoint);
		out.writeList(mDrop);
	}

	@Override
	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mHealth = in.readInt();
		mBasePoint = in.readInt();
		mExpPoint = in.readInt();
		mDrop = new ArrayList<Integer>();
		in.readList(mDrop, Integer.class.getClassLoader());
	}

	public static final Parcelable.Creator<TargetableItem> CREATOR = new Parcelable.Creator<TargetableItem>() {
		public TargetableItem createFromParcel(Parcel in) {
			return new TargetableItem(in);
		}

		public TargetableItem[] newArray(int size) {
			return new TargetableItem[size];
		}
	};

	/**
	 * used to know if this targetable is alive
	 *
	 * @return true if alive
	 */
	public boolean isAlive() {
		if (mHealth == 0) return false;
		return true;
	}

	/**
	 * Getters and Setters
	 */
	public int getHealth() {
		return mHealth;
	}

	public void setHealth(int health) {
		mHealth = health;
	}

	public int getBasePoint() {
		return mBasePoint;
	}

	public void setBasePoint(int basePoint) {
		mBasePoint = basePoint;
	}

	public void setExpPoint(int expPoint) {
		mExpPoint = expPoint;
	}

	public int getExpPoint() {
		return mExpPoint;
	}

	public ArrayList<Integer> getDrop() {
		return mDrop;
	}

	public void setDrop(ArrayList<Integer> drop) {
		mDrop = drop;
	}
}
