package fr.tvbarthel.games.chasewhisply.ui.dialogfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;

public class CraftRequestDialogFragment extends DialogFragment {
    private static final String EXTRA_INVENTORY_ITEM_ENTRY = "CraftRequestDialogFragment.Extra.InventoryItemEntry";
    private Listener mListener;
    private InventoryItemEntry mInventoryItemEntry;

    public static CraftRequestDialogFragment newInstance(InventoryItemEntry inventoryItemEntry) {
        CraftRequestDialogFragment fragment = new CraftRequestDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_INVENTORY_ITEM_ENTRY, inventoryItemEntry);
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
    public CraftRequestDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CraftRequestDialogFragment.Listener) {
            mListener = (CraftRequestDialogFragment.Listener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet CraftRequestDialogFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mInventoryItemEntry = getArguments().getParcelable(EXTRA_INVENTORY_ITEM_ENTRY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(String.format(getString(R.string.craft_dialog_fragment_request),
                mInventoryItemEntry.getRecipe().toString(getActivity()),
                getResources().getQuantityString(mInventoryItemEntry.getTitleResourceId(), 1)));
        builder.setPositiveButton(R.string.craft_dialog_fragment_yes_response, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onCraftValidated(mInventoryItemEntry);
            }
        });

        builder.setNegativeButton(R.string.craft_dialog_fragment_no_response, null);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setBackgroundResource(R.drawable.button_dialog);
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setBackgroundResource(R.drawable.button_dialog);
            }
        });
        return alertDialog;
    }

    public interface Listener {
        public void onCraftValidated(InventoryItemEntry inventoryItemEntry);
    }
}
