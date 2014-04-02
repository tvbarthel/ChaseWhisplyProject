package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbarthel on 02/04/2014.
 */
public class ParallaxFragment extends Fragment {

    /**
     * foreground radius
     * TODO use dp ?
     */
    private static final int RADIUS_FOREGROUND = 200;

    /**
     * sensor manager from system service
     */
    private SensorManager mSensorManager;

    /**
     * rotation to get rotation event
     */
    private Sensor mRotationSensor;

    /**
     * listener register to rotation events
     */
    private SensorEventListener mRotationListener;

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

    private List<View> mForegroundViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForegroundViews = new ArrayList<View>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //get sensor manager
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        //get rotation sensor
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        //get current device orientation
        final int rotation = ((WindowManager) activity.getSystemService(activity.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();

        //remap axis and axis's orientation to match current rotation
        remapAxis(rotation);

        mRotationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //TODO use animator set & object animator ?
                // animate foreground views
                for (View v : mForegroundViews) {
                    v.animate()
                            .translationX(mRemappedViewOrientationX * RADIUS_FOREGROUND *
                                    event.values[mRemappedViewAxisX])
                            .translationY(mRemappedViewOrientationY * RADIUS_FOREGROUND *
                                    event.values[mRemappedViewAxisY]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        //register listener for rotation event
        mSensorManager.registerListener(
                mRotationListener,
                mRotationSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister listener
        mSensorManager.unregisterListener(mRotationListener, mRotationSensor);
    }

    /**
     * Add foreground view
     *
     * @param v added view
     */
    protected void addForegroundView(View v) {
        if (!mForegroundViews.contains(v)) {
            mForegroundViews.add(v);
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
}
