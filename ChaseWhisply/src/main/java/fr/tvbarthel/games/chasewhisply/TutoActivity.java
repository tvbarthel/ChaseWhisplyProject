package fr.tvbarthel.games.chasewhisply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import fr.tvbarthel.games.chasewhisply.ui.TutoFragment;

public class TutoActivity extends FragmentActivity {
	public static final int NB_PAGES = 4;
	private static final String FIRST_LAUNCH_KEY = "First_launch_KEY";
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tuto);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		final boolean firstLaunch = mPrefs.getBoolean(FIRST_LAUNCH_KEY, true);
		if (!firstLaunch) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

		final ViewPager pager = (ViewPager) findViewById(R.id.pager);
		final TutoPagerAdapter adapter = new TutoPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(adapter.getCount());
		pager.setPageMargin((int) getResources().getDimensionPixelSize(R.dimen.default_padding));

		Button closeButton = (Button) findViewById(R.id.closeButton);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeTutorial();
			}
		});
	}

	private void closeTutorial() {
		final Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		mPrefs.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply();
		finish();
	}


	private static class TutoPagerAdapter extends FragmentPagerAdapter {
		public TutoPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			final int drawableResId;
			switch (position) {
				case 0:
					drawableResId = R.drawable.tvbarthel_logo;
					break;
				case 1:
					drawableResId = R.drawable.ic_launcher;
					break;
				case 2:
					drawableResId = R.drawable.tvbarthel_logo;
					break;
				default:
					drawableResId = R.drawable.ic_launcher;
					break;
			}
			return TutoFragment.newInstance(drawableResId, position + 1);
		}

		@Override
		public int getCount() {
			return NB_PAGES;
		}
	}
}
