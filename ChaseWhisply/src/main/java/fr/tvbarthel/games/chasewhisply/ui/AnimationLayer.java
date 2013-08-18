package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import fr.tvbarthel.games.chasewhisply.R;


public class AnimationLayer extends RelativeLayout {
	private final Context mContext;

	public AnimationLayer(Context context) {
		super(context);
		mContext = context;
	}

	public void drawDyingGhost(Drawable drawable, int posX, int posY) {
		ImageView ghostDying = new ImageView(mContext);
		ghostDying.setImageDrawable(drawable);
		RelativeLayout.LayoutParams relativePositionLayout = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		relativePositionLayout.topMargin = posY;
		relativePositionLayout.leftMargin = posX;

		Animation dyingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dying_ghost);
		DyingGhostAnimationListener dyingGhostAnimationListener = new DyingGhostAnimationListener(ghostDying);
		dyingAnimation.setAnimationListener(dyingGhostAnimationListener);

		addView(ghostDying, relativePositionLayout);
		ghostDying.startAnimation(dyingAnimation);
	}

	private class DyingGhostAnimationListener implements Animation.AnimationListener {

		private View mViewToDelete;

		public DyingGhostAnimationListener(View viewToDelete) {
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
