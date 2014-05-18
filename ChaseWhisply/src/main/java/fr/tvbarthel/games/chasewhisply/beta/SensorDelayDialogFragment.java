package fr.tvbarthel.games.chasewhisply.beta;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

import fr.tvbarthel.games.chasewhisply.R;

public class SensorDelayDialogFragment extends DialogFragment {

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public SensorDelayDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sensor Delay");

        final ArrayList<String> sensorDelayString = new ArrayList<String>();
        sensorDelayString.add("Delay Fastest");
        sensorDelayString.add("Delay Game");
        sensorDelayString.add("Delay Ui");
        sensorDelayString.add("Delay Normal");

        final ArrayList<Integer> sensorDelayInteger = new ArrayList<Integer>();
        sensorDelayInteger.add(SensorManager.SENSOR_DELAY_FASTEST);
        sensorDelayInteger.add(SensorManager.SENSOR_DELAY_GAME);
        sensorDelayInteger.add(SensorManager.SENSOR_DELAY_UI);
        sensorDelayInteger.add(SensorManager.SENSOR_DELAY_NORMAL);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(BetaUtils.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        int currentSensorDelayIndex = sensorDelayInteger.indexOf(sharedPreferences.getInt(BetaUtils.KEY_SENSOR_DELAY, SensorManager.SENSOR_DELAY_GAME));

        builder.setSingleChoiceItems(sensorDelayString.toArray(new String[]{}), currentSensorDelayIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferencesEditor.putInt(BetaUtils.KEY_SENSOR_DELAY, sensorDelayInteger.get(which));
            }
        });

        builder.setPositiveButton(R.string.craft_dialog_fragment_ok_response, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferencesEditor.commit();
            }
        });

        return builder.create();
    }
}
