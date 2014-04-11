package fr.tvbarthel.games.chasewhisply.ui.customviews.parallax;

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
    private static final int ANIMATION_DURATION_IN_MILLI = 200;

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
     * store last acceleration values
     */
    private float[] mLastAcceleration;

    /**
     * use to calculate dT
     */
    private long mTimeStamp;

    /**
     * Rect used to store original window
     */
    private Rect mParallaxDim;

    /**
     * Rect used to display the current window
     */
    private Rect mCurrentParallaxDim;

    /**
     * parallax Background
     */
    private ParallaxBackground mParallaxBackground;


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
            mParallaxBackground = new ParallaxBackground(context, drawableToBitmap(background));
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

        //init array
        mLastAcceleration = new float[]{0.0f, 0.0f};
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


    /**
     * Add all children in order to apply parallax motion
     *
     * @param viewGroup
     */
    private void addChildrenRecursively(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                addChildrenRecursively((ViewGroup) childView);
            } else {
                mChildrenToAnimate.add(childView);
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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //get all children which will be animated
        if (mChildrenToAnimate.isEmpty()) {
            addChildrenRecursively(this);
        }


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        params.setMargins((int) (-this.getWidth() / 2.0f), (int) (-this.getHeight() / 2.0f), 0, 0);
        this.removeView(mParallaxBackground);
        this.addView(mParallaxBackground, 0, params);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        final float accelerationX = event.values[mRemappedViewAxisX];
        final float accelerationY = event.values[mRemappedViewAxisY];
        float[] translation = new float[]{0.0f, 0.0f};


        /**
         * Process acceleration to determine motion and let ParallaxLinearLayout animate
         * it's children and it's background
         */
        if (mTimeStamp != 0) {
            final float dT = (event.timestamp - mTimeStamp) * NS2S;

            //process new value to determine current x translation
            if (Math.abs(accelerationX) < MINIMUM_ACCELERATION) {
                translation[mRemappedViewAxisX] = 0;
            } else if (Math.abs(accelerationX) > MAXIMUM_ACCELERATION) {
                translation[mRemappedViewAxisX] = mLastAcceleration[mRemappedViewAxisX] + 0.5f
                        * MAXIMUM_ACCELERATION * dT * dT;
            } else {
                translation[mRemappedViewAxisX] = mLastAcceleration[mRemappedViewAxisX] + 0.5f
                        * accelerationX * dT * dT;
                mLastAcceleration[mRemappedViewAxisX] = accelerationX;
            }

            //process new value to determine current x translation
            if (Math.abs(accelerationY) < MINIMUM_ACCELERATION) {
                translation[mRemappedViewAxisY] = 0;
            } else if (Math.abs(accelerationY) > MAXIMUM_ACCELERATION) {
                translation[mRemappedViewAxisY] = mLastAcceleration[mRemappedViewAxisY] + 0.5f
                        * MAXIMUM_ACCELERATION * dT * dT;
            } else {
                translation[mRemappedViewAxisY] = mLastAcceleration[mRemappedViewAxisY] + 0.5f
                        * accelerationY * dT * dT;
                mLastAcceleration[mRemappedViewAxisY] = accelerationY;
            }


//            //TODO extends Rect and add animation possibility, for smooth motion
//            mCurrentParallaxDim.set(
//                    mParallaxDim.left - (int) (mRemappedViewOrientationX
//                            * mParallaxBackground.getWidth() / 20 * translation[mRemappedViewAxisX]),
//                    mParallaxDim.top - (int) (mRemappedViewOrientationY
//                            * mParallaxBackground.getHeight() / 20 * translation[mRemappedViewAxisY]),
//                    mParallaxDim.right - (int) (mRemappedViewOrientationX
//                            * mParallaxBackground.getWidth() / 20 * translation[mRemappedViewAxisX]),
//                    mParallaxDim.bottom - (int) (mRemappedViewOrientationY
//                            * mParallaxBackground.getHeight() / 20 * translation[mRemappedViewAxisY])
//            );

            mParallaxBackground.animate()
                    .translationX(mRemappedViewOrientationX * this.getWidth() / 10 *
                            translation[mRemappedViewAxisX])
                    .translationY(mRemappedViewOrientationY * this.getHeight() / 10 *
                            translation[mRemappedViewAxisY]).setDuration(ANIMATION_DURATION_IN_MILLI);


            //TODO don't animate all first ground child
            //for (View v : mChildrenToAnimate) {
//            mChildrenToAnimate.get(0).animate()
//                    .translationX(-mRemappedViewOrientationX * this.getWidth() / 10 *
//                            translation[mRemappedViewAxisX])
//                    .translationY(-mRemappedViewOrientationY * this.getHeight() / 10 *
//                            translation[mRemappedViewAxisY]).setDuration(ANIMATION_DURATION_IN_MILLI);

            //}
        }

        mTimeStamp = event.timestamp;


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
