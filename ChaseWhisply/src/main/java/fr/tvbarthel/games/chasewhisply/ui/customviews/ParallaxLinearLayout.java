package fr.tvbarthel.games.chasewhisply.ui.customviews;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Relative layout which use tag to render a parallax experience
 * Don't forget to register this layout as sensor rotation listener
 */
public class ParallaxLinearLayout extends LinearLayout implements SensorEventListener {

    /**
     * foreground radius
     * TODO use dp ?
     */
    private static final int MAX_RADIUS = 200;

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
     * Constructor
     *
     * @param context
     * @param attrs
     */
    public ParallaxLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("DEBUG===", "ParallaxLinearLayout : " + this.getChildCount());

        //get current device orientation
        final int rotation = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();

        //remap axis and axis's orientation to match current rotation
        remapAxis(rotation);

        mChildrenToAnimate = new ArrayList<View>();

        mCurrentRotation = new float[2];
        mCurrentRotation[0] = 0;
        mCurrentRotation[1] = 0;
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
                mRemappedViewAxisX = 1;
                //remapped x axis : use sensor axis X
                mRemappedViewAxisY = 0;
                //remapped x axis orientation : inverse on x axis
                mRemappedViewOrientationX = -1;
                //remapped y axis orientation : inverse on x axis
                mRemappedViewOrientationY = -1;
                break;

            case Surface.ROTATION_90:
                //remapped x axis : use sensor axis X
                mRemappedViewAxisX = 0;
                //remapped x axis : use sensor axis Y
                mRemappedViewAxisY = 1;
                //remapped x axis orientation : inverse  on x axis
                mRemappedViewOrientationX = -1;
                //remapped y axis orientation : default  on x axis
                mRemappedViewOrientationY = +1;
                break;

            case Surface.ROTATION_270:
                //remapped x axis : use sensor axis Y
                mRemappedViewAxisX = 1;
                //remapped x axis : use sensor axis X
                mRemappedViewAxisY = 0;
                //remapped x axis orientation : default  on x axis
                mRemappedViewOrientationX = -1;
                //remapped y axis orientation : default  on x axis
                mRemappedViewOrientationY = -1;
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
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        //TODO compute position according to this values and dT
        mCurrentRotation[mRemappedViewAxisX] += event.values[mRemappedViewAxisX];
        mCurrentRotation[mRemappedViewAxisY] += event.values[mRemappedViewAxisY];


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

        this.animate()
                .translationX(mRemappedViewOrientationX * MAX_RADIUS *
                        mCurrentRotation[mRemappedViewAxisX])
                .translationY(mRemappedViewOrientationY * MAX_RADIUS *
                        mCurrentRotation[mRemappedViewAxisY]);
        ;
        for (View v : mChildrenToAnimate) {
            v.animate()
                    .translationX(-mRemappedViewOrientationX * MAX_RADIUS / 2 *
                            mCurrentRotation[mRemappedViewAxisX])
                    .translationY(-mRemappedViewOrientationY * MAX_RADIUS / 2 *
                            mCurrentRotation[mRemappedViewAxisY]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
