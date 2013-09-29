package fr.tvbarthel.games.chasewhisply.ui;

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

public class GameView extends View {

	private final Bitmap mCrossHairs;
	private final Bitmap mGhostBitmap;
	private final Bitmap mGhostTargetedBitmap;
	private final Bitmap[] mBlondGhostBitmap;
	private final Bitmap[] mBlondTargetedBitmap;
	private final Bitmap mAmmoBitmap;
	private final Bitmap mBulletHoleBitmap;
	private final Bitmap mBabyGhostBitmap;
	private final Bitmap mTargetedBabyGhostBitmap;
	private final Bitmap[] mGhostWithHelmetBitmaps;
	private final Bitmap[] mGhostWithHelmetTargetedBitmaps;
	private final Bitmap mKingGhost;
	private final Bitmap mTargetedKingGhost;
	private final Bitmap mHiddenGhost;
	private final String mComboString;
	private final String mScoreString;
	private final String mTimeString;
	private final Paint mPaint = new Paint();
	private final Rect mBounds = new Rect();
	private GameInformation mModel;
	//ratio for displaying items
	private float mWidthRatioDegreeToPx;
	private float mHeightRatioDegreeToPx;
	private float mFontSize;
	private int mScreenWidth;
	private int mScreenHeight;
	private float mPadding;
	private AnimationLayer mAnimationLayer;


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
		drawCrossHair(canvas);
		drawAmmo(canvas);
		drawRemainingTime(canvas);
		drawCombo(canvas);
		drawScore(canvas);
	}

	private void drawCrossHair(Canvas canvas) {
		canvas.drawBitmap(mCrossHairs, (float) (mScreenWidth - mCrossHairs.getWidth()) / 2,
				(float) (mScreenHeight - mCrossHairs.getHeight()) / 2, mPaint);
	}

	/**
	 * draw ammo on player screen
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
	 * draw remaining time
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawRemainingTime(Canvas canvas) {
		final long millis = mModel.getTime();
		final int ss = (int) (millis / 1000);
		final String remainingTime = String.format(mTimeString, ss);
		resetPainter();
		if (ss > 10) {
			useGreenPainter();
		} else {
			useRedPainter();
		}

		mPaint.getTextBounds(remainingTime, 0, remainingTime.length(), mBounds);
		canvas.drawText(remainingTime
				, mPadding + mBounds.width() / 2
				, mPadding + mPaint.getTextSize()
				, mPaint);
	}

	/**
	 * draw combo counter
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
	 * draw active items on the screen
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawDisplayableItems(Canvas canvas) {
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
	private boolean isTargeted(float[] crosshairPosition, DisplayableItem t, Bitmap targetableRes) {
		final int xInPx = (int) (t.getX() * mWidthRatioDegreeToPx);
		final int yInPx = (int) (t.getY() * mHeightRatioDegreeToPx);
		if (crosshairPosition[0] > xInPx - targetableRes.getWidth() / 2 & crosshairPosition[0] < xInPx + targetableRes.getWidth() / 2
				& crosshairPosition[1] > yInPx - targetableRes.getHeight() / 2 & crosshairPosition[1] < yInPx + targetableRes.getHeight() / 2) {
			return true;
		} else {
			return false;
		}
	}

	private void renderGhostWithHelmet(Canvas canvas, TargetableItem ghostWithHelmet, float[] currentPos) {
		final int bitmapIndex = ghostWithHelmet.getHealth() - 1;
		renderGhost(canvas, ghostWithHelmet, currentPos, mGhostWithHelmetBitmaps[bitmapIndex], mGhostWithHelmetTargetedBitmaps[bitmapIndex]);
	}

	private void renderEasyGhost(Canvas canvas, TargetableItem easyGhost, float[] currentPos) {
		renderGhost(canvas, easyGhost, currentPos, mGhostBitmap, mGhostTargetedBitmap);
	}

	private void renderBabyGhost(Canvas canvas, TargetableItem babyGhost, float[] currentPos) {
		renderGhost(canvas, babyGhost, currentPos, mBabyGhostBitmap, mTargetedBabyGhostBitmap);
	}

	private void renderHiddenGhost(Canvas canvas, TargetableItem hiddenGhost, float[] currentPos) {
		renderGhost(canvas, hiddenGhost, currentPos, mHiddenGhost, mGhostTargetedBitmap);
	}

	private void renderKingGhost(Canvas canvas, TargetableItem kingGhost, float[] currentPos) {
		renderGhost(canvas, kingGhost, currentPos, mKingGhost, mTargetedKingGhost);
	}

	private void renderBlondGhost(Canvas canvas, TargetableItem blondGhost, float[] currentPos) {
		final int bitmapIndex = blondGhost.getHealth() - 1;
		renderGhost(canvas, blondGhost, currentPos, mBlondGhostBitmap[bitmapIndex], mBlondTargetedBitmap[bitmapIndex]);
	}

	private void renderGhost(Canvas canvas, TargetableItem ghost, float[] currentPos, Bitmap ghostBitmap, Bitmap targetedGhostBitmap) {
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

	private void renderBulletHole(Canvas canvas, DisplayableItem bulletHole) {
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
					bitmap = mGhostWithHelmetTargetedBitmaps[4];
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
			mAnimationLayer.drawDyingGhost(new BitmapDrawable(getResources(), bitmap),
					(int) (renderInformation.mPositionX), (int) (renderInformation.mPositionY));
		}
	}

	private void resetPainter() {
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(3);
		mPaint.setTextSize(mFontSize);
		mPaint.setTextAlign(Paint.Align.CENTER);
	}

	private void useGreenPainter() {
		mPaint.setColor(getResources().getColor(R.color.holo_green));
		mPaint.setShadowLayer(5, 5, 5, R.color.holo_dark_green);
	}

	private void useRedPainter() {
		mPaint.setColor(getResources().getColor(R.color.holo_red));
		mPaint.setShadowLayer(5, 5, 5, R.color.holo_dark_red);
	}

	private float getTextSizeFromStyle(Context context, int styleId) {
		final TextView textView = new TextView(context);
		textView.setTextAppearance(context, styleId);
		return textView.getTextSize();
	}

	public void setAnimationLayer(AnimationLayer animationLayer) {
		mAnimationLayer = animationLayer;
	}
}
