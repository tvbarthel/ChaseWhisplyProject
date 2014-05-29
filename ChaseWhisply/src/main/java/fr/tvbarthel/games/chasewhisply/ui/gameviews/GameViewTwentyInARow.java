package fr.tvbarthel.games.chasewhisply.ui.gameviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTime;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTwentyInARow;

public class GameViewTwentyInARow extends GameViewTime {

    public GameViewTwentyInARow(Context c, GameEngineTime gameEngine) {
        super(c, gameEngine);
    }

    @Override
    public void onDrawing(Canvas c) {
        super.onDrawing(c);
        drawCurrentStack(c);
    }

    @Override
    protected void drawScore(Canvas canvas) {
        //don't draw score  
    }

    protected void drawCurrentStack(Canvas canvas) {
        final int currentStack = ((GameEngineTwentyInARow) mGameEngine).getCurrentStack();
        final String currentStackStr = String.valueOf(currentStack);
        final int stackLength = currentStackStr.length();
        final int radius = Math.max(mScreenWidth / (12 - stackLength), mScreenHeight / (12 - stackLength));

        //draw transparent overlay
        useTransparentBlackPainter();
        canvas.drawOval(new RectF(-radius, mScreenHeight - radius, radius, mScreenHeight + radius), mPaint);

        //draw Score
        useWhitePainter();
        mPaint.getTextBounds(currentStackStr, 0, currentStackStr.length(), mBounds);
        canvas.drawText(currentStackStr
                , mBounds.width() / 2 + radius / 4
                , mScreenHeight - radius / 4
                , mPaint);

    }

}
