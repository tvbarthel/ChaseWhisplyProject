package fr.tvbarthel.games.chasewhisply.ui.gameviews;


import android.content.Context;
import android.graphics.Canvas;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTime;

public class GameViewTimeDecreasing extends GameViewTime {

	public GameViewTimeDecreasing(Context c, GameEngineTime gameEngine) {
		super(c, gameEngine);
	}

	/**
	 * draw time, in red if time < 10 sec else in green
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	protected void drawTimer(Canvas canvas) {
		final long millis = mGameEngine.getCurrentTime();
		final int seconds = (int) (millis / 1000) - 1;
		final String remainingTime = String.format(mTimeString, seconds);
		resetPainter();
		if (seconds > 5) {
			mAnimationLayer.hideTopText();
			useGreenPainter();
			mPaint.getTextBounds(remainingTime, 0, remainingTime.length(), mBounds);
			canvas.drawText(remainingTime
					, mPadding + mBounds.width() / 2
					, mPadding + mPaint.getTextSize()
					, mPaint);
		} else if (seconds >= 0) {
			mAnimationLayer.setTopText(new Integer(seconds).toString(), (int) (mFontSize * 1.25),
					R.color.holo_dark_red, mScreenHeight, mCrossHairs.getHeight() / 2);
		}
	}
}
