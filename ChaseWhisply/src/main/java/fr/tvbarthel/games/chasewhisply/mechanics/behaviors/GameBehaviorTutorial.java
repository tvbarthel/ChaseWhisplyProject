package fr.tvbarthel.games.chasewhisply.mechanics.behaviors;


import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTutorial;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

public class GameBehaviorTutorial extends GameBehaviorStandard {

    private IGameBehaviorTutorial mIGameBehaviorTutorial;

    protected GameInformationTutorial mGameInformation;

    @Override
    public void setGameInformation(GameInformation gameInformation) {
        super.setGameInformation(gameInformation);
        mGameInformation = (GameInformationTutorial) gameInformation;
    }


    @Override
    public void setInterface(IGameBehavior iGameBehavior) {
        super.setInterface(iGameBehavior);
        mIGameBehaviorTutorial = (IGameBehaviorTutorial) iGameBehavior;
    }

    @Override
    public void setCurrentPosition(float posX, float posY, float posZ) {
        mGameInformation.setCurrentPosition(posX, posY, posZ);
    }

    @Override
    public void spawn(int xRange, int yRange) {
    }

    public int getCurrentStep() {
        return mGameInformation.getCurrentStep();
    }

    public void onClick() {
        final int currentStep = mGameInformation.getCurrentStep();

        if (currentStep == GameInformationTutorial.STEP_KILL || currentStep == GameInformationTutorial.STEP_KILL_2) {
            fire();
        } else {
            final int nextStep = nextStep();

            if (nextStep == GameInformationTutorial.STEP_END) {
                mGameInformation.earnExp(8);
                mIGameBehavior.stop();
            }

            if (nextStep == GameInformationTutorial.STEP_AMMO_2) {
                mGameInformation.getWeapon().setCurrentAmmunition(1);
            }

            if (nextStep == GameInformationTutorial.STEP_TARGET || nextStep == GameInformationTutorial.STEP_TARGET_2) {
                if (mGameInformation.getCurrentStep() == GameInformationTutorial.STEP_TARGET) {
                    final float[] currentPosition = mGameInformation.getCurrentPosition();
                    final TargetableItem easyGhost = DisplayableItemFactory.createEasyGhost();
                    easyGhost.setX((int) currentPosition[0] + 5);
                    easyGhost.setY((int) currentPosition[1] + 7);
                    mGameInformation.addTargetableItem(easyGhost);
                } else if (mGameInformation.getCurrentStep() == GameInformationTutorial.STEP_TARGET_2) {
                    final float[] currentPosition = mGameInformation.getCurrentPosition();
                    final TargetableItem easyGhost = DisplayableItemFactory.createGhostWithHelmet();
                    easyGhost.setX((int) currentPosition[0] - 5);
                    easyGhost.setY((int) currentPosition[1] + 7);
                    mGameInformation.addTargetableItem(easyGhost);
                }
            }
        }
    }

    @Override
    protected void killTarget(TargetableItem currentTarget) {
        super.killTarget(currentTarget);
        nextStep();
    }

    private int nextStep() {
        final int nextStep = mGameInformation.nextStep();
        mIGameBehaviorTutorial.onNextStep();
        return nextStep;
    }

    public interface IGameBehaviorTutorial extends IGameBehavior {
        public void onNextStep();
    }

}
