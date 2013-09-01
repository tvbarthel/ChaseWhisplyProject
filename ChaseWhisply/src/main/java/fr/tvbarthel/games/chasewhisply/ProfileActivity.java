package fr.tvbarthel.games.chasewhisply;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import fr.tvbarthel.games.chasewhisply.ui.fragments.BestiaryFragment;
import fr.tvbarthel.games.chasewhisply.ui.fragments.InventoryFragment;
import fr.tvbarthel.games.chasewhisply.ui.fragments.ProfileFragment;

public class ProfileActivity extends FragmentActivity implements ProfileFragment.Listener {

	private Toast mTextToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_container,
					new ProfileFragment(), null).commit();
		}
	}

	@Override
	public void onNotAvailableFeatureRequested() {
		makeToast(getResources().getString(R.string.soon_tm));
	}

	@Override
	public void onBestiaryRequested() {
		getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_container,
				new BestiaryFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	@Override
	public void onInventoryRequested() {
		getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_container,
				new InventoryFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
	}

	/**
	 * use to inform user
	 *
	 * @param message display on screen
	 */
	private void makeToast(String message) {
		if (mTextToast != null) {
			mTextToast.cancel();
		}
		mTextToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		mTextToast.show();
	}
}
