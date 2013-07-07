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
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;

public class GameView extends View {

	private GameInformation mModel;
	//TODO remove coordinate
	private float[] mCoordinate;
	private final Bitmap mCrossHairs;
	private final Bitmap mGhostBitmap;
	//ratio for displaying items
	private float mWidthRatioDegreeToPx;
	private float mHeightRatioDegreeToPx;


	public GameView(Context context, GameInformation model) {
		super(context);
		mModel = model;

		//initialize bitmap drawn after
		mCrossHairs = BitmapFactory.decodeResource(getResources(), R.drawable.crosshair_black);
		mGhostBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);

		//TODO remove coordinate
		mCoordinate = new float[2];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//initialize ratio degree / screen px
		mWidthRatioDegreeToPx = this.getWidth() / mModel.getSceneWidth();
		mHeightRatioDegreeToPx = this.getHeight() / mModel.getSceneHeight();

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
			switch (i.getType()) {
				case DisplayableItemFactory.TYPE_EASY_GHOST:
					renderItem(canvas, mGhostBitmap, i, mGhostBitmap.getWidth(), mGhostBitmap.getHeight());
					break;
			}
		}
	}

	public void renderItem(final Canvas canvas, final Bitmap bitmap, final DisplayableItem item, final int mWidth, final int mHeight) {
		final float[] currentPosInDegree = mModel.getCurrentPosition();

		final float windowXInPx = currentPosInDegree[0] * mWidthRatioDegreeToPx;
		final float windowYInPx = currentPosInDegree[1] * mHeightRatioDegreeToPx;
		final float itemXInPx = item.getX() * mWidthRatioDegreeToPx;
		final float itemYInPx = item.getY() * mHeightRatioDegreeToPx;

		final float borderLeft = windowXInPx - mWidth;
		final float borderTop = windowYInPx - mHeight;
		final float borderRight = borderLeft + this.getWidth() + mWidth;
		final float borderBottom = borderTop + this.getHeight() + mHeight;

		if (itemXInPx > borderLeft && itemXInPx < borderRight && itemYInPx < borderBottom && itemYInPx > borderTop) {
			canvas.drawBitmap(bitmap, itemXInPx - windowXInPx, itemYInPx - windowYInPx, new Paint());
		}
	}

	//TODO remove coordinate
	public void setCoordinate(float[] coordinate) {
		mCoordinate = coordinate;
	}
}
