package fr.tvbarthel.games.chasewhisply.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.GameMode;

public class GameModeView extends LinearLayout {

	private TextView mGameModeRules;
	private ImageButton mGameModeImage;
	private GameMode mModel;

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

	/**
	 * set model to the view to configure rules and image
	 *
	 * @param model GameMode
	 */
	public void setModel(GameMode model) {
		mModel = model;
		mGameModeImage.setImageResource(mModel.getImage());
		mGameModeRules.setText(mModel.getRules());
	}

	/**
	 * set a listener for game mode selection
	 *
	 * @param listener
	 */
	public void setGameModeSelectedListener(OnClickListener listener) {
		mGameModeImage.setOnClickListener(listener);
	}

	public void setGameModeEnabled(boolean isAllowed) {
		mGameModeImage.setEnabled(isAllowed);
	}
}
