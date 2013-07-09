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

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.mechanics.GameEngine;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.TimeLimitedGameEngine;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.WeaponFactory;
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
	private static final float NOISE = 0.03f;
	private static final int TEMP_SIZE = 10;
	private final float[] mCoordinate = new float[2];

	private ArrayList<float[]> mCoordinateTemp = new ArrayList<float[]>();
	private int mCoordinateTempCursor = 0;

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

		//Angle view
		final Camera.Parameters params = mCamera.getParameters();
		mHorizontalViewAngle = params.getHorizontalViewAngle();
		mVerticalViewAngle = params.getVerticalViewAngle();

		//create new game information
		final GameInformation gameInformation = new GameInformation(DEFAULT_REMAINING_TIME, WeaponFactory.createBasicWeapon());
		gameInformation.setSceneWidth((int) Math.floor(mHorizontalViewAngle));
		gameInformation.setSceneHeight((int) Math.floor(mVerticalViewAngle));
		gameInformation.addTargetableItem(DisplayableItemFactory.createEasyGhost());

		//instantiate GameView with GameModel
		mGameView = new GameView(this, gameInformation);
		mGameView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mGameEngine.fire();
				mGameView.invalidate();
			}
		});
		addContentView(mGameView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
				, ViewGroup.LayoutParams.WRAP_CONTENT));

		//Sensor
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);

		//instantiate game engine
		mGameEngine = new TimeLimitedGameEngine(gameInformation);
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
		boolean storeCoordinate = false;
		mCoordinate[0] = orientationVals[0];
		mCoordinate[1] = orientationVals[2];

		if (mCoordinateTemp.size() != 0) {
			float[] smoothCoordinate = getSmoothCoordinate();
			if (Math.abs(smoothCoordinate[0] - orientationVals[0]) > NOISE)
				storeCoordinate = true;
			if (Math.abs(smoothCoordinate[1] - orientationVals[2]) > NOISE)
				storeCoordinate = true;
		} else {
			storeCoordinate = true;
		}

		//store current coordinate
		if (storeCoordinate) {
			if (mCoordinateTemp.size() < TEMP_SIZE) {
				mCoordinateTemp.add(mCoordinateTempCursor, mCoordinate.clone());
			} else {
				mCoordinateTemp.set(mCoordinateTempCursor, mCoordinate.clone());
			}
			mCoordinateTempCursor = (mCoordinateTempCursor + 1) % TEMP_SIZE;
			//TODO update DisplayableItemsList
			float[] smoothCoordinate = getSmoothCoordinate();
			mGameEngine.changePosition((float) Math.toDegrees(smoothCoordinate[0]), (float) Math.toDegrees(smoothCoordinate[1]));
			mGameView.invalidate();
		}

	}

	public float[] getSmoothCoordinate() {
		float[] smoothCoordinate = new float[2];
		int coordinateSize = mCoordinateTemp.size();

		for (int j = 0; j < coordinateSize; j++) {
			float[] temp = mCoordinateTemp.get(j);
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


}
