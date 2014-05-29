package fr.tvbarthel.games.chasewhisply.ui.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A simple {@link android.support.v4.app.DialogFragment} that is cancelable and has a OK positive button that does nothing.
 * <p/>
 * Title and message can be customized {@see SimpleDialogFragment#newInstance}
 */
public class SimpleDialogFragment extends DialogFragment {

    private static final String ARGS_TITLE = "SimpleDialogFragment.Args.Title";
    private static final String ARGS_MESSAGE = "SimpleDialogFragment.Args.Message";

    /**
     * Create a {@link fr.tvbarthel.games.chasewhisply.ui.dialogfragments.SimpleDialogFragment} with a custom title and message.
     *
     * @param titleResourceId   the string id of the title.
     * @param messageResourceId the string id of the message.
     * @return a new {@link fr.tvbarthel.games.chasewhisply.ui.dialogfragments.SimpleDialogFragment}
     */
    public static SimpleDialogFragment newInstance(int titleResourceId, int messageResourceId) {
        final SimpleDialogFragment instance = new SimpleDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt(ARGS_TITLE, titleResourceId);
        arguments.putInt(ARGS_MESSAGE, messageResourceId);
        instance.setArguments(arguments);
        return instance;
    }

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public SimpleDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, null);

        // Set the title
        if (arguments.containsKey(ARGS_TITLE)) {
            builder.setTitle(arguments.getInt(ARGS_TITLE));
        }

        // Set the message
        if (arguments.containsKey(ARGS_MESSAGE)) {
            builder.setMessage(arguments.getInt(ARGS_MESSAGE));
        }

        return builder.create();
    }
}
