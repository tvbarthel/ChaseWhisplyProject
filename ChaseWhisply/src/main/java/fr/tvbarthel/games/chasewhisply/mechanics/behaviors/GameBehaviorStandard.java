package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

import java.util.List;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;


public abstract class GameBehaviorStandard implements GameBehavior {

    public final static int FIRE_RESULT_NO_AMMO = 0x00000001;
    public final static int FIRE_RESULT_MISS = 0x00000002;
    public final static int FIRE_RESULT_KILL = 0x00000003;
    public final static int FIRE_RESULT_HIT = 0x00000004;

    private GameInformationStandard mGameInformation;
    protected IGameBehavior mIGameBehavior;

    public abstract void spawn(int xRange, int yRange);

    @Override
    public void setInterface(IGameBehavior iGameBehavior) {
        mIGameBehavior = iGameBehavior;
    }

    @Override
    public GameInformation getGameInformation() {
        return mGameInformation;
    }

    @Override
    public void setGameInformation(GameInformation gameInformation) {
        mGameInformation = (GameInformationStandard) gameInformation;
    }

    @Override
    public void setCurrentPosition(float posX, float posY, float posZ) {
        mGameInformation.setCurrentPosition(posX, posY, posZ);
    }

    @Override
    public float[] getCurrentPosition() {
        return mGameInformation.getCurrentPosition();
    }

    @Override
    public List<DisplayableItem> getItemsForDisplay() {
        return mGameInformation.getItemsForDisplay();
    }

    @Override
    public TargetableItem getCurrentTarget() {
        return mGameInformation.getCurrentTarget();
    }

    @Override
    public void setCurrentTarget(TargetableItem t) {
        mGameInformation.setCurrentTarget(t);
    }

    @Override
    public void removeTarget() {
        mGameInformation.removeTarget();
    }

    @Override
    public int getLastScoreAdded() {
        return mGameInformation.getLastScoreAdded();
    }

    @Override
    public void onClick() {
        fire();
    }

    public int getCurrentAmmunition() {
        return mGameInformation.getCurrentAmmunition();
    }

    public int getCurrentCombo() {
        return mGameInformation.getCurrentCombo();
    }

    public int getCurrentScore() {
        return mGameInformation.getCurrentScore();
    }

    protected int fire() {
        int fireResult = FIRE_RESULT_NO_AMMO;
        final int dmg = mGameInformation.getWeapon().fire();
        final TargetableItem currentTarget = mGameInformation.getCurrentTarget();
        if (dmg != 0) {
            fireABullet();
            if (currentTarget == null) {
                //miss
                fireResult = FIRE_RESULT_MISS;
                missTarget();
            } else {
                //hit
                fireResult = FIRE_RESULT_HIT;
                hitTarget(currentTarget, dmg);
                if (!currentTarget.isAlive()) {
                    //kill
                    fireResult = FIRE_RESULT_KILL;
                    killTarget(currentTarget);
                }
            }
        } else {
            shotWithoutAmmo();
        }
        return fireResult;
    }

    protected void fireABullet() {
        mGameInformation.bulletFired();
        mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_GUN_SHOT);
    }

    protected void shotWithoutAmmo() {
        mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_DRY_SHOT);
    }

    protected void killTarget(TargetableItem currentTarget) {
        mGameInformation.targetKilled();
        mGameInformation.stackCombo();
        mGameInformation.increaseScore(10 * currentTarget.getBasePoint() + 10 * mGameInformation.getCurrentCombo());
        mGameInformation.earnExp(currentTarget.getExpPoint());
        mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_GHOST_DEATH);
        mIGameBehavior.onTargetKilled(currentTarget);
    }

    protected void hitTarget(TargetableItem currentTarget, int dmg) {
        currentTarget.hit(dmg);
    }

    protected void missTarget() {
        mGameInformation.bulletMissed();
        DisplayableItem hole = DisplayableItemFactory.createBulletHole();
        final float[] currentPosition = mGameInformation.getCurrentPosition();
        hole.setX((int) currentPosition[0]);
        hole.setY((int) currentPosition[1]);
        mGameInformation.addDisplayableItem(hole);
    }

    public void reload() {
        mGameInformation.getWeapon().reload();
    }


    /**
     * default method for spawning all kinds of mobs
     *
     * @param xRange x range for the spawn rules
     * @param yRange y range for the spawn rules
     */
    protected void spawnStandardBehavior(int xRange, int yRange) {
        final int ghostType = TargetableItem.randomGhostType();
        spawnGhost(ghostType, xRange, yRange);
    }

    protected void spawnHardBehavior(int xRange, int yRange) {
        final int ghostType = TargetableItem.randomGhostTypeHard();
        spawnGhost(ghostType, xRange, yRange);
    }

    protected void spawnHarderBehavior(int xRange, int yRange) {
        final int ghostType = TargetableItem.randomGhostTypeHarder();
        spawnGhost(ghostType, xRange, yRange);
    }

    protected void spawnHardestBehavior(int xRange, int yRange) {
        final int ghostType = TargetableItem.randomGhostTypeHardest();
        spawnGhost(ghostType, xRange, yRange);
    }

    public void spawnGhost(int ghostType, int xRange, int yRange) {
        final float[] pos = mGameInformation.getCurrentPosition();
        mGameInformation.addTargetableItem(DisplayableItemFactory.createGhostWithRandomCoordinates(
                ghostType,
                (int) pos[0] - xRange,
                (int) pos[0] + xRange,
                (int) pos[1] - yRange,
                (int) pos[1] + yRange
        ));
    }


}
