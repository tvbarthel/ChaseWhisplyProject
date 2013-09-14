package fr.tvbarthel.games.chasewhisply;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemInformation;
import fr.tvbarthel.games.chasewhisply.model.inventory.Recipe;
import fr.tvbarthel.games.chasewhisply.ui.CraftNotEnoughResourcesDialogFragment;
import fr.tvbarthel.games.chasewhisply.ui.CraftRequestDialogFragment;
import fr.tvbarthel.games.chasewhisply.ui.fragments.BestiaryFragment;
import fr.tvbarthel.games.chasewhisply.ui.fragments.InventoryFragment;
import fr.tvbarthel.games.chasewhisply.ui.fragments.ProfileFragment;

public class ProfileActivity extends FragmentActivity implements ProfileFragment.Listener, InventoryFragment.Listener, CraftRequestDialogFragment.Listener {

	private Toast mTextToast;
	private PlayerProfile mPlayerProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		mPlayerProfile = new PlayerProfile(getSharedPreferences(PlayerProfile.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE));

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.profile_fragment_container,
					new ProfileFragment(), null).commit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		hideToast();
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
				new InventoryFragment(), InventoryFragment.TAG).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
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

	private void hideToast() {
		if (mTextToast != null) {
			mTextToast.cancel();
			mTextToast = null;
		}
	}

	public HashMap<Integer, Integer> getMissingResources(Recipe recipe) {
		final HashMap<InventoryItemInformation, Integer> ingredientsAndQuantities = recipe.getIngredientsAndQuantities();
		final HashMap<Integer, Integer> missingResources = new HashMap<Integer, Integer>();

		for (Map.Entry<InventoryItemInformation, Integer> entry : ingredientsAndQuantities.entrySet()) {
			int quantityRequested = entry.getValue();
			long quantityAvailable = mPlayerProfile.getInventoryItemQuantity(entry.getKey().getType());
			if (quantityAvailable < quantityRequested) {
				missingResources.put(entry.getKey().getTitleResourceId(), (int) (quantityRequested - quantityAvailable));
			}
		}

		return missingResources;
	}

	@Override
	public void onCraftRequested(InventoryItemEntry inventoryItemEntry) {
		final HashMap<Integer, Integer> missingResources = getMissingResources(inventoryItemEntry.getRecipe());
		if (missingResources.size() == 0) {
			CraftRequestDialogFragment.newInstance(inventoryItemEntry).show(getSupportFragmentManager(), null);
		} else {
			String missingResourcesString = new String();
			for (Map.Entry<Integer, Integer> entry : missingResources.entrySet()) {
				final int quantityMissing = entry.getValue();
				final int nameResourceId = entry.getKey();
				missingResourcesString += String.valueOf(quantityMissing) + "x " + getResources().getQuantityString(nameResourceId, quantityMissing) + ", ";
			}
			missingResourcesString = missingResourcesString.substring(0, missingResourcesString.length() - 2);
			CraftNotEnoughResourcesDialogFragment.newInstance(missingResourcesString).show(getSupportFragmentManager(), null);
		}

	}

	@Override
	public void onCraftValidated(InventoryItemEntry inventoryItemEntry) {
		for (Map.Entry<InventoryItemInformation, Integer> entry : inventoryItemEntry.getRecipe().getIngredientsAndQuantities().entrySet()) {
			mPlayerProfile.decreaseInventoryItemQuantity(entry.getKey().getType(), entry.getValue());
		}
		mPlayerProfile.increaseInventoryItemQuantity(inventoryItemEntry.getType());
		boolean areChangesSaved = mPlayerProfile.saveChanges();
		InventoryFragment inventoryFragment = (InventoryFragment) getSupportFragmentManager().findFragmentByTag(InventoryFragment.TAG);
		if (areChangesSaved && inventoryFragment != null) {
			inventoryFragment.loadInformation();
		}
	}
}
