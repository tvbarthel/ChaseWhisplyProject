package fr.tvbarthel.games.chasewhisply.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.ui.adapter.GameModeViewAdapter;

public class GameModeView extends RelativeLayout {

    private final TextView mGameModeDescription;
    private final ImageView mGameModeImage;
    private GameMode mModel;

    public GameModeView(Context context) {
        this(context, null);
    }

    public GameModeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.drawable.card_shadow);
        setClickable(true);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_game_mode, this, true);

        mGameModeImage = (ImageView) getChildAt(0);
        mGameModeDescription = (TextView) getChildAt(1);
    }

    /**
     * get GameMode
     *
     * @return mGameEngine
     */
    public GameMode getModel() {
        return mModel;
    }

    /**
     * set model to the view to configure rules and image
     *
     * @param model GameMode
     */
    public void setModel(GameMode model) {
        mModel = model;
        mGameModeImage.setImageResource(mModel.getImage());
    }

    public void setModelForLeaderboard(GameMode model) {
        mModel = model;
        mGameModeImage.setImageResource(mModel.getImage());
        mGameModeDescription.setText(mModel.getLeaderboardDescriptionStringId());
    }

    /**
     * set a listener for game mode selection
     *
     * @param listener
     */
    public void setGameModeSelectedListener(final GameModeViewAdapter.Listener listener) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGameModeSelected(GameModeView.this);
            }
        });
    }

    public void setGameModeEnabled(boolean isAllowed) {
        this.setEnabled(isAllowed);
        this.setClickable(isAllowed);
        this.setBackgroundResource(isAllowed ? R.drawable.card_shadow : R.drawable.card_shadow_disable);
        mGameModeDescription.setText(isAllowed ? mModel.getTitle() : mModel.getRequiredMessage());
    }
}
