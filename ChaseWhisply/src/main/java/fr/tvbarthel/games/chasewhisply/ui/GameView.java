package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;

public class GameView extends View {

	private ArrayList<DisplayableItem> mDisplayableItems;
	private final Bitmap mCrossHairs;

	public GameView(Context context, ArrayList<DisplayableItem> displayableItems) {
		super(context);
		mDisplayableItems = displayableItems;
		mCrossHairs = BitmapFactory.decodeResource(getResources(), R.drawable.crosshair_black);
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
	}
}
