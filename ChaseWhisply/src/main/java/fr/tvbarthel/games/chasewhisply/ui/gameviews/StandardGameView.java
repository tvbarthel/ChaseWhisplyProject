package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;


public class StandardGameView extends GameView {

	/**
	 * GameView which display :
	 * cross hair in the middle of the screen
	 * ammo amount in bottom right corner
	 * current combo just on the right of the crossHair
	 * the score in the bottom left corner
	 * time in top left corner
	 *
	 * @param c context
	 * @param g model of the view
	 * @return game view
	 */
	public StandardGameView(Context c, GameInformation g) {
		super(c, g);
	}

	@Override
	public void onDrawing(Canvas c) {
		drawCrossHair(c);
		drawAmmo(c);
		drawCombo(c);
		drawScore(c);
		drawTimer(c);
	}

	/**
	 * use to display a crossHair
	 *
	 * @param canvas
	 */
	private void drawCrossHair(Canvas canvas) {
		canvas.drawBitmap(mCrossHairs, (float) (mScreenWidth - mCrossHairs.getWidth()) / 2,
				(float) (mScreenHeight - mCrossHairs.getHeight()) / 2, mPaint);
	}

	/**
	 * draw ammo on player screen, in green if ammo > 0 else in red
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawAmmo(Canvas canvas) {
		final int currentAmmunition = mModel.getWeapon().getCurrentAmmunition();
		resetPainter();

		if (currentAmmunition == 0) {
			useRedPainter();
			final String noAmmoMessage = getResources().getString(R.string.in_game_no_ammo_message);

			canvas.drawText(noAmmoMessage,
					mScreenWidth / 2,
					(mScreenHeight - mCrossHairs.getHeight()) / 2 - 10,
					mPaint);
		} else {
			useGreenPainter();
		}

		canvas.drawBitmap(mAmmoBitmap, (float) (mScreenWidth - mAmmoBitmap.getWidth() - mPadding),
				(float) (getHeight() - mAmmoBitmap.getHeight() - mPadding), mPaint);

		mPaint.setTextSize(mAmmoBitmap.getHeight() / 2);
		canvas.drawText(String.valueOf(currentAmmunition)
				, mScreenWidth - mAmmoBitmap.getWidth() - mPaint.getTextSize() / 2 - mPadding
				, mScreenHeight - (mAmmoBitmap.getHeight() / 4)
				, mPaint);
	}

	/**
	 * draw combo counter near crossHair
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawCombo(Canvas canvas) {
		final int comboNumber = mModel.getCurrentCombo();
		resetPainter();
		useGreenPainter();
		if (comboNumber > 1) {
			final String currentCombo = String.format(mComboString, mModel.getCurrentCombo());
			mPaint.getTextBounds(currentCombo, 0, currentCombo.length(), mBounds);
			canvas.drawText(currentCombo
					, mScreenWidth / 2 + mCrossHairs.getWidth() / 2 + mBounds.width() / 2
					, mScreenHeight / 2 + mCrossHairs.getHeight() / 2
					, mPaint);
		}
	}

	/**
	 * draw score
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawScore(Canvas canvas) {
		resetPainter();
		useGreenPainter();
		final String score = String.format(mScoreString, mModel.getCurrentScore());

		mPaint.getTextBounds(score, 0, score.length(), mBounds);
		canvas.drawText(score
				, mBounds.width() / 2 + mPadding
				, mScreenHeight - mPaint.getTextSize()
				, mPaint);

	}

	/**
	 * draw remaining time, in red if remainingTime < 10 sec else in green
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawTimer(Canvas canvas) {
		final long millis = mModel.getTime();
		final int ss = (int) (millis / 1000);
		final String remainingTime = String.format(mTimeString, ss);
		resetPainter();
		if (ss > 6) {
			useGreenPainter();
			mPaint.getTextBounds(remainingTime, 0, remainingTime.length(), mBounds);
			canvas.drawText(remainingTime
					, mPadding + mBounds.width() / 2
					, mPadding + mPaint.getTextSize()
					, mPaint);
		} else if (ss > 0) {
			mAnimationLayer.setTopText(new Integer(ss - 1).toString(), (int) (mFontSize * 1.25),
					R.color.holo_dark_red, mScreenHeight);
		} else {
			mAnimationLayer.hideTopText();
		}

	}
}
