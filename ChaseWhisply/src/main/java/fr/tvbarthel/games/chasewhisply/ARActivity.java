package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Arrays;

import fr.tvbarthel.games.chasewhisply.beta.BetaUtils;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;

public abstract class ARActivity extends Activity implements SensorEventListener {

    public static final int RESULT_SENSOR_NOT_SUPPORTED = 111;
    private static final float KP = 2f; // proportional gain governs rate of convergence to accelerometer/magnetometer
    private static final float KI = 0.001f; // integral gain governs rate of convergence of gyroscope biases

    //Camera
    protected Camera mCamera;
    protected int mCameraId;
    protected CameraPreview mCameraPreview;
    protected boolean mIsCameraReady;

    //Sensor
    protected SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private final float[] mRotationMatrix = new float[16];
    private final float[] mRemappedRotationMatrix = new float[16];
    protected final float[] mOrientationVals = new float[3];
    protected int mRemappedXAxis;
    protected int mRemappedYAxis;
    protected int mCurrentRotation;

    //Beta-Only
    private int mSensorDelay;

    //Compatibility Mode for Rotation Sensor
    protected float[] mAcceleration;
    protected float[] mMagneticField;
    protected float[] mGyroscope;
    protected float[] mQuaternion = new float[]{1f, 0f, 0f, 0f};
    protected float[] mIntegralError = new float[]{0f, 0f, 0f};

    protected Sensor mAccelerationSensor;
    protected Sensor mMagneticSensor;
    protected Sensor mGyroscopeSensor;
    protected long mLastUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Beta-Only
        final SharedPreferences betaSharedPreferences = getSharedPreferences(BetaUtils.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mSensorDelay = betaSharedPreferences.getInt(BetaUtils.KEY_SENSOR_DELAY, SensorManager.SENSOR_DELAY_GAME);
        final boolean isCompatibilityModeActivated = betaSharedPreferences.getBoolean(BetaUtils.KEY_COMPATIBILITY_MODE_ACTIVATED, false);

        //Sensor
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (isCompatibilityModeActivated || !useRotationSensor()) {
            useCompatibilityMode();
        }

        //initialize to the identity matrix
        mRotationMatrix[0] = 1;
        mRotationMatrix[4] = 1;
        mRotationMatrix[8] = 1;
        mRotationMatrix[12] = 1;
        setRemappedAxis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsCameraReady = false;
        //Sensor
        if (mRotationVectorSensor != null) {
            mSensorManager.registerListener(ARActivity.this, mRotationVectorSensor, mSensorDelay);
        } else {
            mSensorManager.registerListener(ARActivity.this, mAccelerationSensor, mSensorDelay);
            mSensorManager.registerListener(ARActivity.this, mMagneticSensor, mSensorDelay);
            mSensorManager.registerListener(ARActivity.this, mGyroscopeSensor, mSensorDelay);
        }
        new CameraAsyncTask().execute();
    }

    @Override
    protected void onPause() {
        releaseCamera();

        if (mCameraPreview != null) {
            final SurfaceHolder holder = mCameraPreview.getHolder();
            if (holder != null) {
                holder.removeCallback(mCameraPreview);
            }
        }

        //Sensor
        mSensorManager.unregisterListener(this);

        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final int sensorType = sensorEvent.sensor.getType();
        float[] rotationVector = null;

        rotationVector = getRotationVector(sensorEvent);

        if (rotationVector != null && mIsCameraReady) {
            updateCoordinate(rotationVector);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    private void updateCoordinate(float[] rotationVector) {
        SensorManager.getRotationMatrixFromVector(mRotationMatrix, rotationVector);
        SensorManager.remapCoordinateSystem(mRotationMatrix, mRemappedXAxis, mRemappedYAxis, mRemappedRotationMatrix);
        SensorManager.getOrientation(mRemappedRotationMatrix, mOrientationVals);
        if (mCurrentRotation == Surface.ROTATION_0) {
            //For some reasons, there is a difference of 100° on the Y coordinate
            //between landscape and portrait orientation. This is a poor
            //attempt at fixing this issue.
            mOrientationVals[2] -= 1.7453292519;
        }
        onSmoothCoordinateChanged(mOrientationVals.clone());
    }

    private boolean useRotationSensor() {
        boolean success = false;
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        if (mRotationVectorSensor != null) {
            success = true;
        } else {
            mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            if (mRotationVectorSensor != null) {
                success = true;
            }
        }
        return success;
    }

    private float[] getRotationVector(SensorEvent sensorEvent) {
        float[] rotationVector = null;
        int sensorType = sensorEvent.sensor.getType();
        if (sensorType == Sensor.TYPE_ROTATION_VECTOR
                || sensorType == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            rotationVector = getRotationVectorDirectly(sensorEvent);
        } else if (sensorType == Sensor.TYPE_ACCELEROMETER
                || sensorType == Sensor.TYPE_MAGNETIC_FIELD
                || sensorType == Sensor.TYPE_GYROSCOPE) {
            rotationVector = computeRotationVectorFromAccMagGyr(sensorEvent);
        }
        return rotationVector;
    }

    private float[] computeRotationVectorFromAccMagGyr(SensorEvent sensorEvent) {
        float[] rotationVector = null;
        int sensorType = sensorEvent.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            mAcceleration = sensorEvent.values.clone();
        } else if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagneticField = sensorEvent.values.clone();
        } else {
            mGyroscope = sensorEvent.values.clone();
        }
        long currentTime = System.nanoTime();
        if (mLastUpdate != -1) {
            float halfSamplePeriod = ((currentTime - mLastUpdate) * 1E-9f) / 2;
            if (mAcceleration != null && mMagneticField != null && mGyroscope != null) {
                AHRSUpdate(halfSamplePeriod);
                rotationVector = Arrays.copyOfRange(mQuaternion, 1, 4);
            }
        }
        mLastUpdate = currentTime;
        return rotationVector;
    }

    private float[] getRotationVectorDirectly(SensorEvent sensorEvent) {
        if (sensorEvent.values.length > 4) {
            return Arrays.copyOfRange(sensorEvent.values, 0, 4);
        } else {
            return sensorEvent.values.clone();
        }
    }

    private void useCompatibilityMode() {
        //Compatibility Mode for Rotation Sensor
        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mAccelerationSensor == null || mMagneticSensor == null || mGyroscopeSensor == null) {
            setResult(RESULT_SENSOR_NOT_SUPPORTED, null);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.toast_compatibility_mode), Toast.LENGTH_SHORT).show();
        }
    }


    public void setCameraDisplayOrientation(android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(mCameraId, info);
        int degrees = 0;
        switch (mCurrentRotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private void setRemappedAxis() {
        final int rotation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                mCurrentRotation = Surface.ROTATION_0;
                mRemappedXAxis = SensorManager.AXIS_MINUS_Y;
                mRemappedYAxis = SensorManager.AXIS_Z;
                break;

            case Surface.ROTATION_90:
                mCurrentRotation = Surface.ROTATION_90;
                mRemappedXAxis = SensorManager.AXIS_X;
                mRemappedYAxis = SensorManager.AXIS_Y;
                break;

            case Surface.ROTATION_180:
                mCurrentRotation = Surface.ROTATION_180;
                mRemappedXAxis = SensorManager.AXIS_Y;
                mRemappedYAxis = SensorManager.AXIS_MINUS_Z;
                break;

            case Surface.ROTATION_270:
                mCurrentRotation = Surface.ROTATION_270;
                mRemappedXAxis = SensorManager.AXIS_MINUS_X;
                mRemappedYAxis = SensorManager.AXIS_MINUS_Y;
                break;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    protected Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } catch (Exception e) {
            try {
                c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } catch (Exception e2) {
                mCameraId = -1;
            }
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * release camera properly
     */
    private void releaseCamera() {
        mIsCameraReady = false;
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * Java Implementation of the AHRS algorithm by
     * S.O.H. Madgwick
     * Original version, published in
     * http://code.google.com/p/imumargalgorithm30042010sohm/
     * under LGPL
     *
     * @param halfT half the sample period (in second)
     */
    private void AHRSUpdate(float halfT) {
        float norm;
        float ax = mAcceleration[0], ay = mAcceleration[1], az = mAcceleration[2];
        float mx = mMagneticField[0], my = mMagneticField[1], mz = mMagneticField[2];
        float gx = mGyroscope[0], gy = mGyroscope[1], gz = mGyroscope[2];
        float q0 = mQuaternion[0], q1 = mQuaternion[1], q2 = mQuaternion[2], q3 = mQuaternion[3];
        float hx, hy, hz, bx, bz;
        float vx, vy, vz, wx, wy, wz;
        float ex, ey, ez;

        // auxiliary variables to reduce number of repeated operations
        float q0q0 = q0 * q0;
        float q0q1 = q0 * q1;
        float q0q2 = q0 * q2;
        float q0q3 = q0 * q3;
        float q1q1 = q1 * q1;
        float q1q2 = q1 * q2;
        float q1q3 = q1 * q3;
        float q2q2 = q2 * q2;
        float q2q3 = q2 * q3;
        float q3q3 = q3 * q3;

        // normalise the measurements
        norm = (float) Math.sqrt(ax * ax + ay * ay + az * az);
        ax = ax / norm;
        ay = ay / norm;
        az = az / norm;
        norm = (float) Math.sqrt(mx * mx + my * my + mz * mz);
        mx = mx / norm;
        my = my / norm;
        mz = mz / norm;

        // compute reference direction of flux
        hx = (float) (2 * mx * (0.5 - q2q2 - q3q3) + 2 * my * (q1q2 - q0q3) + 2 * mz * (q1q3 + q0q2));
        hy = (float) (2 * mx * (q1q2 + q0q3) + 2 * my * (0.5 - q1q1 - q3q3) + 2 * mz * (q2q3 - q0q1));
        hz = (float) (2 * mx * (q1q3 - q0q2) + 2 * my * (q2q3 + q0q1) + 2 * mz * (0.5 - q1q1 - q2q2));
        bx = (float) Math.sqrt((hx * hx) + (hy * hy));
        bz = hz;

        // estimated direction of gravity and flux (v and w)
        vx = 2 * (q1q3 - q0q2);
        vy = 2 * (q0q1 + q2q3);
        vz = q0q0 - q1q1 - q2q2 + q3q3;
        wx = (float) (2 * bx * (0.5 - q2q2 - q3q3) + 2 * bz * (q1q3 - q0q2));
        wy = 2 * bx * (q1q2 - q0q3) + 2 * bz * (q0q1 + q2q3);
        wz = (float) (2 * bx * (q0q2 + q1q3) + 2 * bz * (0.5 - q1q1 - q2q2));

        // error is sum of cross product between reference direction of fields and direction measured by sensors
        ex = (ay * vz - az * vy) + (my * wz - mz * wy);
        ey = (az * vx - ax * vz) + (mz * wx - mx * wz);
        ez = (ax * vy - ay * vx) + (mx * wy - my * wx);

        // integral error scaled integral gain
        mIntegralError[0] = mIntegralError[0] + ex * KI;
        mIntegralError[1] = mIntegralError[1] + ey * KI;
        mIntegralError[2] = mIntegralError[2] + ez * KI;

        // adjusted gyroscope measurements
        gx = gx + KP * ex + mIntegralError[0];
        gy = gy + KP * ey + mIntegralError[1];
        gz = gz + KP * ez + mIntegralError[2];

        // integrate quaternion rate and normalise
        q0 = q0 + (-q1 * gx - q2 * gy - q3 * gz) * halfT;
        q1 = q1 + (q0 * gx + q2 * gz - q3 * gy) * halfT;
        q2 = q2 + (q0 * gy - q1 * gz + q3 * gx) * halfT;
        q3 = q3 + (q0 * gz + q1 * gy - q2 * gx) * halfT;

        // normalise quaternion
        norm = (float) Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);

        mQuaternion[0] = q0 / norm;
        mQuaternion[1] = q1 / norm;
        mQuaternion[2] = q2 / norm;
        mQuaternion[3] = q3 / norm;
    }

    private final class CameraAsyncTask extends AsyncTask<Void, Void, Camera> {
        @Override
        protected Camera doInBackground(Void... voids) {
            return getCameraInstance();
        }

        @Override
        protected void onPostExecute(Camera result) {
            super.onPostExecute(result);

            mCamera = result;

            if (mCamera == null) {
                finish();
                return;
            }

            setCameraDisplayOrientation(mCamera);

            //Angle view
            final Camera.Parameters params = mCamera.getParameters();
            float horizontalViewAngle = params.getHorizontalViewAngle();
            float verticalViewAngle = params.getVerticalViewAngle();

            if (mCurrentRotation == Surface.ROTATION_0 || mCurrentRotation == Surface.ROTATION_180) {
                final float temp = horizontalViewAngle;
                horizontalViewAngle = verticalViewAngle;
                verticalViewAngle = temp;
            }

            mCameraPreview = new CameraPreview(ARActivity.this, mCamera);
            setContentView(mCameraPreview);
            mIsCameraReady = true;
            onCameraReady(horizontalViewAngle, verticalViewAngle);

        }
    }

    abstract void onSmoothCoordinateChanged(float[] smoothCoordinate);

    abstract void onCameraReady(float horizontal, float vertical);

}
