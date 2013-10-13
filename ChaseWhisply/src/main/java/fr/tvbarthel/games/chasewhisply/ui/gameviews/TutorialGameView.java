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
			case GameInformationTutorial.STEP_CROSSHAIR:
				drawCrossHair(c);
				break;
			case GameInformationTutorial.STEP_COMBO:
				drawCrossHair(c);
				drawCombo(c);
				break;
			case GameInformationTutorial.STEP_AMMO:
				drawCrossHair(c);
				drawCombo(c);
				drawAmmo(c);
				break;
			case GameInformationTutorial.STEP_AMMO_2:
				drawCrossHair(c);
				drawCombo(c);
				drawAmmo(c);
				break;
			case GameInformationTutorial.STEP_SCORE:
				drawCrossHair(c);
				drawCombo(c);
				drawAmmo(c);
				drawScore(c);
				break;

		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//TODO rework all this quick work
		if (mAnimationLayer != null) {
			displayCurrentStepMessage();
		}
	}

	@Override
	public void onScreenTouch() {
		//TODO rework all this quick work
		displayCurrentStepMessage();
	}

	//TODO rework all this quick work
	private void displayCurrentStepMessage() {
		final int step = ((GameInformationTutorial) mModel).getCurrentStep();
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
			case GameInformationTutorial.STEP_END:
				stringId = R.string.tuto_step_end;
				break;
		}

		if (stringId != -1) {
			mAnimationLayer.setTopText(getResources().getString(stringId),
					(int) mFontSize, R.color.card_shadow_grey, mScreenHeight, mCrossHairs.getHeight());
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
		canvas.drawText(String.valueOf(mModel.getWeapon().getCurrentAmmunition())
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
}
