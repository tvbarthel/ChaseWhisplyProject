package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

	public AnimationLayer(Context context) {
		super(context);
		mContext = context;
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


}
