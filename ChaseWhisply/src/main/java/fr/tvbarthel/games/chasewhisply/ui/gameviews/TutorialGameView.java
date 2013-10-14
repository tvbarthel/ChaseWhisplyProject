package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameInformationTutorial;
import fr.tvbarthel.games.chasewhisply.ui.AnimationLayer;

public class TutorialGameView extends GameView {

	private TextView mTutoTextView;
	private final Animation mFadeOutAnimation;
	private final Animation mFadeInAnimation;

	/**
	 * Display tutorial information
	 *
	 * @param context
	 * @param model
	 */
	public TutorialGameView(Context context, GameInformation model) {
		super(context, model);

		mFadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
		mFadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
	}

	@Override
	public void onDrawing(Canvas c) {
		int step = ((GameInformationTutorial) mModel).getCurrentStep();
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

	@Override
	//TODO REWORK
	public void setAnimationLayer(AnimationLayer animationLayer) {
		super.setAnimationLayer(animationLayer);

		//Create a text view supposed to display tuto messages
		mTutoTextView = new TextView(getContext());
		mTutoTextView.setTextSize(mFontSize);
		mTutoTextView.setTextColor(getResources().getColor(R.color.white));
		mTutoTextView.setBackgroundResource(R.color.alpha_shadow);
		mTutoTextView.setGravity(Gravity.CENTER);
		final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		final int padding = getResources().getDimensionPixelSize(R.dimen.default_padding);
		mTutoTextView.setPadding(padding, padding, padding, padding);
		mAnimationLayer.addView(mTutoTextView, layoutParams);

		displayCurrentStepMessage();
	}

	@Override
	//TODO REWORK
	public void onScreenTouch() {
		if (!mFadeInAnimation.hasEnded()) {
			mFadeInAnimation.cancel();
			mFadeInAnimation.reset();
		}

		if (!mFadeOutAnimation.hasEnded()) {
			mFadeInAnimation.cancel();
			mFadeOutAnimation.reset();
		}

		displayCurrentStepMessage();
	}

	//TODO REWORK
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
			case GameInformationTutorial.STEP_TARGET:
				stringId = R.string.tuto_step_target;
				break;
			case GameInformationTutorial.STEP_KILL:
				stringId = R.string.tuto_step_kill;
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
			final FadeOutListener fadeOutListener = new FadeOutListener(stringId);
			mFadeOutAnimation.setAnimationListener(fadeOutListener);
			mTutoTextView.startAnimation(mFadeOutAnimation);
		}
	}

	//TODO REWORK
	private class FadeOutListener implements Animation.AnimationListener {
		private final int mStringId;

		public FadeOutListener(final int stringId) {
			mStringId = stringId;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			post(new Runnable() {
				@Override
				public void run() {
					mTutoTextView.setText(mStringId);
					mTutoTextView.startAnimation(mFadeInAnimation);
				}
			});
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
}
