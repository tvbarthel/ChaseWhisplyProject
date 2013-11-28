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

import java.util.Arrays;

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

	//Compatibility Mode for Rotation Sensor
	protected final int MAGNETIC_FIELD_FILTER_THRESHOLD = 10;
	protected boolean isCompatibilityModeActivated;
	protected float[] mAcceleration = new float[3];
	protected float[] mAccelerationBuffer = new float[3];
	protected float[] mMagneticField = new float[3];
	protected float[] mMagneticFieldBuffer = new float[3];
	protected int[] mMagneticFieldFilter = new int[]{0, 0, 0};
	protected float[] mCompatibilityModeRotationVector = new float[3];
	protected float[] mCompatibilityModeRotationVectorBuffer = new float[]{0f, 0f, 0f};
	protected Sensor mAccelerationSensor;
	protected Sensor mMagneticSensor;

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
		mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

		if (mRotationVectorSensor == null) {
			mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

			if (mRotationVectorSensor == null) {
				//Compatibility Mode for Rotation Sensor
				mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

				if (mAccelerationSensor == null || mMagneticSensor == null) {
					setResult(RESULT_SENSOR_NOT_SUPPORTED, null);
					finish();
				}
			}
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
		final int sensorType = sensorEvent.sensor.getType();
		float[] rotationVector = null;

		if (sensorType == Sensor.TYPE_ROTATION_VECTOR || sensorType == Sensor.TYPE_GAME_ROTATION_VECTOR) {
			if (sensorEvent.values.length > 4) {
				rotationVector = Arrays.copyOfRange(sensorEvent.values, 0, 4);
			} else {
				rotationVector = sensorEvent.values.clone();
			}

		} else if (sensorType == Sensor.TYPE_ACCELEROMETER || sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
			if (sensorType == Sensor.TYPE_ACCELEROMETER) {
				mAccelerationBuffer = sensorEvent.values.clone();
				filterAcceleration();
			} else {
				mMagneticFieldBuffer = sensorEvent.values.clone();
				filterMagneticFieldVectorBis();
				/*filterMagneticFieldVector();
				Log.d("argonne", String.valueOf(isMagneticFieldNew()));
				if(isMagneticFieldNew()) {
					mMagneticField = mMagneticFieldBuffer.clone();
				}*/
			}
			computeSupportRotationVector();
			rotationVector = mCompatibilityModeRotationVector.clone();

		}

		if (rotationVector != null) {
			SensorManager.getRotationMatrixFromVector(mRotationMatrix, rotationVector);
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
			if (mRotationVectorSensor != null) {
				mSensorManager.registerListener(ARActivity.this, mRotationVectorSensor, mSensorDelay);
			} else {
				mSensorManager.registerListener(ARActivity.this, mAccelerationSensor, mSensorDelay);
				mSensorManager.registerListener(ARActivity.this, mMagneticSensor, mSensorDelay);
			}

			onCameraReady(horizontalViewAngle, verticalViewAngle);

		}
	}

	protected void computeSupportRotationVector() {
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, mAcceleration, mMagneticField);

		mCompatibilityModeRotationVector = rotMat2rotVec(R);

		float alpha = 0.1f;
		mCompatibilityModeRotationVector[0] = alpha * mCompatibilityModeRotationVector[0] + (1 - alpha) * mCompatibilityModeRotationVectorBuffer[0];
		mCompatibilityModeRotationVector[1] = alpha * mCompatibilityModeRotationVector[1] + (1 - alpha) * mCompatibilityModeRotationVectorBuffer[1];
		mCompatibilityModeRotationVector[2] = alpha * mCompatibilityModeRotationVector[2] + (1 - alpha) * mCompatibilityModeRotationVectorBuffer[2];
		mCompatibilityModeRotationVectorBuffer = mCompatibilityModeRotationVector.clone();
	}

	protected boolean isMagneticFieldNew() {
		return Math.abs(mMagneticFieldFilter[0]) == MAGNETIC_FIELD_FILTER_THRESHOLD
				|| Math.abs(mMagneticFieldFilter[1]) == MAGNETIC_FIELD_FILTER_THRESHOLD
				|| Math.abs(mMagneticFieldFilter[2]) == MAGNETIC_FIELD_FILTER_THRESHOLD;
	}

	protected void filterAcceleration() {
		float alpha = 0.40f;
		mAcceleration[0] = alpha * mAcceleration[0] + (1 - alpha) * mAccelerationBuffer[0];
		mAcceleration[1] = alpha * mAcceleration[1] + (1 - alpha) * mAccelerationBuffer[1];
		mAcceleration[2] = alpha * mAcceleration[2] + (1 - alpha) * mAccelerationBuffer[2];
	}

	protected void filterMagneticFieldVectorBis() {
		float alpha = 0.10f;
		mMagneticField[0] = alpha * mMagneticField[0] + (1 - alpha) * mMagneticFieldBuffer[0];
		mMagneticField[1] = alpha * mMagneticField[1] + (1 - alpha) * mMagneticFieldBuffer[1];
		mMagneticField[2] = alpha * mMagneticField[2] + (1 - alpha) * mMagneticFieldBuffer[2];
	}

	protected void filterMagneticFieldVector() {
		filterMagneticFieldVectorValue(0);
		filterMagneticFieldVectorValue(1);
		filterMagneticFieldVectorValue(2);
	}

	protected void filterMagneticFieldVectorValue(int i) {
		if (mMagneticFieldBuffer[i] > mMagneticField[i]) {
			mMagneticFieldFilter[i] = Math.min(mMagneticFieldFilter[i] + 1,
					MAGNETIC_FIELD_FILTER_THRESHOLD);
		} else if(mMagneticFieldBuffer[i] < mMagneticField[i]){
			mMagneticFieldFilter[i] = Math.max(mMagneticFieldFilter[i] - 1,
					-MAGNETIC_FIELD_FILTER_THRESHOLD);
		}
	}


	protected float[] rotMat2rotVec(float[] rotMat) {
		double theta = Math.acos((rotMat[0] + rotMat[4] + rotMat[8] - 1) / 2);
		double k = 0.5 / Math.sqrt(rotMat[0] + rotMat[4] + rotMat[8] + 1);

		return new float[]{
				(float) k * (rotMat[7] - rotMat[5]),
				(float) k * (rotMat[2] - rotMat[6]),
				(float) k * (rotMat[3] - rotMat[1])};
	}

	abstract void onSmoothCoordinateChanged(float[] smoothCoordinate);

	abstract void onCameraReady(float horizontal, float vertical);

}
