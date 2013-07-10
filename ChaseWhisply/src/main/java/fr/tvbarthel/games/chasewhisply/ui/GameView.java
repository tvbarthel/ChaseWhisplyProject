package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.GameInformation;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;

public class GameView extends View {

	private GameInformation mModel;
	private final Bitmap mCrossHairs;
	private final Bitmap mGhostBitmap;
	private final Bitmap mGhostTargetedBitmap;
	private final Bitmap mAmmoBitmap;
	private final Bitmap mBulletHoleBitmap;
	private final String mFragString;
	private final String mComboString;
	//ratio for displaying items
	private float mWidthRatioDegreeToPx;
	private float mHeightRatioDegreeToPx;


	public GameView(Context context, GameInformation model) {
		super(context);
		mModel = model;

		//initialize bitmap drawn after
		mCrossHairs = BitmapFactory.decodeResource(getResources(), R.drawable.crosshair_black);
		mGhostBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
		mGhostTargetedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ghost_targeted);
		mAmmoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammo);
		mBulletHoleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bullethole);

		final Resources res = getResources();
		mFragString = res.getString(R.string.in_game_kill_counter);
		mComboString = res.getString(R.string.in_game_combo_counter);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//initialize ratio degree / screen px
		mWidthRatioDegreeToPx = this.getWidth() / mModel.getSceneWidth();
		mHeightRatioDegreeToPx = this.getHeight() / mModel.getSceneHeight();

		drawDisplayableItems(canvas);
		drawCrossHair(canvas);
		drawAmmo(canvas);
		drawKill(canvas);
		drawCombo(canvas);
	}

	private void drawCrossHair(Canvas canvas) {
		canvas.drawBitmap(mCrossHairs, (float) (getWidth() - mCrossHairs.getWidth()) / 2,
				(float) (getHeight() - mCrossHairs.getHeight()) / 2, new Paint());
	}

	/**
	 * draw ammo on player screen
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawAmmo(Canvas canvas) {
		canvas.drawBitmap(mAmmoBitmap, (float) (getWidth() - mAmmoBitmap.getWidth() - 10),
				(float) (getHeight() - mAmmoBitmap.getHeight()), new Paint());
		Paint ammos = new Paint();
		ammos.setStyle(Paint.Style.FILL_AND_STROKE);
		ammos.setColor(Color.WHITE);
		ammos.setStrokeWidth(4);
		ammos.setTextSize(mAmmoBitmap.getHeight() / 2);
		canvas.drawText(String.valueOf(mModel.getWeapon().getCurrentAmmunition())
				, getWidth() - mAmmoBitmap.getWidth() - ammos.getTextSize()
				, getHeight() - (mAmmoBitmap.getHeight() / 4)
				, ammos);
	}

	/**
	 * draw kill counter
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawKill(Canvas canvas) {
		Paint kill = new Paint();
		kill.setStyle(Paint.Style.FILL_AND_STROKE);
		kill.setColor(Color.WHITE);
		kill.setStrokeWidth(4);
		kill.setTextSize(getWidth() / 20);
		canvas.drawText(String.format(mFragString, mModel.getFragNumber())
				, 10
				, 10 + getWidth() / 20
				, kill);
	}

	/**
	 * draw combo counter
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawCombo(Canvas canvas) {
		final int comboNumber = mModel.getCurrentCombo();
		if (comboNumber > 0) {
			Paint combo = new Paint();
			combo.setStyle(Paint.Style.FILL_AND_STROKE);
			combo.setColor(Color.WHITE);
			combo.setStrokeWidth(4);
			combo.setTextSize(getWidth() / 30);
			canvas.drawText(String.format(mComboString, mModel.getCurrentCombo())
					, getWidth() / 2 + mCrossHairs.getWidth() / 2
					, getHeight() / 2 + mCrossHairs.getHeight() / 2
					, combo);
		}
	}

	/**
	 * draw active items on the screen
	 *
	 * @param canvas canvas from View.onDraw method
	 */
	private void drawDisplayableItems(Canvas canvas) {
		final float[] currentPos = mModel.getCurrentPosition();
		currentPos[0] *= mWidthRatioDegreeToPx;
		currentPos[1] *= mHeightRatioDegreeToPx;
		for (DisplayableItem i : mModel.getItemsForDisplay()) {
			switch (i.getType()) {
				case DisplayableItemFactory.TYPE_EASY_GHOST:
					if (!((TargetableItem) i).isAlive()) {
						//Ghost dead
					} else {
						//Ghost alive
						if (isTargeted(currentPos, i, mGhostBitmap)) {
							//Ghost alive and targeted
							renderItem(canvas, mGhostTargetedBitmap, i, mGhostTargetedBitmap.getWidth(), mGhostTargetedBitmap.getHeight());
							mModel.setCurrentTarget((TargetableItem) i);
						} else {
							//Ghost alive and not targeted
							renderItem(canvas, mGhostBitmap, i, mGhostBitmap.getWidth(), mGhostBitmap.getHeight());
							if (i == mModel.getCurrentTarget()) {
								mModel.removeTarget();
							}
						}
					}
					break;
				case DisplayableItemFactory.TYPE_BULLET_HOLE:
					renderItem(canvas, mBulletHoleBitmap, i, mBulletHoleBitmap.getWidth(), mBulletHoleBitmap.getHeight());
					break;
			}
		}
	}

	/**
	 * used to know if a targetable item is targeted
	 *
	 * @param crosshairPosition current crosshair x and y
	 * @param t                 targetable item
	 * @param targetableRes     bitmap used to draw this targetable
	 * @return true if targeted
	 */
	private boolean isTargeted(float[] crosshairPosition, DisplayableItem t, Bitmap targetableRes) {
		final int xInPx = (int) (t.getX() * mWidthRatioDegreeToPx);
		final int yInPx = (int) (t.getY() * mHeightRatioDegreeToPx);
		if (crosshairPosition[0] > xInPx - targetableRes.getWidth() / 2 & crosshairPosition[0] < xInPx + targetableRes.getWidth() / 2
				& crosshairPosition[1] > yInPx - targetableRes.getHeight() / 2 & crosshairPosition[1] < yInPx + targetableRes.getHeight() / 2) {
			return true;
		} else {
			return false;
		}
	}

	public void renderItem(final Canvas canvas, final Bitmap bitmap, final DisplayableItem item, final int mWidth, final int mHeight) {
		final float[] currentPosInDegree = mModel.getCurrentPosition();
		//normalization, avoid negative value.
		currentPosInDegree[0] += 180;
		currentPosInDegree[1] += 180;

		final float[] itemPosInDegree = new float[]{item.getX() + 180, item.getY() + 180};

		final int thisWidth = this.getWidth();
		final int thisHeight = this.getHeight();

		final float windowXInPx = currentPosInDegree[0] * mWidthRatioDegreeToPx - thisWidth / 2;
		final float windowYInPx = currentPosInDegree[1] * mHeightRatioDegreeToPx - thisHeight / 2;

		float itemXInPx = itemPosInDegree[0];
		float itemYInPx = itemPosInDegree[1];

		float diffX = currentPosInDegree[0] - itemPosInDegree[0];
		float diffY = currentPosInDegree[1] - itemPosInDegree[1];

		float distX = Math.abs(diffX);
		float distY = Math.abs(diffY);

		if (distX > 360 - distX) {
			itemXInPx = currentPosInDegree[0] - diffX + Math.signum(diffX) * 360;
		}

		if (distY > 360 - distY) {
			itemYInPx = currentPosInDegree[1] - diffY + Math.signum(diffY) * 360;
		}

		itemXInPx = itemXInPx * mWidthRatioDegreeToPx - mWidth / 2;
		itemYInPx = itemYInPx * mHeightRatioDegreeToPx - mHeight / 2;

		final float borderLeft = windowXInPx - mWidth;
		final float borderTop = windowYInPx - mHeight;
		final float borderRight = borderLeft + thisWidth + mWidth;
		final float borderBottom = borderTop + thisHeight + mHeight;

		if (itemXInPx > borderLeft && itemXInPx < borderRight && itemYInPx < borderBottom && itemYInPx > borderTop) {
			canvas.drawBitmap(bitmap, itemXInPx - windowXInPx, itemYInPx - windowYInPx, new Paint());
		}
	}
}
