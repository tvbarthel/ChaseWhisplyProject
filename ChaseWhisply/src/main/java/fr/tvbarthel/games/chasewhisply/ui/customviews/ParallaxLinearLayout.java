package fr.tvbarthel.games.chasewhisply.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.games.chasewhisply.R;

/**
 * Relative layout which use tag to render a parallax experience
 * Don't forget to register this layout as sensor rotation listener
 */
public class ParallaxLinearLayout extends RelativeLayout implements SensorEventListener {

    /**
     * constant use to convert nano second into second
     */
    private static final float NS2S = 1.0f / 1000000000.0f;

    /**
     * boundary minimum to avoid noise
     */
    private static final float MINIMUM_ACCELERATION = 0.18f;

    /**
     * boundary maximum, over it phone rotates
     */
    private static final float MAXIMUM_ACCELERATION = 3.00f;

    /**
     * duration for translation animation
     */
    private static final int ANIMATION_DURATION_IN_MILLI = 100;

    /**
     * remapped axis X according to current device orientation
     */
    private int mRemappedViewAxisX;

    /**
     * remapped axis Y according to current device orientation
     */
    private int mRemappedViewAxisY;

    /**
     * remapped orientation X according to current device orientation
     */
    private int mRemappedViewOrientationX;

    /**
     * remapped orientation Y according to current device orientations
     */
    private int mRemappedViewOrientationY;

    /**
     * Children view to animate
     */
    private List<View> mChildrenToAnimate;

    /**
     * current rotation
     */
    private float[] mCurrentRotation;

    /**
     * use to calculate dT
     */
    private long mTimeStamp;

    /**
     * parallax background
     */
    private Bitmap mParallaxBackground;

    /**
     * Rect used to store original window
     */
    private Rect mParallaxDim;

    /**
     * Rect used to display the current window
     */
    private Rect mCurrentParallaxDim;


    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public ParallaxLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ParallaxLinearLayout,
                0, 0);

        //retrieve custom attribute
        try {
            final Drawable background = a.getDrawable(R.styleable.ParallaxLinearLayout_parallax_background);
            mParallaxBackground = drawableToBitmap(background);
        } finally {
            a.recycle();
        }

        //allow onDraw for layout
        this.setWillNotDraw(false);


        //get current device orientation
        final int rotation = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();

        //remap axis and axis's orientation to match current rotation
        remapAxis(rotation);

        mChildrenToAnimate = new ArrayList<View>();

        mCurrentRotation = new float[]{0.0f, 0.0f};

        mParallaxDim = new Rect(0, 0, 0, 0);
        mCurrentParallaxDim = new Rect(0, 0, 0, 0);

        mTimeStamp = 0;

    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    private void addChildrenRecursively(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                addChildrenRecursively((ViewGroup) childView);
            } else {
                mChildrenToAnimate.add(childView);
                Log.d("DEBUG===", "child added : " + childView.toString());
            }
        }

    }

    /**
     * Used to remap axis and axis' orientation according to the current device rotation
     *
     * @param rotation current device rotation
     */
    private void remapAxis(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                //remapped x axis : use sensor axis Y
                mRemappedViewAxisX = 0;
                //remapped x axis : use sensor axis X
                mRemappedViewAxisY = 1;
                //remapped x axis orientation : default on x axis
                mRemappedViewOrientationX = +1;
                //remapped y axis orientation : inverse on y axis
                mRemappedViewOrientationY = -1;
                break;

            case Surface.ROTATION_90:
                //remapped x axis : use sensor axis X
                mRemappedViewAxisX = 1;
                //remapped x axis : use sensor axis Y
                mRemappedViewAxisY = 0;
                //remapped x axis orientation : inverse  on x axis
                mRemappedViewOrientationX = -1;
                //remapped y axis orientation : inverse  on y axis
                mRemappedViewOrientationY = -1;
                break;

            case Surface.ROTATION_270:
                //remapped x axis : use sensor axis Y
                mRemappedViewAxisX = 1;
                //remapped x axis : use sensor axis X
                mRemappedViewAxisY = 0;
                //remapped x axis orientation : default  on x axis
                mRemappedViewOrientationX = +1;
                //remapped y axis orientation : default  on y axis
                mRemappedViewOrientationY = +1;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw the background image with mCurrentParallaxDim as subset of original background
        canvas.drawBitmap(mParallaxBackground, mCurrentParallaxDim, new Rect(0, 0, this.getRight(), this.getBottom()), null);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //get all children which will be animated
        if (mChildrenToAnimate.isEmpty()) {
            addChildrenRecursively(this);
        }

        if (mParallaxDim.left == 0 && mParallaxDim.right == 0) {

            //layout ratio
            final float destRatio = (float) this.getWidth() / this.getHeight();

            //background ratio
            final float srcRatio = (float) mParallaxBackground.getWidth() / mParallaxBackground.getHeight();

            /**
             * calculate a window for source background, which matches layout ratio
             * to avoid stretched rendering
             */

            //original width and height
            double fitWidth = mParallaxBackground.getWidth();
            double fitHeight = mParallaxBackground.getHeight();

            //compare ration to obtain width and height which match the destination ratio
            if (srcRatio < destRatio) {
                fitHeight = fitWidth / destRatio;
            } else {
                fitWidth = fitHeight * destRatio;
            }


            //remove padding used during parallax motion
            final int windowWidth = (int) (fitWidth - 2 * fitWidth * MAXIMUM_ACCELERATION / 20);
            final int windowHeight = (int) (fitHeight - 2 * fitHeight * MAXIMUM_ACCELERATION / 20);

            //left corner window = original center - windowWidth/2
            final int left = (int) (mParallaxBackground.getWidth() / 2.0f - windowWidth / 2.0f);
            //top corner window = original center - windowHeight/2
            final int top = (int) (mParallaxBackground.getHeight() / 2.0f - windowHeight / 2.0f);
            final int right = left + windowWidth;
            final int bottom = top + windowHeight;

            mParallaxDim.set(left, top, right, bottom);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float accelerationX = event.values[mRemappedViewAxisX];
        final float accelerationY = event.values[mRemappedViewAxisY];
        float translationX;
        float translationY;


        if (mTimeStamp != 0) {
            final float dT = (event.timestamp - mTimeStamp) * NS2S;

            //process new value to determine current x translation
            if (Math.abs(accelerationX) < MINIMUM_ACCELERATION) {
                translationX = 0;
            } else if (Math.abs(accelerationX) > MAXIMUM_ACCELERATION) {
                translationX = mCurrentRotation[mRemappedViewAxisX] + 0.5f
                        * MAXIMUM_ACCELERATION * dT * dT;
            } else {
                translationX = mCurrentRotation[mRemappedViewAxisX] + 0.5f
                        * accelerationX * dT * dT;
                mCurrentRotation[mRemappedViewAxisX] = accelerationX;
            }

            //process new value to determine current x translation
            if (Math.abs(accelerationY) < MINIMUM_ACCELERATION) {
                translationY = 0;
            } else if (Math.abs(accelerationY) > MAXIMUM_ACCELERATION) {
                translationY = mCurrentRotation[mRemappedViewAxisY] + 0.5f
                        * MAXIMUM_ACCELERATION * dT * dT;
            } else {
                translationY = mCurrentRotation[mRemappedViewAxisY] + 0.5f
                        * accelerationY * dT * dT;
                mCurrentRotation[mRemappedViewAxisY] = accelerationY;
            }

            /**
             * Animate the layout and its children
             *
             * Can't only animate children since they won't be drawn outside the ParallaxLinearLayout
             * boundaries.
             *
             * That's the reason why we decided to apply maximum radius motion to the
             * ParallaxLinearLayout and then apply translation in the opposite direction to distinguish
             * planes. Basically apply the same radius motion in
             * opposite direction to your child and they won't move.
             */

            //TODO apply maximum radius to background motion, add custom xml properties
//            this.animate()
//                    .translationX(mRemappedViewOrientationX * this.getWidth() / 10 *
//                            translationX)
//                    .translationY(mRemappedViewOrientationY * this.getHeight() / 10 *
//                            translationY).setDuration(ANIMATION_DURATION_IN_MILLI);
//            ;

            //TODO extends Rect and add animation possibility, for smooth motion
            mCurrentParallaxDim.set(
                    mParallaxDim.left -
                            (int) (mRemappedViewOrientationX * mParallaxBackground.getWidth() / 20 * translationX),
                    mParallaxDim.top -
                            (int) (mRemappedViewOrientationY * mParallaxBackground.getHeight() / 20 * translationY),
                    mParallaxDim.right -
                            (int) (mRemappedViewOrientationX * mParallaxBackground.getWidth() / 20 * translationX),
                    mParallaxDim.bottom -
                            (int) (mRemappedViewOrientationY * mParallaxBackground.getHeight() / 20 * translationY)
            );

            this.invalidate();

            //TODO don't animate all first ground child
//            for (View v : mChildrenToAnimate) {
//                v.animate()
//                        .translationX(-mRemappedViewOrientationX * this.getWidth() / 10 *
//                                translationX)
//                        .translationY(-mRemappedViewOrientationY * this.getHeight() / 10 *
//                                translationY).setDuration(ANIMATION_DURATION_IN_MILLI);
//
//            }
        }

        mTimeStamp = event.timestamp;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
