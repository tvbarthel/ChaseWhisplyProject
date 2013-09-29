package fr.tvbarthel.games.chasewhisply.ui.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fr.tvbarthel.games.chasewhisply.R;

public class CraftNotEnoughResourcesDialogFragment extends DialogFragment {
	public static final String EXTRA_ADDITIONAL_MESSAGE = "CraftNotEnoughResourcesDialogFragment.Extra.AdditionalMessage";

	public static CraftNotEnoughResourcesDialogFragment newInstance(String additionalMessage) {
		CraftNotEnoughResourcesDialogFragment fragment = new CraftNotEnoughResourcesDialogFragment();
		Bundle arguments = new Bundle();
		arguments.putString(EXTRA_ADDITIONAL_MESSAGE, additionalMessage);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(String.format(getString(R.string.craft_dialog_fragment_not_enough_resources),
				getArguments().getString(EXTRA_ADDITIONAL_MESSAGE)));
		builder.setNegativeButton(R.string.craft_dialog_fragment_ok_response, null);

		return builder.create();
	}
}
