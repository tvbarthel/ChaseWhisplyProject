package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;

abstract public class TargetableItem extends DisplayableItem {
	//health of the item
	protected int mHealth;

	protected TargetableItem(Parcel in) {
		super(in);
	}

	/**
	 * check if the item is under the crosshairs
	 *
	 * @param crosshairsX
	 * @param crosshairsY
	 * @return
	 */
	public boolean isTargetable(int crosshairsX, int crosshairsY) {
		return (crosshairsX > mX && crosshairsX < (mX + mWidth) && crosshairsY > mY && crosshairsY < (mY + mHeight));
	}

	/**
	 * Hit the item with damage
	 *
	 * @param damage
	 */
	public void hit(int damage) {
		mHealth = Math.max(0, mHealth - damage);
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
