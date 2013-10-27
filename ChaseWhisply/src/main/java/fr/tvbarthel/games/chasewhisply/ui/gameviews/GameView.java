package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngine;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.ui.AnimationLayer;
import fr.tvbarthel.games.chasewhisply.ui.RenderInformation;

public abstract class GameView extends View {

	protected final Paint mPaint = new Paint();
	protected final Rect mBounds = new Rect();
	private GameEngine mGameEngine;
	//ratio for displaying items
	protected float mWidthRatioDegreeToPx;
	protected float mHeightRatioDegreeToPx;
	protected float mFontSize;
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mCameraAngleInDegreeHorizontal;
	protected float mCameraAngleInDegreeVertical;
	protected float mPadding;
	protected AnimationLayer mAnimationLayer;

	/**
	 * called during View.onDraw
	 *
	 * @param c
	 */
	public abstract void onDrawing(Canvas c);

	public GameView(Context context, GameEngine gameEngine) {
		super(context);
		mGameEngine = gameEngine;

		mFontSize = getTextSizeFromStyle(context, android.R.style.TextAppearance_Holo_Large);
		mPadding = getResources().getDimensionPixelSize(R.dimen.half_padding);

		mScreenWidth = 0;
		mScreenHeight = 0;
		mWidthRatioDegreeToPx = 0;
		mHeightRatioDegreeToPx = 0;
		mCameraAngleInDegreeHorizontal = 1;
		mCameraAngleInDegreeVertical = 1;
	}

	public void setCameraAngleInDegree(float horizontal, float vertical) {
		mCameraAngleInDegreeHorizontal = horizontal;
		mCameraAngleInDegreeVertical = vertical;
	}

	public float[] getCameraAngleInDegree() {
		return new float[]{mCameraAngleInDegreeHorizontal, mCameraAngleInDegreeVertical};
	}

	private void initializeScreenDimension(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		mWidthRatioDegreeToPx = mScreenWidth / mCameraAngleInDegreeHorizontal;
		mHeightRatioDegreeToPx = mScreenHeight / mCameraAngleInDegreeVertical;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initializeScreenDimension(w, h);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		onDrawing(canvas);
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

	public void renderItem(final Canvas canvas, final Bitmap bitmap, final DisplayableItem item) {
		final RenderInformation renderInformation = getRenderInformation(item, bitmap);
		if (renderInformation.isOnScreen) {
			canvas.drawBitmap(bitmap, renderInformation.mPositionX, renderInformation.mPositionY, mPaint);
		}
	}

	public RenderInformation getRenderInformation(DisplayableItem item, Bitmap bitmap) {
		RenderInformation renderInformation = new RenderInformation();
		final float[] currentPosInDegree = mGameEngine.getCurrentPosition();
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
}
