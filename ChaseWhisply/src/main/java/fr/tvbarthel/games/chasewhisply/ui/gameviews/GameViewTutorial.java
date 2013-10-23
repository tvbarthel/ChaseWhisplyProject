package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineTutorial;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTutorial;
import fr.tvbarthel.games.chasewhisply.ui.AnimationLayer;

public class GameViewTutorial extends GameViewStandard {

	private GameEngineTutorial mGameEngine;
	private TextView mTutoTextView;
	private final Animation mFadeOutAnimation;
	private final Animation mFadeInAnimation;

	public GameViewTutorial(Context context, GameEngineTutorial gameEngine) {
		super(context, gameEngine);
		mGameEngine = gameEngine;
		mFadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
		mFadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
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
	//TODO REWORK
	public void setAnimationLayer(AnimationLayer animationLayer) {
		super.setAnimationLayer(animationLayer);

		//Create a text view supposed to display tuto messages
		mTutoTextView = new TextView(getContext());
		mTutoTextView.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
		mTutoTextView.setTextColor(getResources().getColor(R.color.white));
		mTutoTextView.setTypeface(null, Typeface.BOLD);
		mTutoTextView.setBackgroundResource(R.color.alpha_shadow);
		mTutoTextView.setGravity(Gravity.CENTER);
		final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		final int padding = getResources().getDimensionPixelSize(R.dimen.default_padding);
		mTutoTextView.setPadding(padding, 2 * padding, padding, padding);
		mAnimationLayer.addView(mTutoTextView, layoutParams);

		displayCurrentStepMessage();
	}

	public void updateStepMessage() {
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
