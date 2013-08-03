package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.mechanics.GameEngine;
import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameInformationFactory;
import fr.tvbarthel.games.chasewhisply.mechanics.SurvivalGameEngine;
import fr.tvbarthel.games.chasewhisply.mechanics.TimeLimitedGameEngine;
import fr.tvbarthel.games.chasewhisply.model.GameMode;
import fr.tvbarthel.games.chasewhisply.model.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;
import fr.tvbarthel.games.chasewhisply.ui.GameScoreFragment;
import fr.tvbarthel.games.chasewhisply.ui.GameView;

public class GameActivity extends Activity implements SensorEventListener, GameEngine.IGameEngine,
		View.OnClickListener {

	public static final String EXTRA_GAME_MODE = "ExtraGameModeFromChooser";
	private static final float NOISE = 0.03f;
	private static final int TEMP_SIZE = 20;
	private final float[] orientationVals = new float[3];
	private final float[] rotationMatrix = new float[9];
	private final float[] mCoordinate = new float[2];
	private final ArrayList<float[]> mCoordinateTemp = new ArrayList<float[]>();
	private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
			, ViewGroup.LayoutParams.WRAP_CONTENT);
	private float[] magVals = new float[3];
	private float[] accelVals = new float[3];
	private Camera mCamera;
	private CameraPreview mCameraPreview;
	private GameInformation mGameInformation;
	private GameEngine mGameEngine;
	private GameView mGameView;
	private GameMode mGameMode;
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
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		if (intent == null || !intent.hasExtra(EXTRA_GAME_MODE)) {
			finish();
		} else {
			mGameMode = intent.getParcelableExtra(EXTRA_GAME_MODE);
		}

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

		if (mGameEngine != null) {
			mGameEngine.pauseGame();
		}

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
			//TODO update DisplayableItemsList
			smoothCoordinate = getSmoothCoordinate();
			mGameEngine.changePosition((float) Math.toDegrees(smoothCoordinate[0]),
					(float) Math.toDegrees(smoothCoordinate[1]));
			mGameView.invalidate();
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

	@Override
	public void onGameEngineStop() {
		final Intent scoreIntent = new Intent(this, HomeActivity.class);
		scoreIntent.putExtra(GameScoreFragment.EXTRA_GAME_INFORMATION, mGameInformation);
		setResult(RESULT_OK, scoreIntent);
		finish();
	}

	@Override
	public void onClick(View view) {
		mGameEngine.fire();
		mGameView.invalidate();
	}

	private class CameraAsyncTask extends AsyncTask<Void, Void, Camera> {
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
			}

			if (mGameInformation == null) {
				//Angle view
				final Camera.Parameters params = mCamera.getParameters();
				final float horizontalViewAngle = params.getHorizontalViewAngle();
				final float verticalViewAngle = params.getVerticalViewAngle();

				mGameInformation = GameInformationFactory.createEmptyWorld(
						horizontalViewAngle, verticalViewAngle, mGameMode);
			}

			mCameraPreview = new CameraPreview(GameActivity.this, mCamera);
			setContentView(mCameraPreview);

			//instantiate GameView with GameModel
			mGameView = new GameView(GameActivity.this, mGameInformation);
			mGameView.setOnClickListener(GameActivity.this);
			addContentView(mGameView, mLayoutParams);

			//Sensor
			mSensorManager.registerListener(GameActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
			mSensorManager.registerListener(GameActivity.this, mMagneticField, SensorManager.SENSOR_DELAY_GAME);

			//instantiate game engine
			switch (mGameMode.getType()) {
				case GameModeFactory.GAME_TYPE_REMAINING_TIME:
					mGameEngine = new TimeLimitedGameEngine(GameActivity.this, mGameInformation);
					mGameEngine.startGame();
					break;
				case GameModeFactory.GAME_TYPE_SURVIVAL:
					mGameEngine = new SurvivalGameEngine(GameActivity.this, mGameInformation, 1000);
					mGameEngine.startGame();
					break;
				default:
					finish();
					break;
			}
		}
	}
}
