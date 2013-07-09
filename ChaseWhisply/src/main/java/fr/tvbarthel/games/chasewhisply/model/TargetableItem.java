package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TargetableItem extends DisplayableItem {
	//health of the item
	protected int mHealth;

	public TargetableItem() {
		super();
	}

	public TargetableItem(int x, int y, int type) {
		super(x, y, type);
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
	}

	@Override
	public void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		mHealth = in.readInt();
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
}
