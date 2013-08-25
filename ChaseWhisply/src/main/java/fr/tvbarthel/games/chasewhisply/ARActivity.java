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

import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;

public abstract class ARActivity extends Activity implements SensorEventListener {
	//Camera
	protected Camera mCamera;
	protected CameraPreview mCameraPreview;

	//Sensor
	protected SensorManager mSensorManager;
	private Sensor mRotationVectorSensor;
	private final float[] mRotationMatrix = new float[16];
	protected final float[] mOrientationVals = new float[3];

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
			} catch (Exception e2) {
			}
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

		//initialize to the identity matrix
		mRotationMatrix[0] = 1;
		mRotationMatrix[4] = 1;
		mRotationMatrix[8] = 1;
		mRotationMatrix[12] = 1;
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
		if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(mRotationMatrix, sensorEvent.values);
			SensorManager.getOrientation(mRotationMatrix, mOrientationVals);
			onSmoothCoordinateChanged(new float[]{mOrientationVals[0], mOrientationVals[2]});
		}
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
			mSensorManager.registerListener(ARActivity.this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
			onCameraReady(horizontalViewAngle, verticalViewAngle);

		}
	}

	abstract void onSmoothCoordinateChanged(float[] smoothCoordinate);

	abstract void onCameraReady(float horizontal, float vertical);

}
