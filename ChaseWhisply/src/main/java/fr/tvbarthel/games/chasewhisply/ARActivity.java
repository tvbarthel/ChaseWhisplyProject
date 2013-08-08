package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.model.WeightedCoordinate;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;

public abstract class ARActivity extends Activity implements SensorEventListener {
	protected static final float NOISE = 0.03f;
	protected static final int TEMP_SIZE = 20;
	protected final float[] orientationVals = new float[3];
	protected final float[] rotationMatrix = new float[9];
	protected ArrayList<WeightedCoordinate> mXTempCoordinates;
	protected ArrayList<WeightedCoordinate> mYTempCoordinates;
	protected int mXTempCoordinateCursor = 0;
	protected int mYTempCoordinateCursor = 0;
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
		try {
			c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
		} catch (Exception e) {
			try {
				c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
			} catch (Exception ignored) {
			}
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mXTempCoordinates = new ArrayList<WeightedCoordinate>();
		mYTempCoordinates = new ArrayList<WeightedCoordinate>();

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

		final float[] oldSmoothCoordinates = getSmoothCoordinates();
		final float[] newCoordinates = new float[]{orientationVals[0], orientationVals[2]};
		final boolean shouldWeChangeXCoordinate = shouldWeChangeXCoordinate(oldSmoothCoordinates[0], newCoordinates[0]);
		final boolean shouldWeChangeYCoordinate = shouldWeChangeYCoordinate(oldSmoothCoordinates[1], newCoordinates[1]);

		if (shouldWeChangeXCoordinate) {
			changeXCoordinate(oldSmoothCoordinates[0], newCoordinates[0]);
		}

		if (shouldWeChangeYCoordinate) {
			changeYCoordinate(oldSmoothCoordinates[1], newCoordinates[1]);
		}

		onSmoothCoordinateChanged(getSmoothCoordinates());
	}

	protected void changeYCoordinate(float oldCoordinate, float newCoordinate) {
		mYTempCoordinateCursor = changeCoordinate(mYTempCoordinates, mYTempCoordinateCursor, oldCoordinate, newCoordinate);
	}

	protected void changeXCoordinate(float oldCoordinate, float newCoordinate) {
		mXTempCoordinateCursor = changeCoordinate(mXTempCoordinates, mXTempCoordinateCursor, oldCoordinate, newCoordinate);
	}

	protected int changeCoordinate(ArrayList<WeightedCoordinate> tempWeightedCoordinates, int tempCoordinateCursor, float oldCoordinate, float newCoordinate) {
		if (tempWeightedCoordinates.size() != 0
				&& !isEqualSign(oldCoordinate, newCoordinate)) {
			tempWeightedCoordinates.clear();
			tempCoordinateCursor = 0;
		}

		if (tempWeightedCoordinates.size() < TEMP_SIZE) {
			tempWeightedCoordinates.add(tempCoordinateCursor, new WeightedCoordinate(newCoordinate, 1));
		} else {
			tempWeightedCoordinates.set(tempCoordinateCursor, new WeightedCoordinate(newCoordinate, 1));
		}
		return (tempCoordinateCursor + 1) % TEMP_SIZE;
	}

	protected boolean shouldWeChangeXCoordinate(float oldCoordinate, float newCoordinate) {
		return shouldWeChangeCoordinate(mXTempCoordinates, oldCoordinate, newCoordinate);
	}

	protected boolean shouldWeChangeYCoordinate(float oldCoordinate, float newCoordinate) {
		return shouldWeChangeCoordinate(mYTempCoordinates, oldCoordinate, newCoordinate);
	}

	protected boolean shouldWeChangeCoordinate(ArrayList<WeightedCoordinate> tempWeightedCoordinates, float oldCoordinate, float newCoordinate) {
		boolean shouldWeChangeCoordinate = false;
		if (tempWeightedCoordinates.size() != 0) {
			if (isAbsDiffGreaterThanNoise(oldCoordinate, newCoordinate, NOISE)) {
				shouldWeChangeCoordinate = true;
			}
		} else {
			shouldWeChangeCoordinate = true;
		}
		return shouldWeChangeCoordinate;
	}

	protected boolean isEqualSign(float a, float b) {
		return a * b > 0;
	}

	protected boolean isAbsDiffGreaterThanNoise(float a, float b, float noise) {
		return Math.abs(a - b) > noise;
	}

	/**
	 * Compute the average value of all the {@link WeightedCoordinate} in {@code weightedCoordinates}
	 *
	 * @param weightedCoordinates
	 * @return the coordinate average value
	 */
	protected float getSmoothCoordinate(ArrayList<WeightedCoordinate> weightedCoordinates) {
		float smoothXCoordinate = 0;
		float totalWeight = 0;

		for (WeightedCoordinate weightedCoordinate : weightedCoordinates) {
			smoothXCoordinate += weightedCoordinate.getWeightedValue();
			totalWeight += weightedCoordinate.getWeight();
		}

		return smoothXCoordinate / totalWeight;
	}

	protected float getSmoothXCoordinate() {
		return getSmoothCoordinate(mXTempCoordinates);
	}

	protected float getSmoothYCoordinate() {
		return getSmoothCoordinate(mYTempCoordinates);
	}

	protected float[] getSmoothCoordinates() {
		return new float[]{getSmoothXCoordinate(), getSmoothYCoordinate()};
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
			mSensorManager.registerListener(ARActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
			mSensorManager.registerListener(ARActivity.this, mMagneticField, SensorManager.SENSOR_DELAY_GAME);

			onCameraReady(horizontalViewAngle, verticalViewAngle);

		}
	}

	abstract void onSmoothCoordinateChanged(float[] smoothCoordinate);

	abstract void onCameraReady(float horizontal, float vertical);

}
