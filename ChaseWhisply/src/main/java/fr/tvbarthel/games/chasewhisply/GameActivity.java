package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.games.chasewhisply.mechanics.GameEngine;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.TimeLimitedGameEngine;
import fr.tvbarthel.games.chasewhisply.model.Weapon;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;
import fr.tvbarthel.games.chasewhisply.ui.GameView;

public class GameActivity extends Activity implements SensorEventListener {

	//Game default values
	private static final long DEFAULT_REMAINING_TIME = 2 * 60 * 1000;

	private Camera mCamera;
	private CameraPreview mCameraPreview;
	private GameEngine mGameEngine;
	private GameView mGameView;

	//Sensor
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagneticField;
	private float[] magVals = new float[3];
	private float[] accelVals = new float[3];
	private float[] orientationVals = new float[3];
	private float[] mLastOrientationVals = null;
	private float[] rotationMatrix = new float[9];
	private static final float NOISE = 0.10f;
	private final float[] mCoordinate = new float[2];

	//View Angle
	private float mHorizontalViewAngle;
	private float mVerticalViewAngle;

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
		mCamera = getCameraInstance();

		if (mCamera == null) {
			finish();
		}

		mCameraPreview = new CameraPreview(this, mCamera);
		setContentView(mCameraPreview);

		//create new game information
		final GameInformation gameInformation = new GameInformation(DEFAULT_REMAINING_TIME, new Weapon());

		//instantiate GameView with GameModel
		mGameView = new GameView(this, gameInformation);
		mGameView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO call fire method
				mGameView.invalidate();
			}
		});
		addContentView(mGameView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
				, ViewGroup.LayoutParams.WRAP_CONTENT));

		//Sensor
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);

		//Angle view
		final Camera.Parameters params = mCamera.getParameters();
		mHorizontalViewAngle = params.getHorizontalViewAngle();
		mVerticalViewAngle = params.getVerticalViewAngle();

		//instantiate game engine
		mGameEngine = new TimeLimitedGameEngine(gameInformation);
		mGameEngine.setSceneheight((int) Math.floor(mVerticalViewAngle));
		mGameEngine.setSceneWidth((int) Math.floor(mHorizontalViewAngle));
		//TODO mGameEngine.startGame();

		//TODO delete
		mGameView.setCoordinate(mCoordinate);
	}


	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();

		//Sensor
		mSensorManager.unregisterListener(this);
	}

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
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

		mSensorManager.getRotationMatrix(rotationMatrix, null, accelVals, magVals);
		mSensorManager.getOrientation(rotationMatrix, orientationVals);

		if (mLastOrientationVals != null) {
			if (Math.abs(mLastOrientationVals[0] - orientationVals[0]) > NOISE)
				mLastOrientationVals[0] = orientationVals[0];
			if (Math.abs(mLastOrientationVals[1] - orientationVals[1]) > NOISE)
				mLastOrientationVals[1] = orientationVals[1];
			if (Math.abs(mLastOrientationVals[2] - orientationVals[2]) > NOISE)
				mLastOrientationVals[2] = orientationVals[2];

			mCoordinate[0] = (float)Math.toDegrees(mLastOrientationVals[0]);
			mCoordinate[1] = (float)Math.toDegrees(mLastOrientationVals[2]);

			//TODO update DisplayableItemsList
			mGameEngine.changePosition(mCoordinate[0], mCoordinate[1]);
			mGameView.invalidate();

		} else {
			mLastOrientationVals = orientationVals.clone();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}


}
