package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.text.DecimalFormat;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;

public class GameView extends View {

	private GameInformation mModel;
	//TODO remove coordinate
	private float[] mCoordinate;
	private final Bitmap mCrossHairs;

	public GameView(Context context, GameInformation model) {
		super(context);
		mModel = model;
		mCrossHairs = BitmapFactory.decodeResource(getResources(), R.drawable.crosshair_black);
		//TODO remove coordinate
		mCoordinate = new float[2];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCrossHair(canvas);
		drawDisplayableItems(canvas);
	}

	private void drawCrossHair(Canvas canvas) {
		canvas.drawBitmap(mCrossHairs, (float) (getWidth() - mCrossHairs.getWidth()) / 2,
				(float) (getHeight() - mCrossHairs.getHeight()) / 2, new Paint());
		//TODO remove coordinate information
		Paint coordinatePaint = new Paint();
		coordinatePaint.setStyle(Paint.Style.FILL);
		coordinatePaint.setColor(Color.BLACK);
		coordinatePaint.setTextSize(30);
		canvas.drawText(new DecimalFormat("##.##").format(mCoordinate[0]) + " ; " + new DecimalFormat("##.##").format(mCoordinate[1]), 30, 30, coordinatePaint);
	}

	/**
	 * draw active items on the screen
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawDisplayableItems(Canvas canvas) {
		for (DisplayableItem i : mModel.getItems()) {
			if (i.isActive()) {
				//draw items
			}
		}
	}

	//TODO remove coordinate
	public void setCoordinate(float[] coordinate) {
		mCoordinate = coordinate;
	}
}
