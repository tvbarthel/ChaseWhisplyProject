package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTwentyInARow;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.sound.GameSoundManager;

public class GameBehaviorTwentyInARow extends GameBehaviorTimeIncreasing {

    @Override
    public void spawn(int xRange, int yRange) {
        if (mGameInformation.getCurrentTargetsNumber() < GameBehaviorFactory.DEFAULT_MAX_TARGET) {
            spawnGhost(DisplayableItemFactory.TYPE_BABY_GHOST, xRange / 2 + xRange / 10, yRange / 2 + yRange / 10);
            mIGameBehavior.onSoundRequest(GameSoundManager.SOUND_TYPE_LAUGH_RANDOM);
        }
    }

    @Override
    protected void killTarget(TargetableItem currentTarget) {
        super.killTarget(currentTarget);
        if (((GameInformationTwentyInARow) mGameInformation).increaseCurrentStack() >= 20) {
            final ArrayList<Integer> reward = new ArrayList<Integer>();
            for (int i = 0; i < 50; i++) {
                reward.add(InventoryItemInformation.TYPE_COIN);
            }
            mGameInformation.addLoots(reward);
            mIGameBehavior.stop();
        }
    }

    @Override
    protected void missTarget() {
        super.missTarget();
        ((GameInformationTwentyInARow) mGameInformation).resetCurrentStack();
    }

    public int getCurrentStack() {
        return ((GameInformationTwentyInARow) mGameInformation).getCurrentStack();
    }
}
