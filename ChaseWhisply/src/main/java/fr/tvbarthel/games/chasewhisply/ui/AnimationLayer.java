package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;


public class AnimationLayer extends RelativeLayout {
	private final Context mContext;
	private TextView mTopTextView;
	private RelativeLayout.LayoutParams mTopTextRelativeLayoutParams;
	private boolean isTextChanging;

	public AnimationLayer(Context context) {
		super(context);
		mContext = context;
		isTextChanging = false;
	}

	public void drawDyingGhost(Drawable drawable, int score, int textSize, int posX, int posY) {
		//death animation
		ImageView ghostDying = new ImageView(mContext);
		ghostDying.setImageDrawable(drawable);

		RelativeLayout.LayoutParams relativePositionLayout = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		relativePositionLayout.topMargin = posY;
		relativePositionLayout.leftMargin = posX;

		Animation dyingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dying_ghost);
		AnimationListenerWithDeleter dyingGhostAnimationListener = new AnimationListenerWithDeleter(ghostDying);
		dyingAnimation.setAnimationListener(dyingGhostAnimationListener);


		addView(ghostDying, relativePositionLayout);
		ghostDying.startAnimation(dyingAnimation);

		//score animation
		if (score != 0) {
			TextView splashingScore = new TextView(mContext);
			splashingScore.setTextSize(textSize);
			splashingScore.setMaxLines(1);
			if (score > 0) {
				splashingScore.setText("+" + score);
				splashingScore.setTextColor(getResources().getColor(R.color.holo_green));
			} else {
				splashingScore.setText(score);
				splashingScore.setTextColor(getResources().getColor(R.color.holo_red));
			}

			Animation splashingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.splashing_score);
			AnimationListenerWithDeleter splashingAnimationListener = new AnimationListenerWithDeleter(splashingScore);
			splashingAnimation.setAnimationListener(splashingAnimationListener);

			this.bringToFront();
			addView(splashingScore, relativePositionLayout);
			splashingScore.startAnimation(splashingAnimation);
		}
	}


	/**
	 * use to display text information in top screen
	 *
	 * @param info
	 * @param textSize
	 * @param color
	 * @param screenHeight
	 * @param crossHairsHeight
	 */
	public void setTopText(String info, int textSize, int color, int screenHeight, int crossHairsHeight) {
		if (mTopTextView == null) {
			//TextView never shown, need to instantiate it
			mTopTextView = new TextView(getContext());
			mTopTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			mTopTextView.setTypeface(null, Typeface.BOLD);
			mTopTextView.setTextSize(textSize);
			mTopTextRelativeLayoutParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			mTopTextRelativeLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			mTopTextView.setLayoutParams(mTopTextRelativeLayoutParams);
			addView(mTopTextView);
			Animation createAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
			AnimationSetTopTextListener createListener =
					new AnimationSetTopTextListener(info, textSize, color, screenHeight, crossHairsHeight);
			createAnimation.setAnimationListener(createListener);
			requestLayout();
			//setTopText(info, textSize, color, screenHeight, crossHairsHeight);
		}

		if (!mTopTextView.getText().equals(info) && !isTextChanging) {
			//TextView already displayed, replace text
			Animation changeTextAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
			AnimationTopTextChangeListener changeTextAnimationListener =
					new AnimationTopTextChangeListener(info, textSize, color, screenHeight, crossHairsHeight);
			changeTextAnimation.setAnimationListener(changeTextAnimationListener);
			mTopTextView.startAnimation(changeTextAnimation);
			isTextChanging = true;
		}

	}

	/**
	 * delete TextView shown at the top
	 */
	public void hideTopText() {
		if (mTopTextView != null) {
			removeView(mTopTextView);
			mTopTextView = null;
		}
	}

	/**
	 * display new text in the TextView
	 *
	 * @param info
	 * @param textSize
	 * @param color
	 * @param crossHairHeight
	 */
	private void showTopText(String info, int textSize, int color, int screenHeight, int crossHairHeight) {
		mTopTextView.setTextSize(textSize);
		mTopTextView.setText(info);
		mTopTextView.setTextColor(getResources().getColor(color));
		mTopTextRelativeLayoutParams.setMargins(0,
				(int) (screenHeight / 2 - mTopTextView.getHeight() - crossHairHeight), 0, 0);
		Animation showTextAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
		mTopTextView.startAnimation(showTextAnimation);
	}

	/**
	 * Delete the view once the animation ends
	 */
	private class AnimationListenerWithDeleter implements Animation.AnimationListener {

		private View mViewToDelete;

		public AnimationListenerWithDeleter(View viewToDelete) {
			mViewToDelete = viewToDelete;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			final ViewGroup container = (ViewGroup) mViewToDelete.getParent();
			container.post(new Runnable() {
				@Override
				public void run() {
					container.removeView(mViewToDelete);
					mViewToDelete = null;
				}
			});
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * Wait for the end of hiding animation and then call showTopText
	 */
	private class AnimationTopTextChangeListener implements Animation.AnimationListener {

		private String mText;
		private int mTextSize;
		private int mColor;
		private int mScreenHeight;
		private int mCrossHaitHeight;


		public AnimationTopTextChangeListener(String text, int textSize, int color, int screenHeight, int crossHairHeight) {
			mText = text;
			mTextSize = textSize;
			mColor = color;
			mScreenHeight = screenHeight;
			mCrossHaitHeight = crossHairHeight;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			post(new Runnable() {
				@Override
				public void run() {
					showTopText(mText, mTextSize, mColor, mScreenHeight, mCrossHaitHeight);
					isTextChanging = false;
				}
			});
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * Wait for the end of hiding animation and then call showTopText
	 */
	private class AnimationSetTopTextListener implements Animation.AnimationListener {

		private String mText;
		private int mTextSize;
		private int mColor;
		private int mScreenHeight;
		private int mCrossHairsHeight;


		public AnimationSetTopTextListener(String text, int textSize, int color, int screenHeight, int crossHairHeight) {
			mText = text;
			mTextSize = textSize;
			mColor = color;
			mScreenHeight = screenHeight;
			mCrossHairsHeight = crossHairHeight;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			post(new Runnable() {
				@Override
				public void run() {
					mTopTextView.setTextSize(mTextSize);
					mTopTextView.setText(mText);
					mTopTextView.setTextColor(getResources().getColor(mColor));
					mTopTextView.requestLayout();
					mTopTextRelativeLayoutParams.setMargins(0,
							(int) (mScreenHeight / 2 - mTopTextView.getHeight() - mCrossHairsHeight), 0, 0);
				}
			});
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			post(new Runnable() {
				@Override
				public void run() {
					isTextChanging = false;
				}
			});
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}

}
