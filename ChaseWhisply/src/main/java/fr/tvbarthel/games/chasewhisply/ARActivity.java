package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;

import fr.tvbarthel.games.chasewhisply.model.SmoothCoordinate;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;

public abstract class ARActivity extends Activity implements SensorEventListener {
	protected static final float NOISE = 0.030f;
	protected static final int TEMP_SIZE = 20;
	protected static final float INITIAL_COORDINATE_WEIGHT = TEMP_SIZE / 4f;
	protected static final float BASE_LOWER_WEIGHT = 0;
	protected final float[] orientationVals = new float[3];
	protected final float[] rotationMatrix = new float[9];
	protected SmoothCoordinate mXCoordinate;
	protected SmoothCoordinate mYCoordinate;
	protected float[] magVals = new float[3];
	protected float[] accelVals = new float[3];

	//Camera
	protected Camera mCamera;
	protected CameraPreview mCameraPreview;

	//Sensor
	protected SensorManager mSensorManager;
	protected Sensor mAccelerometer;
	protected Sensor mMagneticField;

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	protected static Camera getCameraInstance() {
		Camera c = null;
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
			try {
				c = Camera.open();
			} catch (Exception ignored) {
			}
		} else {
			try {
				c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
			} catch (Exception e) {
				try {
					c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
				} catch (Exception e2) {
				}
			}
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mXCoordinate = new SmoothCoordinate(NOISE, TEMP_SIZE, INITIAL_COORDINATE_WEIGHT);
		mYCoordinate = new SmoothCoordinate(NOISE, TEMP_SIZE, INITIAL_COORDINATE_WEIGHT);

		//Sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	@Override
	protected void onResume() {
		super.onResume();

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

	/**
	 * release camera properly
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();        // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW) return;

		switch (sensorEvent.sensor.getType()) {
			case Sensor.TYPE_MAGNETIC_FIELD:
				magVals = sensorEvent.values.clone();
				break;
			case Sensor.TYPE_ACCELEROMETER:
				accelVals = sensorEvent.values.clone();
				break;
		}

		SensorManager.getRotationMatrix(rotationMatrix, null, accelVals, magVals);
		SensorManager.getOrientation(rotationMatrix, orientationVals);
		final float[] newCoordinates = new float[]{orientationVals[0], orientationVals[2]};

		float xSmoothCoordinate = mXCoordinate.update(newCoordinates[0]);
		float ySmoothCoordinate = mYCoordinate.update(newCoordinates[1]);


		onSmoothCoordinateChanged(new float[]{xSmoothCoordinate, ySmoothCoordinate});
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
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

			//Angle view
			final Camera.Parameters params = mCamera.getParameters();
			final float horizontalViewAngle = params.getHorizontalViewAngle();
			final float verticalViewAngle = params.getVerticalViewAngle();

			mCameraPreview = new CameraPreview(ARActivity.this, mCamera);
			setContentView(mCameraPreview);

			//Sensor
			mSensorManager.registerListener(ARActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
			mSensorManager.registerListener(ARActivity.this, mMagneticField, SensorManager.SENSOR_DELAY_FASTEST);

			onCameraReady(horizontalViewAngle, verticalViewAngle);

		}
	}

	abstract void onSmoothCoordinateChanged(float[] smoothCoordinate);

	abstract void onCameraReady(float horizontal, float vertical);

}
