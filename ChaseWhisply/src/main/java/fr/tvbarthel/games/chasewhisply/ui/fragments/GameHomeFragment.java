package fr.tvbarthel.games.chasewhisply.ui.fragments;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.HomeActivity;
import fr.tvbarthel.games.chasewhisply.R;

public class GameHomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    public static final String FRAGMENT_TAG = "GameHomeFragment_TAG";
    private static final String STATE_SIGNED_IN = "State_signed";
    private boolean mSignedIn;
    private Listener mListener = null;
    //animation
    private Animation mWhisplyAnimation;
    private boolean mIsWhisplyAnimationRunning;


    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public GameHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_SIGNED_IN)) {
            mSignedIn = savedInstanceState.getBoolean(STATE_SIGNED_IN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        final int[] clickable = new int[]{
                R.id.home_play,
                R.id.home_leaderboard,
                R.id.home_achievement,
                R.id.home_about,
                R.id.home_sign_in,
                R.id.home_sign_out,
                R.id.home_whisply_picture,
                R.id.home_help_tuto,
                R.id.home_profile
        };
        for (int i : clickable) {
            final View view = v.findViewById(i);
            view.setOnClickListener(this);
            if (i == R.id.home_about || i == R.id.home_help_tuto) {
                view.findViewById(i).setOnLongClickListener(this);
            }
        }

        initWhisplyPicture(v);
        initVersionName(v);
        notifySignedStateChanged(mSignedIn, true, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTutoButtonAnimation();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof GameHomeFragment.Listener) {
            mListener = (GameHomeFragment.Listener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement GameHomeFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initVersionName(View v) {
        final TextView tvVersionName = (TextView) v.findViewById(R.id.home_version_name);
        try {
            final PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            tvVersionName.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tvVersionName.setVisibility(View.INVISIBLE);
        }

    }

    private void initWhisplyPicture(View v) {
        mIsWhisplyAnimationRunning = false;
        mWhisplyAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.whisply_picture_animation);
        if (mWhisplyAnimation == null) return;
        mWhisplyAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsWhisplyAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsWhisplyAnimationRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mListener == null)
            return;
        switch (view.getId()) {
            case R.id.home_play:
                mListener.onStartGameRequested();
                break;
            case R.id.home_leaderboard:
                mListener.onShowLeaderboardsRequested();
                break;
            case R.id.home_achievement:
                mListener.onShowAchievementsRequested();
                break;
            case R.id.home_about:
                mListener.onShowAboutRequested();
                break;
            case R.id.home_sign_out:
                mListener.onSignOutButtonClicked();
                break;
            case R.id.home_sign_in:
                mListener.onSignInButtonClicked();
                break;
            case R.id.home_whisply_picture:
                if (!mIsWhisplyAnimationRunning) {
                    view.startAnimation(mWhisplyAnimation);
                }
                mListener.onWhisplyPictureClicked();
                break;
            case R.id.home_help_tuto:
                mListener.onHelpRequested();
                break;
            case R.id.home_profile:
                mListener.onShowProfileRequested();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mListener == null)
            return false;
        switch (view.getId()) {
            case R.id.home_about:
                mListener.toast(getResources().getString(R.string.home_i_button_on_long_press));
                break;
            case R.id.home_help_tuto:
                mListener.toast(getResources().getString(R.string.home_help_button_on_long_press));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_SIGNED_IN, mSignedIn);
    }

    private void updateTutoButtonAnimation() {
        final boolean hasTutoEverBeenSeen = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(HomeActivity.KEY_HAS_TUTO_BEEN_SEEN, false);
        final View buttonTutoHelp = getView().findViewById(R.id.home_help_tuto);
        if (buttonTutoHelp != null) {
            if (!hasTutoEverBeenSeen) {
                final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.tuto_anim);
                if (animation != null) {
                    buttonTutoHelp.startAnimation(animation);
                }
            } else {
                buttonTutoHelp.clearAnimation();
            }
        }
    }

    public void notifySignedStateChanged(boolean signedIn) {
        notifySignedStateChanged(signedIn, false);
    }

    private void notifySignedStateChanged(boolean signedIn, boolean forceRefreshState) {
        notifySignedStateChanged(signedIn, forceRefreshState, null);
    }

    private void notifySignedStateChanged(boolean signedIn, boolean forceRefreshState, View rootView) {
        if (forceRefreshState || signedIn != mSignedIn) {
            final View signIn;
            final View signOut;
            final View signInInfo;
            if (rootView == null) {
                final Activity activity = getActivity();
                signIn = activity.findViewById(R.id.home_sign_in);
                signOut = activity.findViewById(R.id.home_sign_out);
                signInInfo = activity.findViewById(R.id.google_signin_info);
            } else {
                signIn = rootView.findViewById(R.id.home_sign_in);
                signOut = rootView.findViewById(R.id.home_sign_out);
                signInInfo = rootView.findViewById(R.id.google_signin_info);
            }
            if (signedIn) {
                signIn.setVisibility(View.GONE);
                signInInfo.setVisibility(View.GONE);
                signOut.setVisibility(View.VISIBLE);
            } else {
                signOut.setVisibility(View.GONE);
                signIn.findViewById(R.id.home_sign_in).setVisibility(View.VISIBLE);
                signInInfo.setVisibility(View.VISIBLE);
            }
            mSignedIn = signedIn;
        }
    }


    //interface
    public interface Listener {
        public void onStartGameRequested();

        public void onShowAchievementsRequested();

        public void onShowLeaderboardsRequested();

        public void onShowAboutRequested();

        public void onSignInButtonClicked();

        public void onSignOutButtonClicked();

        public void onWhisplyPictureClicked();

        public void onHelpRequested();

        public void onShowProfileRequested();

        public void toast(String message);
    }
}
