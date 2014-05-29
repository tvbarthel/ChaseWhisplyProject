package fr.tvbarthel.games.chasewhisply.ui.gameviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.engine.GameEngineStandard;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItem;
import fr.tvbarthel.games.chasewhisply.model.DisplayableItemFactory;
import fr.tvbarthel.games.chasewhisply.model.TargetableItem;
import fr.tvbarthel.games.chasewhisply.ui.RenderInformation;


public class GameViewStandard extends GameView {

    private GameEngineStandard mGameEngine;

    protected final Bitmap mCrossHairs;
    protected final Bitmap mGhostBitmap;
    protected final Bitmap mGhostTargetedBitmap;
    protected final Bitmap[] mBlondGhostBitmap;
    protected final Bitmap[] mBlondTargetedBitmap;
    protected final Bitmap mAmmoBitmap;
    protected final Bitmap mTimerBitmap;
    protected final Bitmap mScoreBitmap;
    protected final Bitmap mBulletHoleBitmap;
    protected final Bitmap mBabyGhostBitmap;
    protected final Bitmap mTargetedBabyGhostBitmap;
    protected final Bitmap[] mGhostWithHelmetBitmaps;
    protected final Bitmap[] mGhostWithHelmetTargetedBitmaps;
    protected final Bitmap mKingGhost;
    protected final Bitmap mTargetedKingGhost;
    protected final Bitmap mHiddenGhost;
    protected final String mComboString;
    protected final String mScoreString;
    protected final String mTimeString;

    public GameViewStandard(Context c, GameEngineStandard gameEngine) {
        super(c, gameEngine);
        mGameEngine = gameEngine;

        final Resources res = getResources();

        //initialize bitmap drawn after
        mCrossHairs = BitmapFactory.decodeResource(res, R.drawable.crosshair_white);
        mGhostBitmap = BitmapFactory.decodeResource(res, R.drawable.ghost);
        mGhostTargetedBitmap = BitmapFactory.decodeResource(res, R.drawable.ghost_targeted);
        mBlondGhostBitmap = new Bitmap[]{
                BitmapFactory.decodeResource(res, R.drawable.blond_ghost_in_tears),
                BitmapFactory.decodeResource(res, R.drawable.blond_ghost),
        };
        mBlondTargetedBitmap = new Bitmap[]{
                BitmapFactory.decodeResource(res, R.drawable.blond_ghost_in_tears_targeted),
                BitmapFactory.decodeResource(res, R.drawable.blond_ghost_targeted),
        };
        mAmmoBitmap = BitmapFactory.decodeResource(res, R.drawable.ic_ammo);
        mTimerBitmap = BitmapFactory.decodeResource(res, R.drawable.ic_timer);
        mScoreBitmap = BitmapFactory.decodeResource(res, R.drawable.ic_score);
        mBulletHoleBitmap = BitmapFactory.decodeResource(res, R.drawable.bullethole);
        mBabyGhostBitmap = BitmapFactory.decodeResource(res, R.drawable.baby_ghost);
        mTargetedBabyGhostBitmap = BitmapFactory.decodeResource(res, R.drawable.baby_ghost_targeted);
        mGhostWithHelmetBitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_5),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_4),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_3),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_2),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet),
        };

        mGhostWithHelmetTargetedBitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_5_targeted),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_4_targeted),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_3_targeted),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_2_targeted),
                BitmapFactory.decodeResource(res, R.drawable.ghost_with_helmet_targeted),
        };

        mHiddenGhost = BitmapFactory.decodeResource(res, R.drawable.hidden_ghost);
        mKingGhost = BitmapFactory.decodeResource(res, R.drawable.king_ghost);
        mTargetedKingGhost = BitmapFactory.decodeResource(res, R.drawable.targeted_king_ghost);

        mComboString = res.getString(R.string.in_game_combo_counter);
        mScoreString = res.getString(R.string.in_game_score);
        mTimeString = res.getString(R.string.in_game_time);
    }

    /**
     * draw active items on the screen
     *
     * @param canvas canvas from View.onDraw method
     */
    protected void drawDisplayableItems(Canvas canvas) {
        final float[] currentPos = mGameEngine.getCurrentPosition();
        currentPos[0] *= mWidthRatioDegreeToPx;
        currentPos[1] *= mHeightRatioDegreeToPx;
        for (DisplayableItem i : mGameEngine.getItemsForDisplay()) {
            switch (i.getType()) {
                case DisplayableItemFactory.TYPE_EASY_GHOST:
                    renderEasyGhost(canvas, (TargetableItem) i, currentPos);
                    break;
                case DisplayableItemFactory.TYPE_BABY_GHOST:
                    renderBabyGhost(canvas, (TargetableItem) i, currentPos);
                    break;
                case DisplayableItemFactory.TYPE_GHOST_WITH_HELMET:
                    renderGhostWithHelmet(canvas, (TargetableItem) i, currentPos);
                    break;
                case DisplayableItemFactory.TYPE_HIDDEN_GHOST:
                    renderHiddenGhost(canvas, (TargetableItem) i, currentPos);
                    break;
                case DisplayableItemFactory.TYPE_KING_GHOST:
                    renderKingGhost(canvas, (TargetableItem) i, currentPos);
                    break;
                case DisplayableItemFactory.TYPE_BLOND_GHOST:
                    renderBlondGhost(canvas, (TargetableItem) i, currentPos);
                    break;
                case DisplayableItemFactory.TYPE_BULLET_HOLE:
                    renderBulletHole(canvas, i);
                    break;
            }
        }
    }

    protected void renderGhostWithHelmet(Canvas canvas, TargetableItem ghostWithHelmet, float[] currentPos) {
        final int bitmapIndex = ghostWithHelmet.getHealth() - 1;
        renderGhost(canvas, ghostWithHelmet, currentPos, mGhostWithHelmetBitmaps[bitmapIndex], mGhostWithHelmetTargetedBitmaps[bitmapIndex]);
    }

    protected void renderEasyGhost(Canvas canvas, TargetableItem easyGhost, float[] currentPos) {
        renderGhost(canvas, easyGhost, currentPos, mGhostBitmap, mGhostTargetedBitmap);
    }

    protected void renderBabyGhost(Canvas canvas, TargetableItem babyGhost, float[] currentPos) {
        renderGhost(canvas, babyGhost, currentPos, mBabyGhostBitmap, mTargetedBabyGhostBitmap);
    }

    protected void renderHiddenGhost(Canvas canvas, TargetableItem hiddenGhost, float[] currentPos) {
        renderGhost(canvas, hiddenGhost, currentPos, mHiddenGhost, mGhostTargetedBitmap);
    }

    protected void renderKingGhost(Canvas canvas, TargetableItem kingGhost, float[] currentPos) {
        renderGhost(canvas, kingGhost, currentPos, mKingGhost, mTargetedKingGhost);
    }

    protected void renderBlondGhost(Canvas canvas, TargetableItem blondGhost, float[] currentPos) {
        final int bitmapIndex = blondGhost.getHealth() - 1;
        renderGhost(canvas, blondGhost, currentPos, mBlondGhostBitmap[bitmapIndex], mBlondTargetedBitmap[bitmapIndex]);
    }

    protected void renderGhost(Canvas canvas, TargetableItem ghost, float[] currentPos, Bitmap ghostBitmap, Bitmap targetedGhostBitmap) {
        if (!ghost.isAlive()) {
            //Ghost dead
        } else {
            //Ghost alive
            if (isTargeted(currentPos, ghost, ghostBitmap)) {
                //Ghost alive and targeted
                renderItem(canvas, targetedGhostBitmap, ghost);
                mGameEngine.setCurrentTarget(ghost);
            } else {
                //Ghost alive and not targeted
                final int oldAlpha = mPaint.getAlpha();
                mPaint.setAlpha(210);
                renderItem(canvas, ghostBitmap, ghost);
                mPaint.setAlpha(oldAlpha);
                if (ghost == mGameEngine.getCurrentTarget()) {
                    mGameEngine.removeTarget();
                }
            }
        }
    }

    protected void renderBulletHole(Canvas canvas, DisplayableItem bulletHole) {
        renderItem(canvas, mBulletHoleBitmap, bulletHole);
    }

    @Override
    public void onDrawing(Canvas c) {
        drawDisplayableItems(c);
        drawCrossHair(c);
        drawAmmo(c);
        drawCombo(c);
        drawScore(c);
    }

    /**
     * use to display a crossHair
     *
     * @param canvas
     */
    protected void drawCrossHair(Canvas canvas) {
        canvas.drawBitmap(mCrossHairs, (float) (mScreenWidth - mCrossHairs.getWidth()) / 2,
                (float) (mScreenHeight - mCrossHairs.getHeight()) / 2, mPaint);
    }

    /**
     * draw ammo on player screen, in green if ammo > 0 else in red
     *
     * @param canvas canvas from View.onDraw method
     */
    protected void drawAmmo(Canvas canvas) {
        final int currentAmmunition = mGameEngine.getCurrentAmmunition();
        final String ammo = String.valueOf(currentAmmunition);
        final int radius = Math.max(mAmmoBitmap.getWidth(), mAmmoBitmap.getHeight()) + (int) mPadding;
        resetPainter();

        //draw transparent overlay
        useTransparentBlackPainter();
        canvas.drawOval(new RectF(mScreenWidth - radius, mScreenHeight - radius, mScreenWidth + radius, mScreenHeight + radius), mPaint);

        if (currentAmmunition == 0) {
            useRedPainter();
            final String noAmmoMessage = getResources().getString(R.string.in_game_no_ammo_message);
            mPaint.getTextBounds(noAmmoMessage, 0, noAmmoMessage.length(), mBounds);
            canvas.drawText(noAmmoMessage,
                    mScreenWidth / 2,
                    (mScreenHeight + mCrossHairs.getHeight()) / 2 + mBounds.height(),
                    mPaint);
        } else {
            useWhitePainter();
        }

        canvas.drawBitmap(mAmmoBitmap, (float) (mScreenWidth - mAmmoBitmap.getWidth()),
                (float) (getHeight() - mAmmoBitmap.getHeight()), mPaint);

        mPaint.setTextSize(mAmmoBitmap.getHeight() / 2);
        mPaint.getTextBounds(ammo, 0, ammo.length(), mBounds);
        canvas.drawText(ammo
                , mScreenWidth - radius
                , mScreenHeight - radius + mBounds.height() / 2
                , mPaint);
    }

    /**
     * draw combo counter near crossHair
     *
     * @param canvas canvas from View.onDraw method
     */
    protected void drawCombo(Canvas canvas) {
        final int comboNumber = mGameEngine.getCurrentCombo();
        resetPainter();
        useGreenPainter();
        if (comboNumber > 1) {
            final String currentCombo = String.format(mComboString, mGameEngine.getCurrentCombo());
            mPaint.getTextBounds(currentCombo, 0, currentCombo.length(), mBounds);
            canvas.drawText(currentCombo
                    , mScreenWidth / 2 + mCrossHairs.getWidth() / 2 + mBounds.width() / 2
                    , mScreenHeight / 2 + mCrossHairs.getHeight() / 2
                    , mPaint);
        }
    }

    /**
     * draw score
     *
     * @param canvas canvas from View.onDraw method
     */
    protected void drawScore(Canvas canvas) {
        resetPainter();
        final String score = String.valueOf(mGameEngine.getCurrentScore());
        final int radius = Math.max(mScoreBitmap.getWidth(), mScoreBitmap.getHeight()) + (int) mPadding;

        //draw transparent overlay
        useTransparentBlackPainter();
        canvas.drawOval(new RectF(-radius, mScreenHeight - radius, radius, mScreenHeight + radius), mPaint);

        //draw score icon
        useWhitePainter();
        canvas.drawBitmap(mScoreBitmap, 0, mScreenHeight - mScoreBitmap.getHeight(), mPaint);

        //draw score
        mPaint.getTextBounds(score, 0, score.length(), mBounds);
        canvas.drawText(score
                , radius + mBounds.width() / 2
                , mScreenHeight - radius + mBounds.height() / 2
                , mPaint);

    }

    public void animateDyingGhost(TargetableItem ghost) {
        if (mAnimationLayer != null) {
            Bitmap bitmap;

            switch (ghost.getType()) {
                case DisplayableItemFactory.TYPE_BABY_GHOST:
                    bitmap = mTargetedBabyGhostBitmap;
                    break;
                case DisplayableItemFactory.TYPE_GHOST_WITH_HELMET:
                    bitmap = mGhostWithHelmetTargetedBitmaps[0];
                    break;
                case DisplayableItemFactory.TYPE_KING_GHOST:
                    bitmap = mTargetedKingGhost;
                    break;
                case DisplayableItemFactory.TYPE_BLOND_GHOST:
                    bitmap = mBlondGhostBitmap[0];
                    break;
                default:
                    bitmap = mGhostTargetedBitmap;
                    break;
            }

            final RenderInformation renderInformation = getRenderInformation(ghost, bitmap);
            mAnimationLayer.drawDyingGhost(
                    new BitmapDrawable(getResources(), bitmap),
                    mGameEngine.getLastScoreAdded(),
                    (int) mFontSize,
                    (int) (renderInformation.mPositionX), (int) (renderInformation.mPositionY));
        }
    }


}
