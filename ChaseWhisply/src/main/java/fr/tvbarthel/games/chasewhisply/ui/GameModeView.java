package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameModeView extends LinearLayout {

	private TextView mGameModeRules;
	private ImageButton mGameModeImage;

	public GameModeView(Context context) {
		super(context, null);
	}

	public GameModeView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL);

		mGameModeImage = (ImageButton) getChildAt(0);
		mGameModeRules = (TextView) getChildAt(1);

	}

	/**
	 * set rules for game mode
	 *
	 * @param rules
	 */
	public void setGameModeRules(String rules) {
		mGameModeRules.setText(rules);
	}

	/**
	 * set image for game mode image button
	 *
	 * @param resId id from drawable
	 */
	public void setGameModeImage(int resId) {
		mGameModeImage.setImageResource(resId);
	}
}
