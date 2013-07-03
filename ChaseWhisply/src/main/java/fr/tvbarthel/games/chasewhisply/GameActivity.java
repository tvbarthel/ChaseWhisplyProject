package fr.tvbarthel.games.chasewhisply;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.ui.CameraPreview;
import fr.tvbarthel.games.chasewhisply.ui.GameView;

public class GameActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mCameraPreview;
	private GameView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mCamera = getCameraInstance();

		if (mCamera == null) {
			finish();
		} else {
			mCameraPreview = new CameraPreview(this, mCamera);
			setContentView(mCameraPreview);
			mGameView = new GameView(this, new ArrayList<DisplayableItem>());
			mGameView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.i("Debug", "clicked !");
				}
			});
			addContentView(mGameView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
					, ViewGroup.LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
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
}
