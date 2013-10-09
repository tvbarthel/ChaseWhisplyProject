package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.graphics.Canvas;

import fr.tvbarthel.games.chasewhisply.model.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.GameInformationTutorial;

public class TutorialGameView extends GameView {

	/**
	 * Display tutorial information
	 *
	 * @param context
	 * @param model
	 */
	public TutorialGameView(Context context, GameInformation model) {
		super(context, model);
	}

	@Override
	public void onDrawing(Canvas c) {
		int step = ((GameInformationTutorial) mModel).getCurrentStep();
		drawStepMessage(c, step);
	}

	/**
	 * draw current step message
	 *
	 * @param c
	 * @param step
	 */
	private void drawStepMessage(Canvas c, int step) {
		switch (step) {
			case GameInformationTutorial.STEP_WELCOME:
				resetPainter();
				useGreenPainter();
				c.drawText("Welcome !",
						mScreenWidth / 2,
						(mScreenHeight - mCrossHairs.getHeight()) / 2 - 10,
						mPaint);
				break;
		}
	}
}
