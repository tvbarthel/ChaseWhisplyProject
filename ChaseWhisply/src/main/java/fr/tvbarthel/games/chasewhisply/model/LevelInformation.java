package fr.tvbarthel.games.chasewhisply.model;


public class LevelInformation {
    private int mLevel;
    private long mTotalExpEarned;
    private long mCurrentExpStep;
    private long mNextExpStep;

    public LevelInformation(int level, long totalExpEarned, long currentExpStep, long nextExpStep) {
        mLevel = level;
        mTotalExpEarned = totalExpEarned;
        mCurrentExpStep = currentExpStep;
        mNextExpStep = nextExpStep;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getExpProgress() {
        return (int) (mTotalExpEarned - mCurrentExpStep);
    }

    public int getExpNeededToLevelUp() {
        return (int) (mNextExpStep - mCurrentExpStep);
    }

    public int getProgressInPercent() {
        return (int) (getExpProgress() * 100 / getExpNeededToLevelUp());
    }

    public long getTotalExpEarned() {
        return mTotalExpEarned;
    }

    public long getCurrentExpStep() {
        return mCurrentExpStep;
    }

    public long getNextExpStep() {
        return mNextExpStep;
    }


}
