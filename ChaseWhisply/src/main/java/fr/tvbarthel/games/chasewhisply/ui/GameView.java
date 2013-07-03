package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;

public class GameView extends View {

	private ArrayList<DisplayableItem> mDisplayableItems;
	//TODO remove coordinate
	private double[] mCoordinate;
	private final Bitmap mCrossHairs;

	public GameView(Context context, ArrayList<DisplayableItem> displayableItems) {
		super(context);
		mDisplayableItems = displayableItems;
		mCrossHairs = BitmapFactory.decodeResource(getResources(), R.drawable.crosshair_black);
		//TODO remove coordinate
		mCoordinate = new double[2];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final Paint paint = new Paint();
		for (DisplayableItem d : mDisplayableItems) {
			paint.reset();
			//TODO draw the items correctly
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			canvas.drawText("DISPLAY ITEM", d.getX(), d.getY(), paint);
		}
		drawCrossHair(canvas);
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

	//TODO remove coordinate
	public void setCoordinate(double[] coordinate) {
		mCoordinate = coordinate;
	}
}
