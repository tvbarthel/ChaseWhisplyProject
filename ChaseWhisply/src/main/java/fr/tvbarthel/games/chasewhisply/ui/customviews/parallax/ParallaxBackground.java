package fr.tvbarthel.games.chasewhisply.ui.customviews.parallax;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

/**
 * Parallax background used to adapt any Bitmap in a viable
 */
public class ParallaxBackground extends View {

    /**
     * parallax background image
     */
    private Bitmap mParallaxBackground;

    /**
     * Rect used to store original window
     */
    private Rect mParallaxDim;

    public ParallaxBackground(Context context, Bitmap parallaxBackground) {
        super(context);

        mParallaxBackground = parallaxBackground;
        mParallaxDim = new Rect(0, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * Draw the given Bitmap background. Use rect determine with (@see determineWellFormedRect)
         * as subset Bitmap to be drawn and take into account the margin for destination area
         */
        canvas.drawBitmap(mParallaxBackground, mParallaxDim,
                new Rect(0, 0, this.getRight() + this.getWidth() / 2,
                        this.getBottom() + this.getHeight() / 2), null
        );
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        determineWellFormedRect();
    }

    /**
     * Adapt any bitmap to fit the parallax background without been stretched or deformed
     */
    private void determineWellFormedRect() {
        if (mParallaxDim.left == 0 && mParallaxDim.right == 0) {

            /**
             * Calculate current view ratio. The displayed bitmap should match it since we want a
             * well formed image
             */
            final float destRatio = (float) this.getWidth() / this.getHeight();

            /**
             * Calculate the original Bitmap ratio in order to determine which dimension has to be
             * re calculated
             */
            final float srcRatio = (float) mParallaxBackground.getWidth() / mParallaxBackground.getHeight();

            /**
             * Initialize width and height with Bitmap value
             */
            double fitWidth = mParallaxBackground.getWidth();
            double fitHeight = mParallaxBackground.getHeight();

            /**
             * Compare destination ration and source ratio in order to re adjust width or weight.
             *
             * After this we have fitHeight <= srcHeight & fitWidth <= srcWidth
             *
             * As well as fitWidth / fitHeight = destRatio to ensure a well formed rendering
             */
            if (srcRatio < destRatio) {
                fitHeight = fitWidth / destRatio;
            } else {
                fitWidth = fitHeight * destRatio;
            }

            /**
             * Determine the rectangle corresponding to the calculated area
             */
            //left corner window = original center - windowWidth/2
            final int left = (int) (mParallaxBackground.getWidth() / 2.0f - fitWidth / 2.0f);
            //top corner window = original center - windowHeight/2
            final int top = (int) (mParallaxBackground.getHeight() / 2.0f - fitHeight / 2.0f);
            final int right = left + (int) fitWidth;
            final int bottom = top + (int) fitHeight;

            mParallaxDim.set(left, top, right, bottom);
        }
    }
}
