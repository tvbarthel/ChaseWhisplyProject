package fr.tvbarthel.games.chasewhisply.mechanics.informations;

import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;
import fr.tvbarthel.games.chasewhisply.model.bonus.BonusInventoryItemConsumer;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;

abstract public class GameInformation implements Parcelable {
    protected float mCurrentX;
    protected float mCurrentY;
    protected float mCurrentZ;
    protected GameMode mGameMode;

    protected GameInformation(GameMode gameMode) {
        mGameMode = gameMode;
    }

    protected GameInformation(Parcel in) {
        readFromParcel(in);
    }

    protected void readFromParcel(Parcel in) {
        mCurrentX = in.readFloat();
        mCurrentY = in.readFloat();
        mCurrentZ = in.readFloat();
        mGameMode = in.readParcelable(GameMode.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeFloat(mCurrentX);
        out.writeFloat(mCurrentY);
        out.writeFloat(mCurrentZ);
        out.writeParcelable(mGameMode, i);
    }

    public void setCurrentPosition(float x, float y, float z) {
        mCurrentX = x;
        mCurrentY = y;
        mCurrentZ = z;
    }

    public float[] getCurrentPosition() {
        return new float[]{mCurrentX, mCurrentY, mCurrentZ};
    }

    public GameMode getGameMode() {
        return mGameMode;
    }

    public Bonus getBonus() {
        return mGameMode.getBonus();
    }

    public void useBonus(PlayerProfile playerProfile) {
        final Bonus currentBonus = mGameMode.getBonus();
        if (currentBonus instanceof BonusInventoryItemConsumer) {
            mGameMode.setBonus(((BonusInventoryItemConsumer) currentBonus).consume(playerProfile));
        }
    }

    /**
     * retrieve grade for the current informations
     *
     * @return
     */
    public int getRank() {
        return mGameMode.getRank(this);
    }

}