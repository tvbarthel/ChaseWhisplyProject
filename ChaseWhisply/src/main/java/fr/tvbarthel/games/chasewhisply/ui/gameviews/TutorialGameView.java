package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameInformationTutorial;

public class TutorialGameView extends GameView {

	/**
	 * Display tutorial information
	 *
	 * @param context
	 * @param model
	 */
	public TutorialGameView(Context context, GameInformation model) {
		super(context, model);
	}

	@Override
	public void onDrawing(Canvas c) {
		int step = ((GameInformationTutorial) mModel).getCurrentStep();
		resetPainter();
		//TODO rework all this sad work
		switch (step) {
			case GameInformationTutorial.STEP_WELCOME:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_welcome),
						(int) mFontSize, R.color.holo_dark_green, mScreenHeight);
				break;
			case GameInformationTutorial.STEP_UI_WELCOME:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_ui_welcome),
						(int) mFontSize, R.color.card_shadow_grey, mScreenHeight);
				break;
			case GameInformationTutorial.STEP_CROSSHAIR:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_crosshair),
						(int) mFontSize, R.color.holo_dark_green, mScreenHeight);
				drawCrossHair(c);
				break;
			case GameInformationTutorial.STEP_AMMO:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_ammos),
						(int) mFontSize, R.color.holo_dark_red, mScreenHeight);
				drawAmmo(c);
				break;
			case GameInformationTutorial.STEP_COMBO:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_combo),
						(int) mFontSize, R.color.holo_dark_green, mScreenHeight);
				drawCombo(c);
				break;
			case GameInformationTutorial.STEP_SERIOUS_THINGS:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_serious_things),
						(int) mFontSize, R.color.holo_dark_red, mScreenHeight);
				break;
			case GameInformationTutorial.STEP_END:
				mAnimationLayer.setTopText(getResources().getString(R.string.tuto_step_end),
						(int) mFontSize, R.color.holo_dark_green, mScreenHeight);
				break;
		}
	}


	/**
	 * use to display a crossHair
	 *
	 * @param canvas
	 */
	private void drawCrossHair(Canvas canvas) {
		resetPainter();
		useGreenPainter();
		canvas.drawBitmap(mCrossHairs, (float) (mScreenWidth - mCrossHairs.getWidth()) / 2,
				(float) (mScreenHeight - mCrossHairs.getHeight()) / 2, mPaint);
	}

	/**
	 * draw ammo on player screen, in green if ammo > 0 else in red
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawAmmo(Canvas canvas) {
		resetPainter();
		useGreenPainter();
		canvas.drawBitmap(mAmmoBitmap, (float) (mScreenWidth - mAmmoBitmap.getWidth() - mPadding),
				(float) (getHeight() - mAmmoBitmap.getHeight() - mPadding), mPaint);

		mPaint.setTextSize(mAmmoBitmap.getHeight() / 2);
		canvas.drawText(String.valueOf(8)
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
		resetPainter();
		useGreenPainter();
		final String currentCombo = String.format(mComboString, 3);
		mPaint.getTextBounds(currentCombo, 0, currentCombo.length(), mBounds);
		canvas.drawText(currentCombo
				, mScreenWidth / 2 + mCrossHairs.getWidth() / 2 + mBounds.width() / 2
				, mScreenHeight / 2 + mCrossHairs.getHeight() / 2
				, mPaint);
	}
}
