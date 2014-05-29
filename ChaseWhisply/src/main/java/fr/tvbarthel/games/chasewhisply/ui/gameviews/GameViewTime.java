package fr.tvbarthel.games.chasewhisply.ui.gameviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTime;

public class GameViewTime extends GameViewStandard {
    protected GameEngineTime mGameEngine;

    public GameViewTime(Context c, GameEngineTime gameEngine) {
        super(c, gameEngine);
        mGameEngine = gameEngine;
    }

    @Override
    public void onDrawing(Canvas c) {
        super.onDrawing(c);
        drawTimer(c);
    }

    /**
     * draw time, in red if time < 10 sec else in green
     *
     * @param canvas canvas from View.onDraw method
     */
    protected void drawTimer(Canvas canvas) {
        final long millis = mGameEngine.getCurrentTime();
        final int seconds = (int) (millis / 1000);
        final String remainingTime = String.valueOf(seconds);
        final int radius = Math.max(mTimerBitmap.getWidth(), mTimerBitmap.getHeight()) + (int) mPadding;
        resetPainter();

        //draw transparent overlay
        useTransparentBlackPainter();
        canvas.drawOval(new RectF(-radius, -radius, radius, radius), mPaint);

        //draw icon
        useWhitePainter();
        canvas.drawBitmap(mTimerBitmap, 0, 0, mPaint);

        //draw time
        mPaint.getTextBounds(remainingTime, 0, remainingTime.length(), mBounds);
        canvas.drawText(remainingTime
                , mPadding + radius
                , radius
                , mPaint);

    }

}
