package fr.tvbarthel.games.chasewhisply.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformation;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationStandard;
import fr.tvbarthel.games.chasewhisply.mechanics.informations.GameInformationTime;
import fr.tvbarthel.games.chasewhisply.mechanics.routine.Routine;
import fr.tvbarthel.games.chasewhisply.model.LevelInformation;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntryFactory;
import fr.tvbarthel.games.chasewhisply.model.mode.GameMode;
import fr.tvbarthel.games.chasewhisply.model.mode.GameModeFactory;
import fr.tvbarthel.games.chasewhisply.ui.dialogfragments.SimpleDialogFragment;

public class GameScoreFragment extends Fragment implements View.OnClickListener {
    public static final String FRAGMENT_TAG = "GameScoreFragment_TAG";
    public static final String EXTRA_GAME_INFORMATION = "GameScoreFragment.Extra.GameInformation";
    private static final String BUNDLE_IS_DISPLAY_DONE = GameScoreFragment.class.getName() + ".Bundle.isDisplayDone";
    private static final String BUNDLE_HAS_LEVELED_UP = GameScoreFragment.class.getName() + ".Bundle.hasLeveledUp";
    private static final String BUNDLE_HAS_INCREASED_RANK = GameScoreFragment.class.getName() + ".Bundle.hasIncreaseRank";
    private static final String BUNDLE_CURRENT_FINAL_SCORE =
            GameScoreFragment.class.getName() + ".Bundle.currentFinalScore";
    private static final String BUNDLE_CURRENT_ACHIEVEMENT_CHECKED =
            GameScoreFragment.class.getName() + ".Bundle.achievementChecked";
    private static final String BUNDLE_CURRENT_PLAYER_PROFILE_SAVED =
            GameScoreFragment.class.getName() + ".Bundle.playerProfileSaved";
    private static final String BUNDLE_CURRENT_EXP_EARNED =
            GameScoreFragment.class.getName() + ".Bundle.expEarned";
    private static final String BUNDLE_CURRENT_EXP_BAR =
            GameScoreFragment.class.getName() + ".Bundle.expBar";

    private static final long CLICK_DELAY = 1400;
    private static final long TICK_INTERVAL = 100;
    private static final int NUMBER_OF_TICK = 30;
    private static final int BITMAP_QUALITY = 80;

    private Listener mListener = null;
    private GameInformationStandard mGameInformation;
    private Routine mRoutine;
    private float mCurrentExpEarned;
    private float mExpEarnedByTick;
    private float mFinalScoreByTick;
    private float mCurrentFinalScore;
    private float mCurrentExpBar;
    private float mExpBarByTick;
    private float mCurrentTickNumber;
    private boolean mIsDisplayDone = false;
    private boolean mAchievementChecked = false;
    private PlayerProfile mPlayerProfile;
    private boolean mPlayerProfileSaved = false;
    //Details from game played
    private long mRetrievedBulletFired;
    private long mRetrievedTargetKilled;
    private long mRetrievedExpEarned;
    private long mRetrievedCombo;
    private long mRetrievedScore;
    private long mRetrievedExpBar;
    //UI
    private TextView mFinalScoreTopTextView;
    private TextView mFinalScoreBottomTextView;
    private TextView mExpEarnedTextView;
    private Button mSkipButton;
    private View mSignInView;
    private ProgressBar mExpbar;
    private long mAttachTime;
    //Congratz card
    private boolean mHasLeveledUp;
    private boolean mHasIncreaseRank;

    public static GameScoreFragment newInstance(GameInformation gameInformation) {
        final GameScoreFragment fragment = new GameScoreFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(GameScoreFragment.EXTRA_GAME_INFORMATION,
                gameInformation);
        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public GameScoreFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAttachTime = System.currentTimeMillis();
        if (activity instanceof GameScoreFragment.Listener) {
            mListener = (GameScoreFragment.Listener) activity;
            mPlayerProfile = new PlayerProfile(activity.getSharedPreferences(
                    PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement GameScoreFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mRoutine != null) {
            mRoutine.stopRoutine();
            mRoutine = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Resources res = getResources();
        View v = inflater.inflate(R.layout.fragment_score, container, false);
        final int[] clickable = new int[]{
                R.id.score_button_replay,
                R.id.score_button_home,
                R.id.score_button_skip,
                R.id.score_button_share,
                R.id.score_button_next_mission,
                R.id.fragment_score_btn_loot_help
        };
        for (int i : clickable) {
            v.findViewById(i).setOnClickListener(this);
        }

        if (savedInstanceState != null) {
            mIsDisplayDone = savedInstanceState.getBoolean(BUNDLE_IS_DISPLAY_DONE, false);
            mAchievementChecked = savedInstanceState.getBoolean(BUNDLE_CURRENT_ACHIEVEMENT_CHECKED, false);
            mPlayerProfileSaved = savedInstanceState.getBoolean(BUNDLE_CURRENT_PLAYER_PROFILE_SAVED, false);
            mHasIncreaseRank = savedInstanceState.getBoolean(BUNDLE_HAS_INCREASED_RANK, false);
            mHasLeveledUp = savedInstanceState.getBoolean(BUNDLE_HAS_LEVELED_UP, false);
        }

        if (getArguments().containsKey(EXTRA_GAME_INFORMATION)) {
            mGameInformation = getArguments().getParcelable(EXTRA_GAME_INFORMATION);
            retrieveGameDetails(mGameInformation);

            //set info to details card
            ((TextView) v.findViewById(R.id.numberOfTargetsKilled)).setText(String.valueOf(mRetrievedTargetKilled));
            ((TextView) v.findViewById(R.id.numberOfBulletsFired)).setText(String.valueOf(mRetrievedBulletFired));
            ((TextView) v.findViewById(R.id.maxCombo)).setText(String.valueOf(mRetrievedCombo));
            ((TextView) v.findViewById(R.id.expEarned)).setText(String.valueOf(mRetrievedExpEarned));
            ((TextView) v.findViewById(R.id.fragment_score_game_mode_name)).setText(mGameInformation.getGameMode().getTitle());
        }

        //update playerProfile with value of this game
        updatePlayerProfile();

        //populate the view
        final LevelInformation levelInformation = mPlayerProfile.getLevelInformation();
        ((TextView) v.findViewById(R.id.result_level)).setText(String.format(getString(R.string.profile_level), levelInformation.getLevel()));
        ((TextView) v.findViewById(R.id.result_current_exp)).setText(String.format(getString(R.string.profile_exp), levelInformation.getExpProgress(), levelInformation.getExpNeededToLevelUp()));
        mRetrievedExpBar = levelInformation.getProgressInPercent();

        //congratz card ?
        final View congratzCard = v.findViewById(R.id.result_card_congratz);
        final TextView congratsText = (TextView) v.findViewById(R.id.result_congratz_message);
        if (mHasLeveledUp) {
            congratzCard.setVisibility(View.VISIBLE);
            congratsText.setText(getString(R.string.score_congratz_level_up) + "\n");
        }
        if (mHasIncreaseRank) {
            congratzCard.setVisibility(View.VISIBLE);
            congratsText.setText(congratsText.getText() + getString(R.string.score_congratz_rank_up));
        }

        mFinalScoreTopTextView = (TextView) v.findViewById(R.id.result_score_top);
        mFinalScoreBottomTextView = (TextView) v.findViewById(R.id.finalScore);
        mSkipButton = (Button) v.findViewById(R.id.score_button_skip);
        mSignInView = v.findViewById(R.id.sign_in_message);
        mExpEarnedTextView = (TextView) v.findViewById(R.id.result_earned_exp);
        mExpbar = (ProgressBar) v.findViewById(R.id.result_level_progess_bar);

        HashMap<Integer, Integer> loots = mGameInformation.getLoot();
        if (loots.size() != 0) {
            String stringLoot = "";
            for (Map.Entry<Integer, Integer> entry : loots.entrySet()) {
                InventoryItemEntry inventoryItemEntry = InventoryItemEntryFactory.create(entry.getKey(), entry.getValue());
                final long quantityDropped = inventoryItemEntry.getQuantityAvailable();
                final int titleResourceId = inventoryItemEntry.getTitleResourceId();
                stringLoot += String.valueOf(quantityDropped) + "x " + res.getQuantityString(titleResourceId, (int) quantityDropped) + "\n";
            }
            stringLoot = stringLoot.substring(0, stringLoot.length() - 1);
            ((TextView) v.findViewById(R.id.score_loot_list)).setText(stringLoot);
        }

        //show the right rank
        String[] ranks = res.getStringArray(R.array.ranks_array_full);
        String[] grades = res.getStringArray(R.array.ranks_array_letter);
        final int gameRank = mGameInformation.getRank();
        switch (gameRank) {
            case GameModeFactory.GAME_RANK_DESERTER:
            case GameModeFactory.GAME_RANK_SOLDIER:
            case GameModeFactory.GAME_RANK_CORPORAL:
            case GameModeFactory.GAME_RANK_SERGEANT:
            case GameModeFactory.GAME_RANK_ADMIRAL:
                ((TextView) v.findViewById(R.id.result_rang)).setText(ranks[gameRank]);
                ((TextView) v.findViewById(R.id.result_grade)).setText(grades[gameRank]);
                break;
            default:
                ((TextView) v.findViewById(R.id.result_rang)).setText(ranks[0]);
                ((TextView) v.findViewById(R.id.result_grade)).setText(grades[0]);
                break;
        }


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRoutine = new Routine(Routine.TYPE_TICKER, TICK_INTERVAL) {
            @Override
            protected void run() {
                if (mCurrentTickNumber >= NUMBER_OF_TICK) {
                    finalizeScoreDisplayed();
                } else {
                    incrementCurrentScoreDisplayed();
                }
            }
        };

        if (mIsDisplayDone) {
            finalizeScoreDisplayed();
        } else if (hasSomethingToDisplay()) {
            initScoreDisplay(savedInstanceState);
            mRoutine.startRoutine();
        } else {
            mSkipButton.setVisibility(View.GONE);
            mIsDisplayDone = true;
        }
    }

    @Override
    public void onClick(View view) {
        if (mIsDisplayDone || (System.currentTimeMillis() - mAttachTime > CLICK_DELAY)) {
            switch (view.getId()) {
                case R.id.score_button_home:
                    mListener.onHomeRequested();
                    break;
                case R.id.score_button_skip:
                    finalizeScoreDisplayed();
                    break;
                case R.id.score_button_replay:
                    mListener.onReplayRequested(mGameInformation);
                    break;
                case R.id.score_button_share:
                    handleShareScore();
                    break;
                case R.id.score_button_next_mission:
                    mListener.onNextMissionRequested();
                    break;
                case R.id.fragment_score_btn_loot_help:
                    showLootHelpMessage();
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_DISPLAY_DONE, mIsDisplayDone);
        outState.putFloat(BUNDLE_CURRENT_FINAL_SCORE, mCurrentFinalScore);
        outState.putBoolean(BUNDLE_CURRENT_ACHIEVEMENT_CHECKED, mAchievementChecked);
        outState.putBoolean(BUNDLE_CURRENT_PLAYER_PROFILE_SAVED, mPlayerProfileSaved);
        outState.putFloat(BUNDLE_CURRENT_EXP_EARNED, mCurrentExpEarned);
        outState.putBoolean(BUNDLE_HAS_LEVELED_UP, mHasLeveledUp);
        outState.putBoolean(BUNDLE_HAS_INCREASED_RANK, mHasIncreaseRank);
    }

    private void showLootHelpMessage() {
        SimpleDialogFragment.newInstance(R.string.score_loot_help_title,
                R.string.score_loot_help_message).show(getFragmentManager(), null);
    }

    public void notifySignedStateChanged(boolean signedIn) {
        if (signedIn) {
            mSignInView.setVisibility(View.GONE);
            if (!mAchievementChecked) {
                mListener.onUpdateAchievements(mGameInformation, mPlayerProfile);
                mAchievementChecked = true;
            }
        } else {
            mSignInView.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getBitmapToShare() {
        final View fragmentView = GameScoreFragment.this.getView();
        final Paint paint = new Paint();
        Bitmap bitmap;

        // Get the game name.
        final View gameName = fragmentView.findViewById(R.id.fragment_score_game_mode_name);
        final int gameNameWidth = gameName.getWidth();
        final int gameNameHeight = gameName.getHeight();

        // Get the grade card.
        final View gradeCard = fragmentView.findViewById(R.id.result_card_grade);
        final int gradeCardWidth = gradeCard.getWidth();
        final int gradeCardHeight = gradeCard.getHeight();

        // Get the details card.
        final View detailsCard = fragmentView.findViewById(R.id.result_card_details);
        final int detailsCardWidth = detailsCard.getWidth();
        final int detailsCardHeight = detailsCard.getHeight();

        // Define some padding and a margin between the elements
        final int padding = 30;
        final int margin = 50;

        // Get the bitmap dimension.
        int bitmapWidth = Math.max(gameNameWidth, Math.max(gradeCardWidth, detailsCardWidth)) + padding;
        int bitmapHeight = padding + gameNameHeight + margin + gradeCardHeight + margin + detailsCardHeight;

        // Compute the left/top for bitmap drawing
        final int gameNameLeft = bitmapWidth / 2 - gameNameWidth / 2;
        final int gameNameTop = padding / 2 + margin / 2;
        final int gradeCardLeft = bitmapWidth / 2 - gradeCardWidth / 2;
        final int gradeCardTop = padding / 2 + gameNameHeight + margin;
        final int detailsCardLeft = bitmapWidth / 2 - detailsCardWidth / 2;
        final int detailsCardTop = padding / 2 + gameNameHeight + margin + gradeCardHeight + margin;

        // Initialize a bitmap with a canvas.
        Bitmap bitmapToShare = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapToShare);
        canvas.drawColor(GameScoreFragment.this.getResources().getColor(R.color.background_grey));

        // Draw the game name
        gameName.setDrawingCacheEnabled(true);
        bitmap = gameName.getDrawingCache(true);
        canvas.drawBitmap(bitmap, gameNameLeft, gameNameTop, paint);
        gameName.setDrawingCacheEnabled(false);
        bitmap.recycle();
        bitmap = null;

        // Draw the grade card.
        gradeCard.setDrawingCacheEnabled(true);
        bitmap = gradeCard.getDrawingCache(true);
        canvas.drawBitmap(bitmap, gradeCardLeft, gradeCardTop, paint);
        gradeCard.setDrawingCacheEnabled(false);
        bitmap.recycle();
        bitmap = null;

        // Draw the details card.
        detailsCard.setDrawingCacheEnabled(true);
        bitmap = detailsCard.getDrawingCache(true);
        canvas.drawBitmap(bitmap, detailsCardLeft, detailsCardTop, paint);
        detailsCard.setDrawingCacheEnabled(false);
        bitmap.recycle();
        bitmap = null;

        return bitmapToShare;
    }

    private Uri writeScoreBytesToExternalStorage(ByteArrayOutputStream scoreBytes) {
        try {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_HH_ss");
            final String filePrefix = "score_";
            final String fileSuffix = ".jpg";
            final String fileName = filePrefix + simpleDateFormat.format(new Date()) + fileSuffix;
            final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "ChaseWhisply", fileName);
            if (!f.getParentFile().isDirectory()) {
                f.getParentFile().mkdirs();
            }
            f.createNewFile();
            final FileOutputStream fo = new FileOutputStream(f);
            fo.write(scoreBytes.toByteArray());
            fo.close();
            return Uri.fromFile(f);
        } catch (IOException e) {
            Log.e("GameScoreFragment", "error while sharing score", e);
            return null;
        }
    }

    private void handleShareScore() {
        new AsyncTask<Void, Void, Uri>() {
            @Override
            protected Uri doInBackground(Void... params) {
                final Bitmap bitmapToShare = getBitmapToShare();

                //Compress the bitmap before saving and sharing.
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmapToShare.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                bitmapToShare.recycle();

                final Uri uriToShare = writeScoreBytesToExternalStorage(bytes);

                return uriToShare;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                super.onPostExecute(uri);
                if (uri != null) {
                    // Add the screen to the Media Provider's database.
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    getActivity().sendBroadcast(mediaScanIntent);

                    // Share intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    getActivity().startActivity(shareIntent);
                }
            }
        }.execute();
    }

    private void retrieveGameDetails(GameInformationStandard gameInformation) {
        mRetrievedBulletFired = gameInformation.getBulletsFired();
        mRetrievedTargetKilled = gameInformation.getFragNumber();
        mRetrievedCombo = gameInformation.getMaxCombo();
        mRetrievedExpEarned = gameInformation.getExpEarned();
        //TODO find a better way to display score
        final int gameType = gameInformation.getGameMode().getType();
        if (gameType == GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING
                || gameType == GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW) {
            mRetrievedScore = ((GameInformationTime) gameInformation).getPlayingTime();
        } else {
            mRetrievedScore = gameInformation.getCurrentScore();
        }
    }

    private void initCurrentScoreDisplayed(Bundle savedBundle) {
        if (savedBundle != null) {
            mCurrentFinalScore = savedBundle.getFloat(BUNDLE_CURRENT_FINAL_SCORE);
            mCurrentExpEarned = savedBundle.getFloat(BUNDLE_CURRENT_EXP_EARNED);
            mCurrentExpEarned = savedBundle.getFloat(BUNDLE_CURRENT_EXP_BAR);
        } else {
            mCurrentFinalScore = 0;
            mCurrentExpEarned = 0;
            mCurrentExpBar = 0;
        }
    }

    private void initScoreByTick() {
        if (mGameInformation != null) {
            mFinalScoreByTick = (float) (mRetrievedScore - mCurrentFinalScore) / NUMBER_OF_TICK;
            mExpEarnedByTick = (float) (mRetrievedExpEarned - mCurrentExpEarned) / NUMBER_OF_TICK;
            mExpBarByTick = (float) (mRetrievedExpBar - mCurrentExpBar) / NUMBER_OF_TICK;
        }
    }

    private void initScoreDisplay(Bundle savedBundle) {
        mCurrentTickNumber = 0;
        initCurrentScoreDisplayed(savedBundle);
        initScoreByTick();
    }

    private void incrementCurrentScoreDisplayed() {
        mCurrentTickNumber++;
        mCurrentFinalScore += mFinalScoreByTick;
        mCurrentExpEarned += mExpEarnedByTick;
        mCurrentExpBar += mExpBarByTick;

        displayDetails(
                (long) mCurrentFinalScore,
                (long) mCurrentExpEarned,
                (long) mCurrentExpBar);
    }

    private void finalizeScoreDisplayed() {
        mRoutine.stopRoutine();

        displayDetails(mRetrievedScore, mRetrievedExpEarned, mRetrievedExpBar);

        if (mIsDisplayDone) {
            mSkipButton.setVisibility(View.GONE);
        } else {
            fadeOut(mSkipButton);
            mIsDisplayDone = true;
        }

    }

    private void displayDetails(long score, long expEarned, long progressBar) {
        mExpEarnedTextView.setText(String.format(getString(R.string.score_added_exp), expEarned));
        mExpbar.setProgress((int) progressBar);
        //TODO create an abstract method displaysGamesDetails and implement a specific behavior for each mode
        switch (mGameInformation.getGameMode().getType()) {

            case GameModeFactory.GAME_TYPE_SURVIVAL:
            case GameModeFactory.GAME_TYPE_REMAINING_TIME:
            case GameModeFactory.GAME_TYPE_TUTORIAL:
            case GameModeFactory.GAME_TYPE_MEMORIZE:
                mFinalScoreTopTextView.setText(String.valueOf(score));
                mFinalScoreBottomTextView.setText(String.valueOf(score));
                break;
            case GameModeFactory.GAME_TYPE_TWENTY_IN_A_ROW:
            case GameModeFactory.GAME_TYPE_DEATH_TO_THE_KING:
                final Calendar cl = Calendar.getInstance();
                cl.setTimeInMillis(score);
                mFinalScoreTopTextView.setText(cl.get(Calendar.SECOND) + "' " + cl.get(Calendar.MILLISECOND) + "''");
                mFinalScoreBottomTextView.setText(cl.get(Calendar.SECOND) + "' " + cl.get(Calendar.MILLISECOND) + "''");
                break;
        }
    }

    private boolean hasSomethingToDisplay() {
        return mGameInformation.getCurrentScore() != 0 ||
                mGameInformation.getExpEarned() != 0;

    }

    private void fadeOut(final View view) {
        final ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).setDuration(500);
        fadeOutAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                fadeOutAnimation.removeListener(this);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                fadeOutAnimation.removeListener(this);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        fadeOutAnimation.start();
    }

    private void updatePlayerProfile() {
        final GameMode gameMode = mGameInformation.getGameMode();
        final int oldLevel = mPlayerProfile.getLevelInformation().getLevel();
        final int oldRank = mPlayerProfile.getRankByGameMode(gameMode);
        if (mGameInformation != null && !mPlayerProfileSaved) {
            mPlayerProfile.increaseBulletsFired(mRetrievedBulletFired);
            mPlayerProfile.increaseGamesPlayed(1);
            mPlayerProfile.increaseTargetsKilled(mRetrievedTargetKilled);
            mPlayerProfile.increaseBulletsMissed(mGameInformation.getBulletsMissed());
            mPlayerProfile.increaseExperienceEarned(mRetrievedExpEarned);
            mPlayerProfile.setRankByGameMode(gameMode, mGameInformation.getRank());
            updateInventoryEntryQuantity();
            mGameInformation.useBonus(mPlayerProfile);
            mPlayerProfileSaved = mPlayerProfile.saveChanges();
        }
        //check if player has leveled up
        if (oldLevel < mPlayerProfile.getLevelInformation().getLevel()) {
            mHasLeveledUp = true;
        }
        //check if player has increased his rank
        if (oldRank < mPlayerProfile.getRankByGameMode(gameMode)) {
            mHasIncreaseRank = true;
        }
    }

    private void updateInventoryEntryQuantity() {
        for (Map.Entry<Integer, Integer> entry : mGameInformation.getLoot().entrySet()) {
            mPlayerProfile.increaseInventoryItemQuantity(entry.getKey(), entry.getValue());
        }
    }

    //interface
    public interface Listener {
        public void onReplayRequested(GameInformation gameInformation);

        public void onNextMissionRequested();

        public void onHomeRequested();

        public void onUpdateAchievements(final GameInformationStandard gameInformation, final PlayerProfile playerProfile);

        public void onShareScoreRequested(long score);
    }
}
