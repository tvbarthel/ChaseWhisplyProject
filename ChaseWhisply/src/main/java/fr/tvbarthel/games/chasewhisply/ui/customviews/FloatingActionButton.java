package fr.tvbarthel.games.chasewhisply.ui.customviews;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import fr.tvbarthel.games.chasewhisply.R;

/**
 * Custom component used to reproduce the FAB icon from Android L.
 */
public class FloatingActionButton extends View {

    /**
     * Default size for a FAB as mentioned in guidelines.
     * http://www.google.com/design/spec/components/buttons.html#buttons-main-buttons
     */
    private static final int DEFAULT_SIZE_IN_DP = 56;

    /**
     * Size of the icon displayed in the FAB according to the guidelines.
     * http://www.google.com/design/spec/components/buttons.html#buttons-main-buttons
     */
    private static final int ICON_SIZE_IN_DP = 24;

    /**
     * Padding use to render the shadow.
     */
    private static final int SHADOW_PADDING_IN_DP = 12;

    /**
     * Default color used as background.
     */
    private static final int DEFAULT_BG_COLOR = Color.argb(255, 250, 250, 250);

    /**
     * Default color used as shadow color.
     */
    private static final int DEFAULT_SHADOW_COLOR = Color.argb(100, 0, 0, 0);

    /**
     * Duration used for the pressed animation.
     */
    private static final int FADE_DURATION_IN_MILLI = 400;

    /**
     * FAB alpha when the state is normal.
     */
    private static final float ALPHA_NORMAL_STATE = 1.0f;

    /**
     * FAB alpha when the state is pressed.
     */
    private static final float ALPHA_PRESSED_STATE = 0.3f;

    /**
     * Paint used to render the background.
     */
    private Paint mBackgroundPaint;

    /**
     * Paint used to render the icon.
     */
    private Paint mIconPaint;

    /**
     * Circle center.
     */
    private PointF mCenter;

    /**
     * Circle radius.
     */
    private float mRadius;

    /**
     * Bitmap used to render the icon.
     */
    private Bitmap mBitmapIcon;

    /**
     * Rectangle used as destination rect to draw the bitmap icon.
     */
    private RectF mDestRect;

    /**
     * Rectangle used as source rect to draw the bitmap icon.
     */
    private RectF mSrcRect;

    /**
     * Color used in the FAB background.
     */
    private int mBackgroundColor;

    private Matrix mRotationMatrix;

    /**
     * Constructor.
     *
     * @param context holding context.
     */
    public FloatingActionButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, null);
        }
    }

    /**
     * Constructor.
     *
     * @param context holding context.
     * @param attrs   attrs from xml.
     */
    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    /**
     * Constructor.
     *
     * @param context      holding context.
     * @param attrs        attrs from xml.
     * @param defStyleAttr style from xml.
     */
    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        float specRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_SIZE_IN_DP,
                getResources().getDisplayMetrics()

        );
        float specPadding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                SHADOW_PADDING_IN_DP,
                getResources().getDisplayMetrics()
        );

        mRadius = specRadius / 2;

        int dimen = MeasureSpec.makeMeasureSpec(
                (int) (specRadius + specPadding),
                MeasureSpec.EXACTLY
        );

        super.onMeasure(dimen, dimen);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mCenter == null) {
            mCenter = new PointF(w / 2, h / 2);
        } else {
            mCenter.set(w / 2, h / 2);
        }

        initDestRect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBackgroundPaint == null) {
            initPaint();
        }

        // draw the rounded background.
        canvas.drawCircle(mCenter.x, mCenter.y - 2.5f, mRadius, mBackgroundPaint);

        // draw the icon.
        if (mBitmapIcon != null) {
            canvas.drawBitmap(mBitmapIcon, mRotationMatrix, mIconPaint);
        }

        super.onDraw(canvas);
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
        if (isPressed()) {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(
                    this, "alpha", ALPHA_NORMAL_STATE, ALPHA_PRESSED_STATE);
            fadeIn.setDuration(FADE_DURATION_IN_MILLI);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.start();
        } else {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(
                    this, "alpha", ALPHA_PRESSED_STATE, ALPHA_NORMAL_STATE);
            fadeOut.setDuration(FADE_DURATION_IN_MILLI);
            fadeOut.setInterpolator(new DecelerateInterpolator());
            fadeOut.start();
        }
    }

    @Override
    public void setRotation(float rotation) {
        if (mRotationMatrix != null) {
            mRotationMatrix.reset();
            mRotationMatrix.setRectToRect(mSrcRect, mDestRect, Matrix.ScaleToFit.CENTER);
            mRotationMatrix.postRotate(rotation, this.getWidth() / 2, this.getHeight() / 2);
            invalidate();
        }
    }

    /**
     * Set the FAB icon programmically.
     *
     * @param resId res id of the icon.
     */
    public void setIcon(@DrawableRes int resId) {
        mBitmapIcon = BitmapFactory.decodeResource(getResources(), resId);
        mSrcRect = new RectF(0, 0, mBitmapIcon.getWidth(), mBitmapIcon.getHeight());
    }

    /**
     * Initialize the dest rect used to evaluate the icon position.
     */
    private void initDestRect() {

        float iconDimen = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ICON_SIZE_IN_DP,
                getResources().getDisplayMetrics()
        );

        int radius = (int) iconDimen / 2;

        mDestRect = new RectF(
                (int) (mCenter.x - radius),
                (int) (mCenter.y - radius),
                (int) (mCenter.x + radius),
                (int) (mCenter.y + radius)
        );
    }

    /**
     * Initialize the painter used to draw the background.
     */
    private void initPaint() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Drawable bg = this.getBackground();
        if (bg != null && bg instanceof ColorDrawable) {
            mBackgroundPaint.setColor(((ColorDrawable) bg).getColor());
        }
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setShadowLayer(SHADOW_PADDING_IN_DP, 1, 4, DEFAULT_SHADOW_COLOR);

        mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRotationMatrix = new Matrix();
        mRotationMatrix.setRectToRect(mSrcRect, mDestRect, Matrix.ScaleToFit.CENTER);
    }

    /**
     * Initialize the custom component.
     *
     * @param context holding context.
     * @param attrs   attrs from xml.
     */
    private void init(Context context, AttributeSet attrs) {

        mBackgroundColor = DEFAULT_BG_COLOR;
        this.setClickable(true);

        if (attrs == null) {
            return;
        }

        TypedArray customAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FloatingActionButton,
                0, 0
        );

        mBackgroundColor = customAttrs.getColor(
                R.styleable.FloatingActionButton_fab_color,
                DEFAULT_BG_COLOR
        );

        int resId = customAttrs.getResourceId(
                R.styleable.FloatingActionButton_fab_icon,
                -1
        );

        if (resId != -1) {
            setIcon(resId);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        invalidate();
    }
}
