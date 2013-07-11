package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;

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

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_game_mode, this, true);

		mGameModeImage = (ImageButton) getChildAt(0);
		mGameModeRules = (TextView) getChildAt(1);

	}

	//TODO remove when model used

	/**
	 * set rules for game mode
	 *
	 * @param rules
	 */
	public void setGameModeRules(String rules) {
		mGameModeRules.setText(rules);
	}

	//TODO remove when model used

	/**
	 * set image for game mode image button
	 *
	 * @param resId id from drawable
	 */
	public void setGameModeImage(int resId) {
		mGameModeImage.setImageResource(resId);
	}

	/**
	 * set a listener for game mode selection
	 *
	 * @param listener
	 */
	public void setGameModeSelectedListener(OnClickListener listener) {
		mGameModeImage.setOnClickListener(listener);
	}
}
