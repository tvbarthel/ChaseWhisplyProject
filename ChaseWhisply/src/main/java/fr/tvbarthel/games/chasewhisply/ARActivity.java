package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
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

import fr.tvbarthel.games.chasewhisply.beta.BetaUtils;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;

public abstract class ARActivity extends Activity implements SensorEventListener {

	public static final int RESULT_SENSOR_NOT_SUPPORTED = 111;

	//Camera
	protected Camera mCamera;
	protected int mCameraId;
	protected CameraPreview mCameraPreview;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Beta-Only
		mSensorDelay = getSharedPreferences(BetaUtils.KEY_SHARED_PREFERENCES, MODE_PRIVATE).getInt(BetaUtils.KEY_SENSOR_DELAY, SensorManager.SENSOR_DELAY_GAME);

		//Sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		if (mRotationVectorSensor == null) {
			setResult(RESULT_SENSOR_NOT_SUPPORTED, null);
			finish();
		}

		//initialize to the identity matrix
		mRotationMatrix[0] = 1;
		mRotationMatrix[4] = 1;
		mRotationMatrix[8] = 1;
		mRotationMatrix[12] = 1;

		setRemappedAxis();
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

	public void setCameraDisplayOrientation(Activity activity, android.hardware.Camera camera) {
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
			SensorManager.remapCoordinateSystem(mRotationMatrix, mRemappedXAxis, mRemappedYAxis, mRemappedRotationMatrix);
			SensorManager.getOrientation(mRemappedRotationMatrix, mOrientationVals);
			if (mCurrentRotation == Surface.ROTATION_0) {
				//For some reasons, there is a difference of 100° on the Y coordinate
				//between landscape and portrait orientation. This is a poor
				//attempt at fixing this issue.
				mOrientationVals[2] -= 1.7453292519;
			}
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

			setCameraDisplayOrientation(ARActivity.this, mCamera);

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

			//Sensor
			mSensorManager.registerListener(ARActivity.this, mRotationVectorSensor, mSensorDelay);
			onCameraReady(horizontalViewAngle, verticalViewAngle);

		}
	}

	abstract void onSmoothCoordinateChanged(float[] smoothCoordinate);

	abstract void onCameraReady(float horizontal, float vertical);

}
