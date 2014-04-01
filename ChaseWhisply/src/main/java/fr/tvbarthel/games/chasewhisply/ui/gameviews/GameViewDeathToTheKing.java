package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineDeathToTheKing;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTime;

public class GameViewDeathToTheKing extends GameViewTime {
    private GameEngineDeathToTheKing mGameEngine;
    private TextView mInstruction;

    public GameViewDeathToTheKing(Context c, GameEngineTime gameEngine) {
        super(c, gameEngine);
        mGameEngine = (GameEngineDeathToTheKing) gameEngine;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mGameEngine.hasTheKingAlreadyBeenSummoned()) return;

        //Create a text view supposed to display tuto messages
        if (mInstruction == null) {
            mInstruction = new TextView(getContext());
            mInstruction.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
            mInstruction.setTextColor(getResources().getColor(R.color.white));
            mInstruction.setTypeface(null, Typeface.BOLD);
            mInstruction.setBackgroundResource(R.color.alpha_shadow);
            mInstruction.setGravity(Gravity.CENTER);
            final int padding = getResources().getDimensionPixelSize(R.dimen.default_padding);
            mInstruction.setPadding(padding, 2 * padding, padding, padding);

            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mInstruction.setLayoutParams(layoutParams);
            mInstruction.setText(R.string.game_mode_death_to_the_king_instruction);
        }

        mAnimationLayer.addView(mInstruction);
        mAnimationLayer.showTextView(mInstruction);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mInstruction != null) mAnimationLayer.removeView(mInstruction);
        super.onDetachedFromWindow();
    }

    public void hideInstruction() {
        if (mInstruction != null) {
            mAnimationLayer.hideTextView(mInstruction);
        }
    }

    @Override
    protected void drawTimer(Canvas canvas) {
        if (mGameEngine.hasTheKingAlreadyBeenSummoned()) {
            super.drawTimer(canvas);
        }
    }
}


