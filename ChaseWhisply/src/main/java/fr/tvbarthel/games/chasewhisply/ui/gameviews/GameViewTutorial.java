package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTutorial;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTutorial;

public class GameViewTutorial extends GameViewStandard {

    private GameEngineTutorial mGameEngine;
    private TextView mTutoTextView;

    public GameViewTutorial(Context context, GameEngineTutorial gameEngine) {
        super(context, gameEngine);
        mGameEngine = gameEngine;
    }

    @Override
    public void onDrawing(Canvas c) {
        super.onDrawing(c);
        int step = mGameEngine.getCurrentStep();
        resetPainter();

        if (step >= GameInformationTutorial.STEP_CROSSHAIR) {
            drawCrossHair(c);
        }

        if (step >= GameInformationTutorial.STEP_COMBO) {
            drawCombo(c);
        }

        if (step >= GameInformationTutorial.STEP_AMMO) {
            drawAmmo(c);
        }

        if (step >= GameInformationTutorial.STEP_SCORE) {
            drawScore(c);
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //Create a text view supposed to display tuto messages
        if (mTutoTextView == null) {
            mTutoTextView = new TextView(getContext());
            mTutoTextView.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
            mTutoTextView.setTextColor(getResources().getColor(R.color.white));
            mTutoTextView.setTypeface(null, Typeface.BOLD);
            mTutoTextView.setBackgroundResource(R.color.alpha_shadow);
            mTutoTextView.setGravity(Gravity.CENTER);
            final int padding = getResources().getDimensionPixelSize(R.dimen.default_padding);
            mTutoTextView.setPadding(padding, 2 * padding, padding, padding);

            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mTutoTextView.setLayoutParams(layoutParams);
        }

        mAnimationLayer.addView(mTutoTextView);
        displayCurrentStepMessage(false);
    }

    @Override
    protected void onDetachedFromWindow() {
        mAnimationLayer.removeView(mTutoTextView);
        super.onDetachedFromWindow();
    }

    public void updateStepMessage() {
        displayCurrentStepMessage(true);
    }

    private void displayCurrentStepMessage(boolean changeText) {
        final int step = mGameEngine.getCurrentStep();
        int stringId = -1;
        switch (step) {
            case GameInformationTutorial.STEP_WELCOME:
                stringId = R.string.tuto_step_welcome;
                break;
            case GameInformationTutorial.STEP_UI_WELCOME:
                stringId = R.string.tuto_step_ui_welcome;
                break;
            case GameInformationTutorial.STEP_CROSSHAIR:
                stringId = R.string.tuto_step_crosshair;
                break;
            case GameInformationTutorial.STEP_AMMO:
                stringId = R.string.tuto_step_ammos;
                break;
            case GameInformationTutorial.STEP_AMMO_2:
                stringId = R.string.tuto_step_ammos_2;
                break;
            case GameInformationTutorial.STEP_COMBO:
                stringId = R.string.tuto_step_combo;
                break;
            case GameInformationTutorial.STEP_SCORE:
                stringId = R.string.tuto_step_score;
                break;
            case GameInformationTutorial.STEP_SERIOUS_THINGS:
                stringId = R.string.tuto_step_serious_things;
                break;
            case GameInformationTutorial.STEP_TARGET:
                stringId = R.string.tuto_step_target;
                break;
            case GameInformationTutorial.STEP_TARGET_2:
                stringId = R.string.tuto_step_target_2;
                break;
            case GameInformationTutorial.STEP_KILL:
                stringId = R.string.tuto_step_kill;
                break;
            case GameInformationTutorial.STEP_KILL_2:
                stringId = R.string.tuto_step_kill_2;
                break;
            case GameInformationTutorial.STEP_CONGRATULATION:
                stringId = R.string.tuto_step_congratulation;
                break;
            case GameInformationTutorial.STEP_CONGRATULATION_2:
                stringId = R.string.tuto_step_congratulation_2;
                break;
            case GameInformationTutorial.STEP_END:
                stringId = R.string.tuto_step_end;
                break;
        }

        if (stringId != -1) {
            if (changeText) {
                mAnimationLayer.changeTextView(mTutoTextView, stringId);
            } else {
                mTutoTextView.setText(stringId);
                mAnimationLayer.showTextView(mTutoTextView);
            }
        }
    }


}
