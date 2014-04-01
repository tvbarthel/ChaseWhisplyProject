package fr.tvbarthel.games.chasewhisply.model.mode;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.bonus.Bonus;

public class GameMode implements Parcelable {

    private int mType;
    private int mLevel;
    private int mImage;
    private int mLeaderboardStringId;
    private int mLeaderboardDescriptionStringId;
    private Bonus mBonus;
    private int mRequiredCondition;
    private int mRequiredMessage;
    private int mTitle;
    private int mLongDescription;
    private boolean mBonusAvailable;

    public GameMode() {
        mType = -1;
        mLevel = -1;
        mImage = -1;
        mLeaderboardStringId = -1;
        mLeaderboardDescriptionStringId = -1;
        mBonus = new Bonus.DummyBonus();
        mRequiredCondition = -1;
        mRequiredMessage = -1;
        mBonusAvailable = true;
        mTitle = -1;
        mLongDescription = -1;

    }

    protected GameMode(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<GameMode> CREATOR = new Parcelable.Creator<GameMode>() {
        public GameMode createFromParcel(Parcel in) {
            return new GameMode(in);
        }

        public GameMode[] newArray(int size) {
            return new GameMode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(mType);
        out.writeInt(mLevel);
        out.writeInt(mImage);
        out.writeInt(mLeaderboardStringId);
        out.writeInt(mLeaderboardDescriptionStringId);
        out.writeParcelable(mBonus, i);
        out.writeInt(mRequiredCondition);
        out.writeInt(mRequiredMessage);
        out.writeByte((byte) (mBonusAvailable ? 1 : 0));
        out.writeInt(mTitle);
        out.writeInt(mLongDescription);

    }

    /**
     * read parameters from a given Parcel
     *
     * @param in input Parcel to read from
     */
    public void readFromParcel(Parcel in) {
        mType = in.readInt();
        mLevel = in.readInt();
        mImage = in.readInt();
        mLeaderboardStringId = in.readInt();
        mLeaderboardDescriptionStringId = in.readInt();
        mBonus = in.readParcelable(Bonus.class.getClassLoader());
        mRequiredCondition = in.readInt();
        mRequiredMessage = in.readInt();
        mBonusAvailable = in.readByte() == 1;
        mTitle = in.readInt();
        mLongDescription = in.readInt();
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }

    public void setLeaderboardStringId(int stringId) {
        mLeaderboardStringId = stringId;
    }

    public int getLeaderboardStringId() {
        return mLeaderboardStringId;
    }

    public void setLeaderboardDescriptionStringId(int stringId) {
        mLeaderboardDescriptionStringId = stringId;
    }

    public int getLeaderboardDescriptionStringId() {
        return mLeaderboardDescriptionStringId;
    }

    public void setBonus(Bonus bonus) {
        mBonus = bonus;
    }

    public Bonus getBonus() {
        return mBonus;
    }

    /**
     * set the required condition to play this game mode
     *
     * @param condition int used to represent a condition
     */
    public void setRequiredCondition(int condition) {
        mRequiredCondition = condition;
    }

    /**
     * get the required level to play this game mode
     *
     * @return
     */
    public int getRequiredCondition() {
        return mRequiredCondition;
    }

    /**
     * set message displayed to the user when he can't access to this game mode
     *
     * @param message
     */
    public void setRequiredMessage(int message) {
        mRequiredMessage = message;
    }

    /**
     * get message displaed to the user when he can't access to this game mode
     *
     * @return
     */
    public int getRequiredMessage() {
        return mRequiredMessage;
    }


    /**
     * @param areBonusAvailable true if player can choose a bonus
     */
    public void setBonusAvailable(boolean areBonusAvailable) {
        mBonusAvailable = areBonusAvailable;
    }

    /**
     * are Bonus available for this mode ?
     *
     * @return
     */
    public boolean areBonusAvailable() {
        return mBonusAvailable;
    }


    /**
     * define own rules for availability
     *
     * @param p player profile
     * @return true if available
     */
    public boolean isAvailable(PlayerProfile p) {
        //always available
        return true;
    }

    /**
     * define own rules for grade
     *
     * @param gameInformation
     * @return
     */
    public int getRank(GameInformation gameInformation) {
        //always get the rank deserter
        return 0;
    }

    /**
     * retrieve gameMode title
     *
     * @return
     */
    public int getTitle() {
        return mTitle;
    }

    public void setTitle(int t) {
        mTitle = t;
    }

    public String getAdmiralRankRule(Resources res) {
        return "";
    }

    public String getSergeantRankRule(Resources res) {
        return "";
    }

    public String getCorporalRankRule(Resources res) {
        return "";
    }

    public String getSoldierRankRule(Resources res) {
        return "";
    }

    public String getDeserterRankRule(Resources res) {
        return "";
    }

    public void setLongDescription(int description) {
        mLongDescription = description;
    }

    public int getLongDescription() {
        return mLongDescription;
    }

}
