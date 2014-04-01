package fr.tvbarthel.games.chasewhisply.model.bestiary;

import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

public class BestiaryEntry {
    private int mTitleResourceId;
    private TargetableItem mTargetableItem;
    private int mImageResourceId;


    public void setTargetableItem(TargetableItem targetableItem) {
        mTargetableItem = targetableItem;
    }

    public String getHealth() {
        return String.valueOf(mTargetableItem.getHealth());
    }

    public String getExpValue() {
        return String.valueOf(mTargetableItem.getExpPoint());
    }

    public String getPointValue() {
        return String.valueOf(mTargetableItem.getBasePoint());
    }

    public void setImageResourceId(int resourceId) {
        mImageResourceId = resourceId;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setTitleResourceId(int resourceId) {
        mTitleResourceId = resourceId;
    }

    public int getTitleResourceId() {
        return mTitleResourceId;
    }
}
