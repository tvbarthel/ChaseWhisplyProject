package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
    private Matrix mMatrix = new Matrix();
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
            mMatrix.reset();
            mMatrix.setTranslate(renderInformation.mPositionX, renderInformation.mPositionY);
            mMatrix.postRotate(renderInformation.mPositionZ, renderInformation.mPositionX + bitmap.getWidth() / 2,
                    renderInformation.mPositionY + bitmap.getHeight() / 2);
            canvas.drawBitmap(bitmap, mMatrix, mPaint);
        }
    }

    public RenderInformation getRenderInformation(DisplayableItem item, Bitmap bitmap) {
        RenderInformation renderInformation = new RenderInformation();
        final float[] currentPosInDegree = mGameEngine.getCurrentPosition();

        // Normalization, avoid negative value.
        currentPosInDegree[0] += 180;
        currentPosInDegree[1] += 180;

        // Get the smallest angle between the current position and the position of the item.
        // Torus ?
        final float[] itemPosInDegree = new float[]{item.getX() + 180, item.getY() + 180};

        float diffX = currentPosInDegree[0] - itemPosInDegree[0];
        float diffY = currentPosInDegree[1] - itemPosInDegree[1];

        float distX = Math.abs(diffX);
        float distY = Math.abs(diffY);

        if (distX > 360 - distX) {
            itemPosInDegree[0] = currentPosInDegree[0] - diffX + Math.signum(diffX) * 360;
        }

        if (distY > 360 - distY) {
            itemPosInDegree[1] = currentPosInDegree[1] - diffY + Math.signum(diffY) * 360;
        }

        // Get the current coordinates in pixels.
        // (the current coordinate is the coordinate of the center of the device screen)
        float currentXInPx = currentPosInDegree[0] * mWidthRatioDegreeToPx;
        float currentYInPy = currentPosInDegree[1] * mHeightRatioDegreeToPx;

        // Get the item coordinates in pixels;
        float itemXInPx = itemPosInDegree[0] * mWidthRatioDegreeToPx;
        float itemYInPx = itemPosInDegree[1] * mHeightRatioDegreeToPx;

        // Translate the item coordinates.
        // The point of reference is now the center of the screen.
        float itemXInPxAfterTranslation = itemXInPx - currentXInPx;
        float itemYInPxAfterTranslation = itemYInPx - currentYInPy;

        // Rotate the item coordinates.
        float rotationAngle = (float) Math.toRadians(-currentPosInDegree[2]);
        float itemXInPxAfterRotation = (float) (itemXInPxAfterTranslation * Math.cos(rotationAngle)
                + itemYInPxAfterTranslation * Math.sin(rotationAngle));
        float itemYInPxAfterRotation = (float) (-itemXInPxAfterTranslation * Math.sin(rotationAngle)
                + itemYInPxAfterTranslation * Math.cos(rotationAngle));

        // Translate the item coordinates
        // The point of reference is now the top left corner of the screen.
        itemXInPx = itemXInPxAfterRotation + mScreenWidth / 2;
        itemYInPx = itemYInPxAfterRotation + mScreenHeight / 2;

        // Set the render information.
        renderInformation.mPositionX = itemXInPx - bitmap.getWidth() / 2;
        renderInformation.mPositionY = itemYInPx - bitmap.getHeight() / 2;
        renderInformation.mPositionZ = currentPosInDegree[2];

        // Check if the item should be rendered
        renderInformation.isOnScreen = renderInformation.mPositionX > -bitmap.getWidth()
                && renderInformation.mPositionY > -bitmap.getHeight()
                && renderInformation.mPositionX < mScreenWidth
                && renderInformation.mPositionY < mScreenHeight;

        return renderInformation;
    }

    protected void resetPainter() {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(mFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
    }

    protected void useGreenPainter() {
        mPaint.setColor(getResources().getColor(R.color.holo_green));
        mPaint.setShadowLayer(5, 5, 5, R.color.holo_dark_green);
    }

    protected void useRedPainter() {
        mPaint.setColor(getResources().getColor(R.color.holo_red));
        mPaint.setShadowLayer(5, 5, 5, R.color.holo_dark_red);
    }

    protected void useTransparentBlackPainter() {
        mPaint.setColor(getResources().getColor(R.color.transparent_grey));
        mPaint.setShadowLayer(0, 0, 0, R.color.transparent_grey);
    }

    protected void useTransparentGreenPainter() {
        mPaint.setColor(getResources().getColor(R.color.transparent_green));
        mPaint.setShadowLayer(0, 0, 0, R.color.transparent_green);
    }

    protected void useWhitePainter() {
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setShadowLayer(5, 5, 5, R.color.alpha_shadow);
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
