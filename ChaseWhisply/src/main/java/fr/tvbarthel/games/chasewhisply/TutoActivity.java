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
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.ui.TutoFragment;

public class TutoActivity extends FragmentActivity {
	public static final int NB_PAGES = 4;
	private static final String FIRST_LAUNCH_KEY = "First_launch_KEY";
	private SharedPreferences mPrefs;
	private String[] mPageTitles;

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

		mPageTitles = new String[]{getResources().getString(R.string.tuto_default_title),
				getResources().getString(R.string.tuto_title_slide_1),
				getResources().getString(R.string.tuto_title_slide_2),
				getResources().getString(R.string.tuto_title_slide_3)};

		final ViewPager pager = (ViewPager) findViewById(R.id.pager);
		final TutoPagerAdapter adapter = new TutoPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(adapter.getCount());
		pager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.tuto_page_margin));
		final TextView tutoTitle = (TextView) findViewById(R.id.tuto_title);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {
				tutoTitle.setText(adapter.getPageTitle(i));
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});

		final Button closeButton = (Button) findViewById(R.id.closeButton);
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

	private class TutoPagerAdapter extends FragmentPagerAdapter {
		public TutoPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			final int drawableResId;
			switch (position) {
				case 0:
					drawableResId = R.drawable.tuto_1_bis;
					break;
				case 1:
					drawableResId = R.drawable.tuto_2_bis;
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
		public CharSequence getPageTitle(int position) {
			return mPageTitles[position];
		}

		@Override
		public int getCount() {
			return NB_PAGES;
		}
	}
}
