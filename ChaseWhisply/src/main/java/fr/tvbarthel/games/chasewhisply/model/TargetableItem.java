package fr.tvbarthel.games.chasewhisply.model;

import android.os.Parcel;

public class TargetableItem extends DisplayableItem {

	protected TargetableItem(Parcel in) {
		super(in);
	}

	public boolean isTargetable(int crossHairX, int crossHairY) {
		return (crossHairX > mX && crossHairX < (mX + mWidth) && crossHairY > mY && crossHairY < (mY + mHeight));
	}
}
