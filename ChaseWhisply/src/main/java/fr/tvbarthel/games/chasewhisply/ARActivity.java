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

import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;

public abstract class ARActivity extends Activity implements SensorEventListener {
	private static final float NOISE = 0.03f;
	private static final int TEMP_SIZE = 20;
	private final float[] orientationVals = new float[3];
	private final float[] rotationMatrix = new float[9];
	private final float[] mCoordinate = new float[2];
	private final ArrayList<float[]> mCoordinateTemp = new ArrayList<float[]>();
	private float[] magVals = new float[3];
	private float[] accelVals = new float[3];
	//Camera
	private Camera mCamera;
	private CameraPreview mCameraPreview;
	//Sensor
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagneticField;
	private int mCoordinateTempCursor = 0;

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance() {
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
		boolean storeCoordinate = false;
		mCoordinate[0] = orientationVals[0];
		mCoordinate[1] = orientationVals[2];

		float[] smoothCoordinate = getSmoothCoordinate();

		if (mCoordinateTemp.size() != 0) {
			if (Math.abs(smoothCoordinate[0] - mCoordinate[0]) > NOISE)
				storeCoordinate = true;
			if (Math.abs(smoothCoordinate[1] - mCoordinate[1]) > NOISE)
				storeCoordinate = true;
		} else {
			storeCoordinate = true;
		}

		//store current coordinate
		if (storeCoordinate) {
			if (mCoordinateTemp.size() != 0
					&& (smoothCoordinate[0] * mCoordinate[0] < 0 || smoothCoordinate[1] * mCoordinate[1] < 0)) {
				mCoordinateTemp.clear();
				mCoordinateTempCursor = 0;
			}

			if (mCoordinateTemp.size() < TEMP_SIZE) {
				mCoordinateTemp.add(mCoordinateTempCursor, mCoordinate.clone());
			} else {
				mCoordinateTemp.set(mCoordinateTempCursor, mCoordinate.clone());
			}
			mCoordinateTempCursor = (mCoordinateTempCursor + 1) % TEMP_SIZE;

			smoothCoordinate = getSmoothCoordinate();

			onSmoothCoordinateChanged(smoothCoordinate);
		}
	}

	public float[] getSmoothCoordinate() {
		final float[] smoothCoordinate = new float[2];
		final int coordinateSize = mCoordinateTemp.size();

		for (float[] temp : mCoordinateTemp) {
			smoothCoordinate[0] += temp[0];
			smoothCoordinate[1] += temp[1];
		}

		smoothCoordinate[0] /= coordinateSize;
		smoothCoordinate[1] /= coordinateSize;
		return smoothCoordinate;
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
