package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.ui.AnimationLayer;
import fr.tvbarthel.games.chasewhisply.ui.RenderInformation;

public abstract class GameView extends View {

	protected final Bitmap mCrossHairs;
	protected final Bitmap mGhostBitmap;
	protected final Bitmap mGhostTargetedBitmap;
	protected final Bitmap[] mBlondGhostBitmap;
	protected final Bitmap[] mBlondTargetedBitmap;
	protected final Bitmap mAmmoBitmap;
	protected final Bitmap mBulletHoleBitmap;
	protected final Bitmap mBabyGhostBitmap;
	protected final Bitmap mTargetedBabyGhostBitmap;
	protected final Bitmap[] mGhostWithHelmetBitmaps;
	protected final Bitmap[] mGhostWithHelmetTargetedBitmaps;
	protected final Bitmap mKingGhost;
	protected final Bitmap mTargetedKingGhost;
	protected final Bitmap mHiddenGhost;
	protected final String mComboString;
	protected final String mScoreString;
	protected final String mTimeString;
	protected final Paint mPaint = new Paint();
	protected final Rect mBounds = new Rect();
	protected GameInformation mModel;
	//ratio for displaying items
	protected float mWidthRatioDegreeToPx;
	protected float mHeightRatioDegreeToPx;
	protected float mFontSize;
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mPadding;
	protected AnimationLayer mAnimationLayer;

	/**
	 * called during View.onDraw
	 *
	 * @param c
	 */
	public abstract void onDrawing(Canvas c);

	//TODO rework dat shit
	public abstract void onScreenTouch();


	public GameView(Context context, GameInformation model) {
		super(context);
		mModel = model;

		final Resources res = getResources();

		//initialize bitmap drawn after
		mCrossHairs = BitmapFactory.decodeResource(res, R.drawable.crosshair_white);
		mGhostBitmap = BitmapFactory.decodeResource(res, R.drawable.ghost);
		mGhostTargetedBitmap = BitmapFactory.decodeResource(res, R.drawable.ghost_targeted);
		mBlondGhostBitmap = new Bitmap[]{
				BitmapFactory.decodeResource(res, R.drawable.blond_ghost_in_tears),
				BitmapFactory.decodeResource(res, R.drawable.blond_ghost),
		};
		mBlondTargetedBitmap = new Bitmap[]{
				BitmapFactory.decodeResource(res, R.drawable.blond_ghost_in_tears_targeted),
				BitmapFactory.decodeResource(res, R.drawable.blond_ghost_targeted),
		};
		mAmmoBitmap = BitmapFactory.decodeResource(res, R.drawable.ammo);
		mBulletHoleBitmap = BitmapFactory.decodeResource(res, R.drawable.bullethole);
		mBabyGhostBitmap = BitmapFactory.decodeResource(res, R.drawable.baby_ghost);
		mTargetedBabyGhostBitmap = BitmapFactory.decodeResource(res, R.drawable.baby_ghost_targeted);
		mGhostWithHelmetBitmaps = new Bitmap[]{
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_5),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_4),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_3),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_2),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet),
		};

		mGhostWithHelmetTargetedBitmaps = new Bitmap[]{
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_5_targeted),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_4_targeted),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_3_targeted),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_2_targeted),
				BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_targeted),
		};

		mHiddenGhost = BitmapFactory.decodeResource(res, R.drawable.hidden_ghost);
		mKingGhost = BitmapFactory.decodeResource(res, R.drawable.king_ghost);
		mTargetedKingGhost = BitmapFactory.decodeResource(res, R.drawable.targeted_king_ghost);

		mComboString = res.getString(R.string.in_game_combo_counter);
		mScoreString = res.getString(R.string.in_game_score);
		mTimeString = res.getString(R.string.in_game_time);

		mFontSize = getTextSizeFromStyle(context, android.R.style.TextAppearance_Holo_Large);
		mPadding = getResources().getDimensionPixelSize(R.dimen.half_padding);

		mScreenWidth = 0;
		mScreenHeight = 0;
		mWidthRatioDegreeToPx = 0;
		mHeightRatioDegreeToPx = 0;
	}

	private void initializeScreenDimension(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		mWidthRatioDegreeToPx = mScreenWidth / mModel.getSceneWidth();
		mHeightRatioDegreeToPx = mScreenHeight / mModel.getSceneHeight();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initializeScreenDimension(w, h);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawDisplayableItems(canvas);
		onDrawing(canvas);
	}

	/**
	 * draw active items on the screen
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	protected void drawDisplayableItems(Canvas canvas) {
		final float[] currentPos = mModel.getCurrentPosition();
		currentPos[0] *= mWidthRatioDegreeToPx;
		currentPos[1] *= mHeightRatioDegreeToPx;
		for (DisplayableItem i : mModel.getItemsForDisplay()) {
			switch (i.getType()) {
				case DisplayableItemFactory.TYPE_EASY_GHOST:
					renderEasyGhost(canvas, (TargetableItem) i, currentPos);
					break;
				case DisplayableItemFactory.TYPE_BABY_GHOST:
					renderBabyGhost(canvas, (TargetableItem) i, currentPos);
					break;
				case DisplayableItemFactory.TYPE_GHOST_WITH_HELMET:
					renderGhostWithHelmet(canvas, (TargetableItem) i, currentPos);
					break;
				case DisplayableItemFactory.TYPE_HIDDEN_GHOST:
					renderHiddenGhost(canvas, (TargetableItem) i, currentPos);
					break;
				case DisplayableItemFactory.TYPE_KING_GHOST:
					renderKingGhost(canvas, (TargetableItem) i, currentPos);
					break;
				case DisplayableItemFactory.TYPE_BLOND_GHOST:
					renderBlondGhost(canvas, (TargetableItem) i, currentPos);
					break;
				case DisplayableItemFactory.TYPE_BULLET_HOLE:
					renderBulletHole(canvas, i);
					break;
			}
		}
	}

	/**
	 * used to know if a targetable item is targeted
	 *
	 * @param crosshairPosition current crosshair x and y
	 * @param t                 targetable item
	 * @param targetableRes     bitmap used to draw this targetable
	 * @return true if targeted
	 */
	protected boolean isTargeted(float[] crosshairPosition, DisplayableItem t, Bitmap targetableRes) {
		final int xInPx = (int) (t.getX() * mWidthRatioDegreeToPx);
		final int yInPx = (int) (t.getY() * mHeightRatioDegreeToPx);
		if (crosshairPosition[0] > xInPx - targetableRes.getWidth() / 2 & crosshairPosition[0] < xInPx + targetableRes.getWidth() / 2
				& crosshairPosition[1] > yInPx - targetableRes.getHeight() / 2 & crosshairPosition[1] < yInPx + targetableRes.getHeight() / 2) {
			return true;
		} else {
			return false;
		}
	}

	protected void renderGhostWithHelmet(Canvas canvas, TargetableItem ghostWithHelmet, float[] currentPos) {
		final int bitmapIndex = ghostWithHelmet.getHealth() - 1;
		renderGhost(canvas, ghostWithHelmet, currentPos, mGhostWithHelmetBitmaps[bitmapIndex], mGhostWithHelmetTargetedBitmaps[bitmapIndex]);
	}

	protected void renderEasyGhost(Canvas canvas, TargetableItem easyGhost, float[] currentPos) {
		renderGhost(canvas, easyGhost, currentPos, mGhostBitmap, mGhostTargetedBitmap);
	}

	protected void renderBabyGhost(Canvas canvas, TargetableItem babyGhost, float[] currentPos) {
		renderGhost(canvas, babyGhost, currentPos, mBabyGhostBitmap, mTargetedBabyGhostBitmap);
	}

	protected void renderHiddenGhost(Canvas canvas, TargetableItem hiddenGhost, float[] currentPos) {
		renderGhost(canvas, hiddenGhost, currentPos, mHiddenGhost, mGhostTargetedBitmap);
	}

	protected void renderKingGhost(Canvas canvas, TargetableItem kingGhost, float[] currentPos) {
		renderGhost(canvas, kingGhost, currentPos, mKingGhost, mTargetedKingGhost);
	}

	protected void renderBlondGhost(Canvas canvas, TargetableItem blondGhost, float[] currentPos) {
		final int bitmapIndex = blondGhost.getHealth() - 1;
		renderGhost(canvas, blondGhost, currentPos, mBlondGhostBitmap[bitmapIndex], mBlondTargetedBitmap[bitmapIndex]);
	}

	protected void renderGhost(Canvas canvas, TargetableItem ghost, float[] currentPos, Bitmap ghostBitmap, Bitmap targetedGhostBitmap) {
		if (!ghost.isAlive()) {
			//Ghost dead
		} else {
			//Ghost alive
			if (isTargeted(currentPos, ghost, ghostBitmap)) {
				//Ghost alive and targeted
				renderItem(canvas, targetedGhostBitmap, ghost);
				mModel.setCurrentTarget(ghost);
			} else {
				//Ghost alive and not targeted
				final int oldAlpha = mPaint.getAlpha();
				mPaint.setAlpha(210);
				renderItem(canvas, ghostBitmap, ghost);
				mPaint.setAlpha(oldAlpha);
				if (ghost == mModel.getCurrentTarget()) {
					mModel.removeTarget();
				}
			}
		}
	}

	protected void renderBulletHole(Canvas canvas, DisplayableItem bulletHole) {
		renderItem(canvas, mBulletHoleBitmap, bulletHole);
	}

	public void renderItem(final Canvas canvas, final Bitmap bitmap, final DisplayableItem item) {
		final RenderInformation renderInformation = getRenderInformation(item, bitmap);
		if (renderInformation.isOnScreen) {
			canvas.drawBitmap(bitmap, renderInformation.mPositionX, renderInformation.mPositionY, mPaint);
		}
	}

	public RenderInformation getRenderInformation(DisplayableItem item, Bitmap bitmap) {
		RenderInformation renderInformation = new RenderInformation();
		final float[] currentPosInDegree = mModel.getCurrentPosition();
		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();
		//normalization, avoid negative value.
		currentPosInDegree[0] += 180;
		currentPosInDegree[1] += 180;

		final float[] itemPosInDegree = new float[]{item.getX() + 180, item.getY() + 180};

		final float windowXInPx = currentPosInDegree[0] * mWidthRatioDegreeToPx - mScreenWidth / 2;
		final float windowYInPx = currentPosInDegree[1] * mHeightRatioDegreeToPx - mScreenHeight / 2;

		float itemXInPx = itemPosInDegree[0];
		float itemYInPx = itemPosInDegree[1];

		float diffX = currentPosInDegree[0] - itemPosInDegree[0];
		float diffY = currentPosInDegree[1] - itemPosInDegree[1];

		float distX = Math.abs(diffX);
		float distY = Math.abs(diffY);

		if (distX > 360 - distX) {
			itemXInPx = currentPosInDegree[0] - diffX + Math.signum(diffX) * 360;
		}

		if (distY > 360 - distY) {
			itemYInPx = currentPosInDegree[1] - diffY + Math.signum(diffY) * 360;
		}

		itemXInPx = itemXInPx * mWidthRatioDegreeToPx - bitmapWidth / 2;
		itemYInPx = itemYInPx * mHeightRatioDegreeToPx - bitmapHeight / 2;

		final float borderLeft = windowXInPx - bitmapWidth;
		final float borderTop = windowYInPx - bitmapHeight;
		final float borderRight = borderLeft + mScreenWidth + bitmapWidth;
		final float borderBottom = borderTop + mScreenHeight + bitmapHeight;

		renderInformation.mPositionX = itemXInPx - windowXInPx;
		renderInformation.mPositionY = itemYInPx - windowYInPx;

		if (itemXInPx > borderLeft && itemXInPx < borderRight && itemYInPx < borderBottom && itemYInPx > borderTop) {
			renderInformation.isOnScreen = true;
		}
		return renderInformation;
	}

	public void animateDyingGhost(TargetableItem ghost) {
		if (mAnimationLayer != null) {
			Bitmap bitmap;

			switch (ghost.getType()) {
				case DisplayableItemFactory.TYPE_BABY_GHOST:
					bitmap = mTargetedBabyGhostBitmap;
					break;
				case DisplayableItemFactory.TYPE_GHOST_WITH_HELMET:
					bitmap = mGhostWithHelmetTargetedBitmaps[0];
					break;
				case DisplayableItemFactory.TYPE_KING_GHOST:
					bitmap = mTargetedKingGhost;
					break;
				case DisplayableItemFactory.TYPE_BLOND_GHOST:
					bitmap = mBlondGhostBitmap[0];
					break;
				default:
					bitmap = mGhostTargetedBitmap;
					break;
			}

			final RenderInformation renderInformation = getRenderInformation(ghost, bitmap);
			mAnimationLayer.drawDyingGhost(
					new BitmapDrawable(getResources(), bitmap),
					mModel.getScoreInformation().getLastScoreAdded(),
					(int) mFontSize,
					(int) (renderInformation.mPositionX), (int) (renderInformation.mPositionY));
		}
	}

	protected void resetPainter() {
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(3);
		mPaint.setTextSize(mFontSize);
		mPaint.setTextAlign(Paint.Align.CENTER);
	}

	protected void useGreenPainter() {
		mPaint.setColor(getResources().getColor(R.color.holo_green));
		mPaint.setShadowLayer(5, 5, 5, R.color.holo_dark_green);
	}

	protected void useRedPainter() {
		mPaint.setColor(getResources().getColor(R.color.holo_red));
		mPaint.setShadowLayer(5, 5, 5, R.color.holo_dark_red);
	}

	protected float getTextSizeFromStyle(Context context, int styleId) {
		final TextView textView = new TextView(context);
		textView.setTextAppearance(context, styleId);
		return textView.getTextSize();
	}

	public void setAnimationLayer(AnimationLayer animationLayer) {
		mAnimationLayer = animationLayer;
	}

	@Override
	protected void onDetachedFromWindow() {
		mAnimationLayer.hideTopText();
		super.onDetachedFromWindow();
	}
}
